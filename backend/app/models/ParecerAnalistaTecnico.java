package models;
import exceptions.ValidacaoException;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.enums.TipoEndereco;
import models.licenciamento.StatusCaracterizacaoEnum;
import models.pdf.PDFGenerator;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.HistoricoTramitacao;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.GenericModel;
import play.libs.Crypto;
import services.IntegracaoEntradaUnicaService;
import utils.Mensagem;

import javax.persistence.*;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static models.tramitacao.AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_GERENTE;

@Entity
@Table(schema = "analise", name = "parecer_analista_tecnico")
public class ParecerAnalistaTecnico extends ParecerAnalista {

	public static final String SEQ = "analise.parecer_analista_tecnico_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id")
	public Long id;

	@ManyToOne
	@JoinColumn(name = "id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;

	@OneToOne
	@JoinColumn(name = "id_usuario_analista_tecnico", referencedColumnName = "id")
	public UsuarioAnalise analistaTecnico;

	@Column(name = "do_processo")
	public String doProcesso;

	@Column(name = "da_analise_tecnica")
	public String daAnaliseTecnica;

	@Column(name = "da_conclusao")
	public String daConclusao;

	@Column(name = "validade_permitida")
	public Integer validadePermitida;

	@OneToMany(mappedBy = "parecerAnalistaTecnico")
	public List<Condicionante> condicionantes;

	@OneToMany(mappedBy = "parecerAnalistaTecnico")
	public List<Restricao> restricoes;

	@Column(name="finalidade_atividade")
	public String finalidadeAtividade;

	@OneToOne(mappedBy = "parecerAnalistaTecnico")
	public Vistoria vistoria;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema="analise", name="rel_documento_parecer_analista_tecnico",
			joinColumns=@JoinColumn(name="id_parecer_analista_tecnico"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;

	@OneToOne
	@JoinColumn(name = "id_documento", referencedColumnName = "id")
	public Documento documentoParecer;

	@OneToOne
	@JoinColumn(name = "id_documento_minuta", referencedColumnName = "id")
	public Documento documentoMinuta;

	private void finalizaParecerDeferido(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioExecutor) {

		if(this.condicionantes != null && !this.condicionantes.isEmpty()) {

			this.condicionantes.forEach(condicionante -> {
				condicionante.parecerAnalistaTecnico = this;
				condicionante._save();
			});

		}

		if(this.restricoes != null && !this.restricoes.isEmpty()) {

			this.restricoes.forEach(restricao -> {
				restricao.parecerAnalistaTecnico = this;
				restricao._save();
			});

		}

		Gerente gerente = Gerente.distribuicaoAutomaticaGerenteAnaliseTecnica(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, analiseTecnica);
		gerente._save();

		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE, usuarioExecutor, UsuarioAnalise.findByGerente(gerente));
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);

	}

	private void finalizaParecerDeferidoSolicitacaoAjuste(AnaliseTecnica analiseTecnica, ParecerAnalistaTecnico parecerAntigo, UsuarioAnalise usuarioExecutor) {

		parecerAntigo.condicionantes = new ArrayList<>();

		if(this.condicionantes != null && !this.condicionantes.isEmpty()) {

			this.condicionantes.forEach(condicionante -> {

				if(condicionante.id != null) {

					parecerAntigo.condicionantes.add(condicionante);

				} else {

					condicionante.parecerAnalistaTecnico = this;
					condicionante._save();
					parecerAntigo.condicionantes.add(condicionante);

				}
			});

		}

		parecerAntigo.restricoes = new ArrayList<>();

		if(this.restricoes != null && !this.restricoes.isEmpty()) {

			this.restricoes.forEach(restricao -> {

				if(restricao.id != null) {

					parecerAntigo.restricoes.add(restricao);

				} else {

					restricao.parecerAnalistaTecnico = this;
					restricao._save();
					parecerAntigo.restricoes.add(restricao);

				}
			});

		}

		Gerente gerente = Gerente.distribuicaoAutomaticaGerenteAnaliseTecnica(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, analiseTecnica);
		gerente._save();

		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE, usuarioExecutor, UsuarioAnalise.findByGerente(gerente));
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);

	}

	private void finalizaParecerIndeferido(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioExecutor) {

		Gerente gerente = Gerente.distribuicaoAutomaticaGerenteAnaliseTecnica(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, analiseTecnica);
		gerente.save();

		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_GERENTE, usuarioExecutor, UsuarioAnalise.findByGerente(gerente));
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);

	}

	private void finalizaParecerEmitirNotificacao(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioExecutor) throws Exception {

		List<Notificacao> notificacoes = this.analiseTecnica.notificacoes;

		notificacoes = notificacoes.stream().filter(notificacao -> notificacao.id == null).collect(Collectors.toList());

		if (notificacoes.size() != 1) {

			throw new ValidacaoException(Mensagem.ERRO_SALVAMENTO_NOTIFICACAO);

		}

		if(this.id != null) {

			analiseTecnica.enviarEmailNotificacao(notificacoes.get(0), this, this.analiseTecnica.documentos);

		} else {

			analiseTecnica.enviarEmailNotificacao(notificacoes.get(0), this.save(), this.analiseTecnica.documentos);

		}

		Analise.alterarStatusLicenca(StatusCaracterizacaoEnum.NOTIFICADO.codigo, analiseTecnica.analise.processo.numero);

		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.NOTIFICAR_PELO_ANALISTA_TECNICO, usuarioExecutor, "Notificado");
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);

	}

	public void finalizarSolicitacaoAjuste(UsuarioAnalise usuarioExecutor) throws Exception {

		ParecerAnalistaTecnico parecerAntigo = ParecerAnalistaTecnico.findById(this.id);

		AnaliseTecnica analiseTecnicaBanco = AnaliseTecnica.findById(parecerAntigo.analiseTecnica.id);

		validarParecer();
		validarTipoResultadoAnalise();

		parecerAntigo.analistaTecnico = this.analistaTecnico = usuarioExecutor;
		parecerAntigo.dataParecer = this.dataParecer = new Date();
		parecerAntigo.doProcesso = this.doProcesso;
		parecerAntigo.daAnaliseTecnica = this.daAnaliseTecnica;
		parecerAntigo.daConclusao = this.daConclusao;
		parecerAntigo.parecer = this.parecer;
		parecerAntigo.validadePermitida = this.validadePermitida;
		parecerAntigo.finalidadeAtividade = this.finalidadeAtividade;

		if(this.documentos != null && !this.documentos.isEmpty()) {
			parecerAntigo.documentos = parecerAntigo.updateDocumentos(this.documentos);
		}

		if(this.vistoria != null) {

			parecerAntigo.vistoria.parecerAnalistaTecnico = parecerAntigo;
			parecerAntigo.vistoria = this.vistoria.salvar();
			analiseTecnicaBanco.vistoria = parecerAntigo.vistoria;

		}

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO)) {

			this.finalizaParecerDeferidoSolicitacaoAjuste(analiseTecnicaBanco, parecerAntigo, usuarioExecutor);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO)) {

			this.finalizaParecerIndeferido(analiseTecnicaBanco, usuarioExecutor);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.EMITIR_NOTIFICACAO)) {

			this.finalizaParecerEmitirNotificacao(analiseTecnicaBanco, usuarioExecutor);

		}

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analiseTecnicaBanco.analise.processo.objetoTramitavel.id);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;
		parecerAntigo._save();

	}

	public void finalizar(UsuarioAnalise usuarioExecutor) throws Exception {

		AnaliseTecnica analiseTecnicaBanco = AnaliseTecnica.findById(this.analiseTecnica.id);

		validarParecer();
		validarTipoResultadoAnalise();

		this.analistaTecnico = usuarioExecutor;
		this.dataParecer = new Date();

		if(this.documentos != null && !this.documentos.isEmpty()) {
			this.updateDocumentos(this.documentos);
		}

		if(this.vistoria != null) {

			this.vistoria.parecerAnalistaTecnico = this;
			this.vistoria = this.vistoria.salvar();
			analiseTecnicaBanco.vistoria = this.vistoria;

		}

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO)) {

			this.finalizaParecerDeferido(analiseTecnicaBanco, usuarioExecutor);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO)) {

			this.finalizaParecerIndeferido(analiseTecnicaBanco, usuarioExecutor);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.EMITIR_NOTIFICACAO)) {

			this.finalizaParecerEmitirNotificacao(analiseTecnicaBanco, usuarioExecutor);
		}

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analiseTecnicaBanco.analise.processo.objetoTramitavel.id);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;
		this._save();

	}

	private List<Documento> updateDocumentos(List<Documento> novosDocumentos) {

		if(this.id != null) {
			this.documentos.forEach(anexoA -> {
				if (novosDocumentos.stream().anyMatch(anexo -> anexoA.id.equals(anexo.id))) {
					anexoA._delete();
				}
			});
		}

		TipoDocumento tipoAutoInfracao = TipoDocumento.findById(TipoDocumento.AUTO_INFRACAO);
		TipoDocumento tipoParecer = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_TECNICA);

		this.documentos = new ArrayList<>();

		for (Documento documento : novosDocumentos) {

			if(documento.id != null) {

				documento = Documento.findById(documento.id);

			} else {

				if(documento.tipo.id.equals(tipoAutoInfracao.id)) {

					documento.tipo = tipoAutoInfracao;

				} else if (documento.tipo.id.equals(tipoParecer.id)) {

					documento.tipo = tipoParecer;

				}

				documento = documento.save();

			}

			this.documentos.add(documento);

		}

		return this.documentos;

	}

	private void validarParecer() {

		if (StringUtils.isBlank(this.parecer))
			throw new ValidacaoException(Mensagem.ANALISE_PARECER_NAO_PREENCHIDO);

	}

	private void validarTipoResultadoAnalise() {

		if (this.tipoResultadoAnalise == null) {
			throw new ValidacaoException(Mensagem.ANALISE_FINAL_PROCESSO_NAO_PREENCHIDA);
		}

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO) && this.parecer.equals("")) {
			throw new ValidacaoException(Mensagem.ANALISE_DESPACHO_NAO_PREENCHIDO);
		}

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO) && this.parecer.equals("")) {
			throw new ValidacaoException(Mensagem.ANALISE_JUSTIFICATIVA_NAO_PREENCHIDA);
		}

	}

	public static ParecerAnalistaTecnico getUltimoParecer(List<ParecerAnalistaTecnico> pareceresAnalistatecnico) {

		return pareceresAnalistatecnico.stream().max(Comparator.comparing(ParecerAnalistaTecnico::getDataParecer)).orElseThrow(ValidationException::new);

	}

	public static Documento gerarPDFMinuta(AnaliseTecnica analiseTecnica, ParecerAnalistaTecnico parecer) throws Exception {

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		Endereco enderecoPrincipal = new Endereco();

		br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(analiseTecnica.analise.processo.empreendimento.getCpfCnpj());
		for (Endereco endereco : empreendimentoEU.enderecos) {
			if (endereco.tipo.id == TipoEndereco.ID_PRINCIPAL) {
				enderecoPrincipal = endereco;
			}
		}

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.DOCUMENTO_MINUTA);

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("analiseTecnica", analiseTecnica)
				.addParam("endereco", enderecoPrincipal)
				.addParam("empreendimento", empreendimentoEU)
				.addParam("parecer", parecer)
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 4.0D, 5.0D);

		pdf.generate();

		return new Documento(tipoDocumento, pdf.getFile(),"minuta.pdf", parecer.analistaTecnico.pessoa.nome, new Date());
	}

	public static ParecerAnalistaTecnico findParecerByProcesso(Processo processo) {

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(processo.idObjetoTramitavel);

		if(historicoTramitacao.idAcao.equals(SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_GERENTE)) {

			return ParecerAnalistaTecnico.getUltimoParecer(processo.analise.analiseTecnica.pareceresAnalistaTecnico);

		}

		return new ParecerAnalistaTecnico();
	
	}

	@Override
	public List<Documento> getDocumentos() {
		return this.documentos;
	}

	@Override
	public List<Documento> getDocumentosParecer() {
		return this.documentos.stream().filter(Documento::isParecerAnaliseTecnica).collect(Collectors.toList());
	}

}
