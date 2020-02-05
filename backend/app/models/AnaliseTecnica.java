package models;

import com.itextpdf.text.DocumentException;
import exceptions.PortalSegurancaException;
import exceptions.ValidacaoException;
import main.java.br.ufla.lemaf.beans.pessoa.Endereco;
import main.java.br.ufla.lemaf.enums.TipoEndereco;
import models.licenciamento.*;
import models.pdf.PDFGenerator;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import models.validacaoParecer.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.libs.Crypto;
import services.IntegracaoEntradaUnicaService;
import utils.*;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static security.Auth.getUsuarioSessao;

@Entity
@Table(schema = "analise", name = "analise_tecnica")
public class AnaliseTecnica extends GenericModel implements Analisavel {

	public static final String SEQ = "analise.analise_tecnica_id_seq";
	private static final String NOME_PERFIL = "Analista Tecnico";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name = "id_analise")
	public Analise analise;

	@Column(name = "parecer")
	public String parecerAnalista;

	@Required
	@Column(name = "data_vencimento_prazo")
	public Date dataVencimentoPrazo;

	@Required
	@Column(name = "revisao_solicitada")
	public Boolean revisaoSolicitada;

	@Required
	@Column(name = "notificacao_atendida")
	public Boolean notificacaoAtendida;

	public Boolean ativo;

	@OneToOne
	@JoinColumn(name = "id_analise_tecnica_revisada", referencedColumnName = "id")
	public AnaliseTecnica analiseTecnicaRevisada;

	@Column(name = "data_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataInicio;

	@Column(name = "data_fim")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFim;

	@ManyToOne
	@JoinColumn(name = "id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@ManyToOne
	@JoinColumn(name = "id_tipo_resultado_validacao")
	public TipoResultadoAnalise tipoResultadoValidacao;

	@ManyToMany
	@JoinTable(schema = "analise", name = "rel_documento_analise_tecnica",
			joinColumns = @JoinColumn(name = "id_analise_tecnica"),
			inverseJoinColumns = @JoinColumn(name = "id_documento"))
	public List<Documento> documentos;

	@OneToMany(mappedBy = "analiseTecnica", cascade = CascadeType.ALL)
	public List<AnaliseDocumento> analisesDocumentos;

	@OneToOne(mappedBy = "analiseTecnica", cascade = CascadeType.ALL)
	public AnalistaTecnico analistaTecnico;

	@Column(name = "parecer_validacao")
	public String parecerValidacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_validacao", referencedColumnName = "id")
	public UsuarioAnalise usuarioValidacao;

	@OneToMany(mappedBy = "analiseTecnica", orphanRemoval = true)
	public List<LicencaAnalise> licencasAnalise;

	@OneToMany(mappedBy = "analiseTecnica", orphanRemoval = true)
	public List<ParecerTecnicoRestricao> pareceresTecnicosRestricoes;

	@OneToMany(mappedBy = "analiseTecnica")
	@Fetch(FetchMode.SUBSELECT)
	public List<ParecerGerenteAnaliseTecnica> pareceresGerenteAnaliseTecnica;

	@Column(name = "justificativa_coordenador")
	public String justificativaCoordenador;

	@ManyToOne
	@JoinColumn(name = "id_tipo_resultado_validacao_gerente")
	public TipoResultadoAnalise tipoResultadoValidacaoGerente;

	@Column(name = "parecer_validacao_gerente")
	public String parecerValidacaoGerente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_validacao_gerente", referencedColumnName = "id")
	public UsuarioAnalise usuarioValidacaoGerente;

	@OneToMany(mappedBy = "analiseTecnica", cascade = CascadeType.ALL)
	public List<Gerente> gerentes;

	@Required
	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@OneToMany(mappedBy = "analiseTecnica", fetch = FetchType.LAZY)
	public List<DesvinculoAnaliseTecnica> desvinculos;

	@ManyToOne
	@JoinColumn(name = "id_tipo_resultado_validacao_aprovador")
	public TipoResultadoAnalise tipoResultadoValidacaoAprovador;

	@Column(name = "parecer_validacao_aprovador")
	public String parecerValidacaoAprovador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_validacao_aprovador", referencedColumnName = "id")
	public UsuarioAnalise usuarioValidacaoAprovador;

	@Column(name = "data_fim_validacao_aprovador")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFimValidacaoAprovador;

	@OneToMany(mappedBy = "analiseTecnica")
	public List<InconsistenciaTecnica> inconsistenciasTecnica;

	@OneToMany(mappedBy = "analiseTecnica")
	public List<ParecerAnalistaTecnico> pareceresAnalistaTecnico;

	@OneToMany(mappedBy = "analiseTecnica", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	public List<Notificacao> notificacoes;

	@Transient
	public String linkNotificacao;

	@Transient
	public Integer prazoNotificacao;

	@Transient
	public Vistoria vistoria;

	@Transient
	public Long idAnalistaDestino;

	private void validarParecer() {

		if (StringUtils.isBlank(this.parecerAnalista))
			throw new ValidacaoException(Mensagem.ANALISE_PARECER_NAO_PREENCHIDO);

	}

	public static AnaliseTecnica findByProcessoAtivo(Processo processo) {

		return AnaliseTecnica.find("analise.processo.id = :idProcesso AND ativo = true")
				.setParameter("idProcesso", processo.id)
				.first();

	}

	public static AnaliseTecnica findByProcesso(Processo processo) {

		return AnaliseTecnica.find("analise.processo.id = :idProcesso")
				.setParameter("idProcesso", processo.id)
				.first();

	}

	public void iniciarAnaliseTecnicaGerente(UsuarioAnalise usuarioExecutor) {

		verificarDataInicio();

		this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_TECNICA_GERENTE, usuarioExecutor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
	}

	public void verificarDataInicio() {
		if (this.dataInicio == null) {

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			this.dataInicio = c.getTime();

			this._save();

			iniciarLicencas();
		}
	}

	public AnaliseTecnica save() {

		if (this.dataCadastro == null) {

			this.dataCadastro = new Date();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(this.dataCadastro);
		c.add(Calendar.DAY_OF_MONTH, Configuracoes.PRAZO_ANALISE_TECNICA);
		this.dataVencimentoPrazo = c.getTime();

		this.ativo = true;

		if (this.analise.diasAnalise.qtdeDiasTecnica == null) {

			this.analise.diasAnalise.qtdeDiasTecnica = 0;
			this.analise.diasAnalise.save();
		}

		return super.save();

	}

	public void iniciar(UsuarioAnalise usuarioExecutor) {

		if (this.dataInicio == null) {

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			this.dataInicio = c.getTime();

			this._save();

		}

		this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_TECNICA, usuarioExecutor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);

	}

	public Boolean validarEmissaoLicencas(List<LicencaAnalise> licencas) {

		for (int i = 0; i < licencas.size(); i++) {

			LicencaAnalise licencaVerificar = licencas.get(i);
			if (licencaVerificar.emitir) {
				return true;
			}

		}

		throw new ValidacaoException(Mensagem.ERRO_NENHUMA_LICENCA_EMITIDA);

	}

	private void iniciarLicencas() {

		List<LicencaAnalise> novasLicencasAnalise = new ArrayList<>();

		for (Caracterizacao caracterizacao : this.analise.processo.empreendimento.caracterizacoes) {

			LicencaAnalise novaLicencaAnalise = new LicencaAnalise();
			novaLicencaAnalise.caracterizacao = caracterizacao;
			novaLicencaAnalise.validade = caracterizacao.tipoLicenca.validadeEmAnos;

			novasLicencasAnalise.add(novaLicencaAnalise);
		}

		updateLicencasAnalise(novasLicencasAnalise);

	}

	public void update(AnaliseTecnica novaAnalise) {

		if (this.dataFim != null) {
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_CONCLUIDA);
		}

		this.parecerAnalista = novaAnalise.parecerAnalista;

		if (novaAnalise.tipoResultadoAnalise != null &&
				novaAnalise.tipoResultadoAnalise.id != null) {

			this.tipoResultadoAnalise = novaAnalise.tipoResultadoAnalise;
		}

		updateDocumentos(novaAnalise.documentos);

		if (this.analisesDocumentos == null) {

			this.analisesDocumentos = new ArrayList<>();
		}

		for (AnaliseDocumento novaAnaliseDocumento : novaAnalise.analisesDocumentos) {

			AnaliseDocumento analiseDocumento = ListUtil.getById(novaAnaliseDocumento.id, this.analisesDocumentos);

			if (analiseDocumento != null) {

				analiseDocumento.update(novaAnaliseDocumento);

			} else {

				novaAnaliseDocumento.analiseTecnica = this;
				this.analisesDocumentos.add(novaAnaliseDocumento);
			}
		}

		this._save();

		updateLicencasAnalise(novaAnalise.licencasAnalise);
		updatePareceresTecnicosRestricoes(novaAnalise.pareceresTecnicosRestricoes);

	}

	public void updatePareceresTecnicosRestricoes(List<ParecerTecnicoRestricao> pareceresSalvar) {

		if (this.pareceresTecnicosRestricoes == null)
			this.pareceresTecnicosRestricoes = new ArrayList<>();

		Iterator<ParecerTecnicoRestricao> pareceresTecnicosRestricoesIterator = this.pareceresTecnicosRestricoes.iterator();

		while (pareceresTecnicosRestricoesIterator.hasNext()) {

			ParecerTecnicoRestricao parecerTecnicoRestricao = pareceresTecnicosRestricoesIterator.next();

			if (ListUtil.getById(parecerTecnicoRestricao.id, pareceresSalvar) == null) {

				parecerTecnicoRestricao.delete();
				pareceresTecnicosRestricoesIterator.remove();
			}
		}

		for (ParecerTecnicoRestricao novoParecerTecnicoRestricao : pareceresSalvar) {

			ParecerTecnicoRestricao parecerTecnicoRestricao = ListUtil.getById(novoParecerTecnicoRestricao.id, this.pareceresTecnicosRestricoes);

			if (parecerTecnicoRestricao == null) { // novo parecer técnico restrição

				novoParecerTecnicoRestricao.analiseTecnica = this;
				novoParecerTecnicoRestricao.save();

				this.pareceresTecnicosRestricoes.add(novoParecerTecnicoRestricao);

			} else { // parecer técnico já existente

				parecerTecnicoRestricao.update(novoParecerTecnicoRestricao);

			}

		}

	}

	public void updateLicencasAnalise(List<LicencaAnalise> novasLicencasAnalise) {

		if (this.licencasAnalise == null) {

			this.licencasAnalise = new ArrayList<>();
		}

		for (LicencaAnalise novaLicencaAnalise : novasLicencasAnalise) {

			LicencaAnalise licencaAnalise = ListUtil.getById(novaLicencaAnalise.id, this.licencasAnalise);

			if (licencaAnalise != null) {

				licencaAnalise.update(novaLicencaAnalise);

			} else {

				novaLicencaAnalise.analiseTecnica = this;
				novaLicencaAnalise.save();

				this.licencasAnalise.add(novaLicencaAnalise);

			}

		}

	}

	private void updateDocumentos(List<Documento> novosDocumentos) {

		TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_ANALISE_TECNICA);

		if (this.documentos == null)
			this.documentos = new ArrayList<>();

		Iterator<Documento> docsCadastrados = documentos.iterator();
		List<Documento> documentosDeletar = new ArrayList<>();

		while (docsCadastrados.hasNext()) {

			Documento docCadastrado = docsCadastrados.next();

			if (ListUtil.getById(docCadastrado.id, novosDocumentos) == null) {

				docsCadastrados.remove();
				// remove o documeto do banco apenas se ele não estiver relacionado
				// com outra análises
				List<AnaliseTecnica> analisesTecnicasRelacionadas = docCadastrado.getAnaliseTecnicasRelacionadas();
				if (analisesTecnicasRelacionadas.size() == 0) {

					documentosDeletar.add(docCadastrado);

				}

			}

		}

		for (Documento novoDocumento : novosDocumentos) {

			if (novoDocumento.id == null) {

				novoDocumento.tipo = tipo;
				novoDocumento.save();
				this.documentos.add(novoDocumento);

			}

		}

		ModelUtil.deleteAll(documentosDeletar);

	}

	public static AnaliseTecnica findByNumeroProcesso(String numeroProcesso) {

		return AnaliseTecnica.find("analise.processo.numero = :numeroProcesso AND ativo = true")
				.setParameter("numeroProcesso", numeroProcesso)
				.first();

	}

	public void enviarEmailNotificacao(Notificacao notificacao, ParecerAnalistaTecnico parecerAnalistaTecnico, List<Documento> documentos) throws Exception {

		Empreendimento empreendimento = Empreendimento.findById(this.analise.processo.empreendimento.id);
		List<String> destinatarios = new ArrayList<>(Collections.singleton(empreendimento.cadastrante.contato.email));

		this.linkNotificacao = Configuracoes.URL_LICENCIAMENTO;

		if(notificacao.segundoEmailEnviado){

			notificacao._save();

			this.prazoNotificacao = notificacao.prazoNotificacao/3;

			EmailNotificacaoAnaliseTecnica emailNotificacaoAnaliseTecnica = new EmailNotificacaoAnaliseTecnica(this, parecerAnalistaTecnico, destinatarios);

			emailNotificacaoAnaliseTecnica.enviar();

		} else {

			Notificacao notificacaoSave = new Notificacao(this, notificacao, parecerAnalistaTecnico);

			this.prazoNotificacao = notificacaoSave.prazoNotificacao;

			EmailNotificacaoAnaliseTecnica emailNotificacaoAnaliseTecnica = new EmailNotificacaoAnaliseTecnica(this, parecerAnalistaTecnico, destinatarios);
			emailNotificacaoAnaliseTecnica.enviar();

			notificacaoSave.documentosNotificacaoTecnica.addAll(emailNotificacaoAnaliseTecnica.getPdfsNotificacao());
			notificacaoSave.save();

		}

	}

	public void validaParecer(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioExecutor) {

		TipoResultadoAnaliseChain<AnaliseTecnica> tiposResultadosAnalise = new ParecerValidadoTecnico();
		tiposResultadosAnalise.setNext(new SolicitarAjustesTecnico());
		tiposResultadosAnalise.setNext(new ParecerNaoValidadoTecnico());

		tiposResultadosAnalise.validarParecer(this, analiseTecnica, usuarioExecutor);

	}

	public void validaParecerGerente(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioExecutor) {

		TipoResultadoAnaliseChain<AnaliseTecnica> tiposResultadosAnalise = new ParecerValidadoTecnicoGerente();
		tiposResultadosAnalise.setNext(new SolicitarAjustesTecnicoGerente());
		tiposResultadosAnalise.setNext(new ParecerNaoValidadoTecnicoGerente());

		tiposResultadosAnalise.validarParecer(this, analiseTecnica, usuarioExecutor);

	}

	public void validarParecerValidacaoAprovador(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioExecutor) {

		TipoResultadoAnaliseChain<AnaliseTecnica> tiposResultadosAnalise = new SolicitarAjustesTecnicoAprovador();
		tiposResultadosAnalise.validarParecer(this, analiseTecnica, usuarioExecutor);

	}

	private void validarLicencasAnalise() {

		if (this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {

			for (LicencaAnalise licencaAnalise : this.licencasAnalise) {

				if (licencaAnalise.emitir == null) {

					throw new ValidacaoException(Mensagem.ANALISE_TECNICA_LICENCA_SEM_VALIDACAO);
				}

			}

		}

	}

	private void validarResultado() {

		if (this.tipoResultadoAnalise == null)
			throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO);

		boolean todosDocumentosValidados = true;
		for (AnaliseDocumento analise : this.analisesDocumentos) {

			if (analise.documento.tipo.tipoAnalise.equals(TipoAnalise.TECNICA)) {

				if (analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {

					throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
				}

				todosDocumentosValidados &= analise.validado;

			}

		}

		if (this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO && !todosDocumentosValidados) {

			throw new ValidacaoException(Mensagem.TODOS_OS_DOCUMENTOS_VALIDOS);

		}

	}

	private void validarAnaliseDocumentos() {

		if (this.analisesDocumentos == null || this.analisesDocumentos.size() == 0)
			throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);

		for (AnaliseDocumento analise : this.analisesDocumentos) {

			if (analise.documento.tipo.tipoAnalise.equals(TipoAnalise.TECNICA) &&
					(analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer)))) {

				throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
			}

		}

	}

	@Override
	public TipoResultadoAnalise getTipoResultadoValidacao() {

		return this.tipoResultadoValidacao != null ? this.tipoResultadoValidacao : this.tipoResultadoValidacaoGerente;

	}

	public void validarTipoResultadoValidacao() {

		if (tipoResultadoValidacao == null) {

			throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO_VALIDACAO);

		}

	}

	public void validarParecerValidacao() {

		if (StringUtils.isEmpty(parecerValidacao)) {

			throw new ValidacaoException(Mensagem.ANALISE_SEM_PARECER_VALIDACAO);

		}

	}

	public void validarTipoResultadoValidacaoGerente() {

		if (tipoResultadoValidacaoGerente == null) {

			throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO_VALIDACAO);

		}

	}

	public void validarParecerValidacaoGerente() {

		if (StringUtils.isEmpty(parecerValidacaoGerente)) {

			throw new ValidacaoException(Mensagem.ANALISE_SEM_PARECER_VALIDACAO);

		}

	}

	public void validarTipoResultadoValidacaoAprovador() {

		if (tipoResultadoValidacaoAprovador == null) {

			throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO_VALIDACAO);

		}

	}

	public void validarParecerValidacaoAprovador() {

		if (StringUtils.isEmpty(parecerValidacaoAprovador)) {

			throw new ValidacaoException(Mensagem.ANALISE_SEM_PARECER_VALIDACAO);

		}

	}

	public AnaliseTecnica gerarCopia(boolean notificacao) {

		AnaliseTecnica copia = new AnaliseTecnica();

		copia.analise = this.analise;
		copia.parecerAnalista = this.parecerAnalista;
		copia.dataCadastro = this.dataCadastro;
		copia.dataVencimentoPrazo = this.dataVencimentoPrazo;
		copia.revisaoSolicitada = !notificacao;
		copia.notificacaoAtendida = notificacao;
		copia.ativo = true;
		copia.analiseTecnicaRevisada = this;
		copia.dataInicio = this.dataInicio;
		copia.tipoResultadoAnalise = this.tipoResultadoAnalise;
		copia.documentos = new ArrayList<>(this.documentos);
		copia.analisesDocumentos = new ArrayList<>();
		copia.usuarioValidacao = this.usuarioValidacao;
		copia.usuarioValidacaoGerente = this.usuarioValidacaoGerente;

		for (AnaliseDocumento analiseDocumento : this.analisesDocumentos) {

			AnaliseDocumento copiaAnaliseDoc = analiseDocumento.gerarCopia();
			copiaAnaliseDoc.analiseTecnica = copia;
			copia.analisesDocumentos.add(copiaAnaliseDoc);
		}

		AnalistaTecnico copiaAnalistaTec = analistaTecnico.gerarCopia();

		copiaAnalistaTec.analiseTecnica = copia;

		copia.gerentes = new ArrayList<>();

		for (Gerente gerente : this.gerentes) {

			Gerente copiaGerenteTec = gerente.gerarCopia();

			copiaGerenteTec.analiseTecnica = copia;
			copia.gerentes.add(copiaGerenteTec);
		}

		copia.licencasAnalise = new ArrayList<>();
		for (LicencaAnalise licenca : this.licencasAnalise) {

			LicencaAnalise copiaLicencaAnalise = licenca.gerarCopia();

			copiaLicencaAnalise.analiseTecnica = copia;
			copia.licencasAnalise.add(copiaLicencaAnalise);
		}

		return copia;

	}

	public void setValidacaoGerente(AnaliseTecnica analise) {

		this.tipoResultadoValidacaoGerente = analise.tipoResultadoValidacaoGerente;
		this.parecerValidacaoGerente = analise.parecerValidacaoGerente;

	}

	public void setValidacaoCoordenador(AnaliseTecnica analise) {

		this.tipoResultadoValidacao = analise.tipoResultadoValidacao;
		this.parecerValidacao = analise.parecerValidacao;

	}

	public boolean hasGerentes() {

		return this.gerentes != null && this.gerentes.size() > 0;

	}

	public Gerente getGerente() {

		return this.gerentes.get(0);

	}

	public Documento gerarPDFParecer(ParecerAnalistaTecnico parecerAnalistaTecnico) throws Exception {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_TECNICA);

		List<Condicionante> condicionantes = Condicionante.findByIdParecer(parecerAnalistaTecnico.id);
		List<Restricao> restricoes = Restricao.findByIdParecer(parecerAnalistaTecnico.id);
		Vistoria vistoria = Vistoria.findByIdParecer(parecerAnalistaTecnico.id);
		String numeroProcesso = this.analise.processo.numero;
		String numeroProcessoLicenciamento = this.analise.processo.caracterizacao.processoLicenciamento.numero;

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("analiseEspecifica", this)
				.addParam("numeroProcessoLicenciamento", numeroProcessoLicenciamento)
				.addParam("numeroProcesso", numeroProcesso)
				.addParam("vistoria", vistoria)
				.addParam("parecer", parecerAnalistaTecnico)
				.addParam("condicionantes", condicionantes)
				.addParam("restricoes", restricoes)
				.addParam("dataDoParecer", Helper.getDataPorExtenso(new Date()))
				.addParam("nomeAnalista", usuarioExecutor.pessoa.nome)
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 2.0D, 4.0D);

		pdf.generate();

		Documento documento = new Documento(tipoDocumento, pdf.getFile(), Crypto.encryptAES(new Date().getTime() + "documento_parecer"), new Date());

		return documento;

	}

	public void geraLicencasAnaliseTecnica(List<LicencaAnalise> licencasAnalise) {

		licencasAnalise.forEach(licencaAnalise -> {

			LicencaAnalise l = LicencaAnalise.findById(licencaAnalise.id);
			l.analiseTecnica = this;
			l._save();

		});

	}

	public List<Documento> gerarPDFNotificacao(AnaliseTecnica analiseTecnica) {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_TECNICA);

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
		main.java.br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(analiseTecnica.analise.processo.empreendimento.getCpfCnpj());
		final Endereco enderecoCompleto = empreendimentoEU.enderecos.stream().filter(endereco -> endereco.tipo.id.equals(TipoEndereco.ID_PRINCIPAL)).findAny().orElseThrow(PortalSegurancaException::new);

		UsuarioAnalise analista;
		AnalistaVO analistaVO;

		if(analiseTecnica.id != null) {

			AnalistaTecnico analistaTecnico = AnalistaTecnico.findByAnaliseTecnica(analiseTecnica.id);

			analista = UsuarioAnalise.findById(analistaTecnico.usuario.id);

			analistaVO = new AnalistaVO(analista.pessoa.nome, NOME_PERFIL, analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor);

		} else {

			analista = getUsuarioSessao();
			analistaVO = new AnalistaVO(analista.pessoa.nome, analista.usuarioEntradaUnica.perfilSelecionado.nome, analista.usuarioEntradaUnica.setorSelecionado.sigla);

		}

		List<Documento> documentosNotificacao = new ArrayList<>();

		AnalistaVO finalAnalistaVO = analistaVO;

		analiseTecnica.inconsistenciasTecnica.forEach(inconsistencia -> {

			inconsistencia.setTipoDeInconsistenciaTecnica();

			PDFGenerator pdf = new PDFGenerator()
					.setTemplate(tipoDocumento.getPdfTemplate())
					.addParam("analiseTecnica", analiseTecnica)
					.addParam("inconsistencia", inconsistencia)
					.addParam("enderecoCompleto", enderecoCompleto)
					.addParam("analista", finalAnalistaVO)
					.addParam("vistoria", false)
					.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 4.0D, 4.0D);

			pdf.generate();

			documentosNotificacao.add(new Documento(tipoDocumento, pdf.getFile(), "notificacao_tecnica", new Date()));

		});

		if(analiseTecnica.vistoria.realizada && analiseTecnica.vistoria.inconsistenciaVistoria != null) {

			PDFGenerator pdf = new PDFGenerator()
					.setTemplate(tipoDocumento.getPdfTemplate())
					.addParam("analiseTecnica", analiseTecnica)
					.addParam("inconsistencia", analiseTecnica.vistoria.inconsistenciaVistoria)
					.addParam("enderecoCompleto", enderecoCompleto)
					.addParam("analista", finalAnalistaVO)
					.addParam("vistoria", true)
					.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 4.0D, 4.0D);

			pdf.generate();

			documentosNotificacao.add(new Documento(tipoDocumento, pdf.getFile(), "notificacao_tecnica", new Date()));
		}

		return documentosNotificacao;

	}
	
	public AnalistaTecnico getAnalistaTecnico() {

		return this.analistaTecnico;
	}

	public String getJustificativaUltimoParecer() {

		ParecerGerenteAnaliseTecnica parecerGerenteAnaliseTecnica = this.pareceresGerenteAnaliseTecnica.stream()
				.filter(parecer -> parecer.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.SOLICITAR_AJUSTES))
				.max(Comparator.comparing(ParecerGerenteAnaliseTecnica::getDataParecer))
				.orElseThrow(() -> new ValidacaoException(Mensagem.PARECER_NAO_ENCONTRADO));

		return parecerGerenteAnaliseTecnica.parecer;

	}

	public Documento gerarPDFRelatorioTecnicoVistoria() throws IOException, DocumentException {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.DOCUMENTO_RELATORIO_TECNICO_VISTORIA);

		Vistoria vistoria = ParecerAnalistaTecnico.getUltimoParecer(this.pareceresAnalistaTecnico).vistoria;

		Integer tamanhoEquipeVistoria = vistoria.equipe.size();

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseTecnica", this)
				.addParam("tamanhoEquipeVistoria", tamanhoEquipeVistoria)
                .addParam("vistoria", vistoria)
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 4.0D, 5.0D);

		pdf.generate();

		List<File> documentos = new ArrayList<>();

		documentos.add(vistoria.documentoRit.getFile());
		documentos.add(pdf.getFile());

		return new Documento(tipoDocumento, PDFGenerator.mergePDF(documentos), "documento_relatorio_tecnico_vistoria", new Date());

	}

}
