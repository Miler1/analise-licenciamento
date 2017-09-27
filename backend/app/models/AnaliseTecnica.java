package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import exceptions.ValidacaoException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import models.validacaoParecer.Analisavel;
import models.validacaoParecer.ParecerNaoValidadoTecnico;
import models.validacaoParecer.ParecerNaoValidadoTecnicoGerente;
import models.validacaoParecer.ParecerValidadoTecnico;
import models.validacaoParecer.ParecerValidadoTecnicoGerente;
import models.validacaoParecer.SolicitarAjustesTecnico;
import models.validacaoParecer.SolicitarAjustesTecnicoAprovador;
import models.validacaoParecer.SolicitarAjustesTecnicoGerente;
import models.validacaoParecer.TipoResultadoAnaliseChain;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.ListUtil;
import utils.Mensagem;
import utils.ModelUtil;

@Entity
@Table(schema="analise", name="analise_tecnica")
public class AnaliseTecnica extends GenericModel implements Analisavel {

	public static final String SEQ = "analise.analise_tecnica_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name="id_analise")
	public Analise analise;
	
	public String parecer;
	
	@Required
	@Column(name="data_vencimento_prazo")
	public Date dataVencimentoPrazo;
	
	@Required
	@Column(name="revisao_solicitada")
	public Boolean revisaoSolicitada;
	
	public Boolean ativo;
	
	@OneToOne
	@JoinColumn(name="id_analise_tecnica_revisada", referencedColumnName="id")
	public AnaliseTecnica analiseTecnicaRevisada;
	
	@Column(name="data_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataInicio;
	
	@Column(name="data_fim")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFim;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_validacao")
	public TipoResultadoAnalise tipoResultadoValidacao;	
	
	@ManyToMany
	@JoinTable(schema="analise", name="rel_documento_analise_tecnica", 
		joinColumns=@JoinColumn(name="id_analise_tecnica"), 
		inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;
	
	@OneToMany(mappedBy="analiseTecnica", cascade=CascadeType.ALL)
	public List<AnaliseDocumento> analisesDocumentos;
	
	@OneToMany(mappedBy="analiseTecnica", cascade=CascadeType.ALL)
	public List<AnalistaTecnico> analistasTecnicos;
	
	@Column(name="parecer_validacao")
	public String parecerValidacao;
	
 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao", referencedColumnName = "id")
	public Usuario usuarioValidacao;
	
	@OneToMany(mappedBy="analiseTecnica", orphanRemoval = true)
	public List<LicencaAnalise> licencasAnalise;
	
	@OneToMany(mappedBy = "analiseTecnica", orphanRemoval = true)
	public List<ParecerTecnicoRestricao> pareceresTecnicosRestricoes;
	
	@Column(name="justificativa_coordenador")
	public String justificativaCoordenador;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_validacao_gerente")
	public TipoResultadoAnalise tipoResultadoValidacaoGerente;
	
	@Column(name="parecer_validacao_gerente")
	public String parecerValidacaoGerente;
	
 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao_gerente", referencedColumnName = "id")
	public Usuario usuarioValidacaoGerente;	
 	
	@OneToMany(mappedBy="analiseTecnica", cascade=CascadeType.ALL)
	public List<GerenteTecnico> gerentesTecnicos;
	
	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_resultado_validacao_aprovador")
	public TipoResultadoAnalise tipoResultadoValidacaoAprovador;
	
	@Column(name="parecer_validacao_aprovador")
	public String parecerValidacaoAprovador;
	
 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao_aprovador", referencedColumnName = "id")
	public Usuario usuarioValidacaoAprovador;		
		
	private void validarParecer() {
		
		if(StringUtils.isBlank(this.parecer)) 
			throw new ValidacaoException(Mensagem.ANALISE_PARECER_NAO_PREENCHIDO);
	}	
	
	public static AnaliseTecnica findByProcesso(Processo processo) {
		return AnaliseTecnica.find("analise.processo.id = ? AND ativo = true", processo.id).first();
	}
	
	public AnaliseTecnica save() {
		
		if (this.dataCadastro == null) {
			
			this.dataCadastro = new Date();
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(this.dataCadastro);
		c.add(Calendar.DAY_OF_MONTH, Configuracoes.PRAZO_ANALISE_TECNICA);
		this.dataVencimentoPrazo = c.getTime();
		
		DiasAnalise diasAnalise = DiasAnalise.find("analise.id", this.analise.id).first();
		
			
		this.ativo = true;
		
		return super.save();
	}
	
	public void iniciar(Usuario usuarioExecutor) {
		
		if(this.dataInicio == null) {
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			
			this.dataInicio = c.getTime();
									
			this._save();
			
			iniciarLicencas();
		}
		
		this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_TECNICA, usuarioExecutor);
	}	

	public Boolean validarEmissaoLicencas(List<LicencaAnalise> licencas) {
		for(int i = 0; i < licencas.size() ; i++ ) {
			LicencaAnalise licencaVerificar = licencas.get(i);
			if (licencaVerificar.emitir) {
				return true;
			}
		}
		throw new ValidacaoException(Mensagem.ERRO_NENHUMA_LICENCA_EMITIDA);
	}
	
	private void iniciarLicencas() {
		
		List<LicencaAnalise> novasLicencasAnalise = new ArrayList<>();
		
		for (Caracterizacao caracterizacao : this.analise.processo.caracterizacoes) {
			
			LicencaAnalise novaLicencaAnalise = new LicencaAnalise();
			novaLicencaAnalise.caracterizacao = caracterizacao;
			novaLicencaAnalise.validade = caracterizacao.tipoLicenca.validadeEmAnos;
			
			novasLicencasAnalise.add(novaLicencaAnalise);
		}
		
		updateLicencasAnalise(novasLicencasAnalise);
	}

	public void update(AnaliseTecnica novaAnalise) {
				
		if(this.dataFim != null) {
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_CONCLUIDA);
		}
			
		this.parecer = novaAnalise.parecer;
		
		if(novaAnalise.tipoResultadoAnalise != null &&
				novaAnalise.tipoResultadoAnalise.id != null) {
			
			this.tipoResultadoAnalise = novaAnalise.tipoResultadoAnalise;		
		}
		
		updateDocumentos(novaAnalise.documentos);
		
		if (this.analisesDocumentos == null) {
			
			this.analisesDocumentos = new ArrayList<>();
		}
		
		for(AnaliseDocumento novaAnaliseDocumento : novaAnalise.analisesDocumentos) {
						
			AnaliseDocumento analiseDocumento = ListUtil.getById(novaAnaliseDocumento.id, this.analisesDocumentos);
				
			if(analiseDocumento != null) {
				
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
		
		for(LicencaAnalise novaLicencaAnalise : novasLicencasAnalise) {
						
			LicencaAnalise licencaAnalise = ListUtil.getById(novaLicencaAnalise.id, this.licencasAnalise);
				
			if(licencaAnalise != null) {
				
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
				if(analisesTecnicasRelacionadas.size() == 0) {
				
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
		
		return AnaliseTecnica.find("analise.processo.numero = ? AND ativo = true", numeroProcesso).first();
	}
	
	public void finalizar(AnaliseTecnica analise, Usuario usuarioExecutor) {
		
		this.update(analise);
		
		validarLicencasAnalise();
		validarParecer();
		validarAnaliseDocumentos();
		validarResultado();						
		validarEmissaoLicencas(this.licencasAnalise);
		this._save();
				
		if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
			
			if(this.usuarioValidacaoGerente != null)
				this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE, usuarioExecutor);
			else
				this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_COORDENADOR, usuarioExecutor);
		
		} else if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {
			
			if(this.usuarioValidacaoGerente != null)
				this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_GERENTE, usuarioExecutor);
			else
				this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_COORDENADOR, usuarioExecutor);
		
		} else {
		
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.NOTIFICAR, usuarioExecutor);
			enviarEmailNotificacao();
		}
	}
	
	public void enviarEmailNotificacao() {
		
		List<String> destinatarios = new ArrayList<String>();
		destinatarios.addAll(this.analise.processo.empreendimento.emailsProprietarios());
		destinatarios.addAll(this.analise.processo.empreendimento.emailsResponsaveis());
				
		EmailNotificacaoAnaliseTecnica notificacao = new EmailNotificacaoAnaliseTecnica(this, destinatarios);
		notificacao.enviar();				
	}	
	
	public void validaParecer(AnaliseTecnica analiseTecnica, Usuario usuarioExecutor) {
		
		TipoResultadoAnaliseChain<AnaliseTecnica> tiposResultadosAnalise = new ParecerValidadoTecnico();		
		tiposResultadosAnalise.setNext(new SolicitarAjustesTecnico());
		tiposResultadosAnalise.setNext(new ParecerNaoValidadoTecnico());
		
		tiposResultadosAnalise.validarParecer(this, analiseTecnica, usuarioExecutor);		
	}
	
	public void validaParecerGerente(AnaliseTecnica analiseTecnica, Usuario usuarioExecutor) {
		
		TipoResultadoAnaliseChain<AnaliseTecnica> tiposResultadosAnalise = new ParecerValidadoTecnicoGerente();		
		tiposResultadosAnalise.setNext(new SolicitarAjustesTecnicoGerente());
		tiposResultadosAnalise.setNext(new ParecerNaoValidadoTecnicoGerente());
		
		tiposResultadosAnalise.validarParecer(this, analiseTecnica, usuarioExecutor);		
	}
	
	public void validarParecerValidacaoAprovador(AnaliseTecnica analiseTecnica, Usuario usuarioExecutor) {
		
		TipoResultadoAnaliseChain<AnaliseTecnica> tiposResultadosAnalise = new SolicitarAjustesTecnicoAprovador();	
		tiposResultadosAnalise.validarParecer(this, analiseTecnica, usuarioExecutor);		
	}	

	private void validarLicencasAnalise() {
		
		if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
						
			for (LicencaAnalise licencaAnalise : this.licencasAnalise) {
				
				if (licencaAnalise.emitir == null) {
					
					throw new ValidacaoException(Mensagem.ANALISE_TECNICA_LICENCA_SEM_VALIDACAO);
				}
			}
		}
	}

	private void validarResultado() {
		
		if(this.tipoResultadoAnalise == null)
			throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO);
		
		boolean todosDocumentosValidados = true;
		for(AnaliseDocumento analise : this.analisesDocumentos) {
			
			if(analise.documento.tipo.tipoAnalise.equals(TipoAnalise.TECNICA)) {
				
				if(analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {
					
					throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
				}
				todosDocumentosValidados &= analise.validado;
			}										
		}	
		
		if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO && !todosDocumentosValidados) {
			
			throw new ValidacaoException(Mensagem.TODOS_OS_DOCUMENTOS_VALIDOS);
		}
	}

	private void validarAnaliseDocumentos() {

		if(this.analisesDocumentos == null || this.analisesDocumentos.size() == 0)
			throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
			
		for(AnaliseDocumento analise : this.analisesDocumentos) {
			
			if(analise.documento.tipo.tipoAnalise.equals(TipoAnalise.TECNICA) &&
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

	public AnaliseTecnica gerarCopia() {
		
		AnaliseTecnica copia = new AnaliseTecnica();
		
		copia.analise = this.analise;
		copia.parecer = this.parecer;
		copia.dataCadastro = this.dataCadastro;
		copia.dataVencimentoPrazo = this.dataVencimentoPrazo;
		copia.revisaoSolicitada = true;
		copia.ativo = true;
		copia.analiseTecnicaRevisada = this;
		copia.dataInicio = this.dataInicio;
		copia.tipoResultadoAnalise = this.tipoResultadoAnalise;
		copia.documentos = new ArrayList<>(this.documentos);
		copia.analisesDocumentos = new ArrayList<>();
		copia.usuarioValidacao = this.usuarioValidacao;
		copia.usuarioValidacaoGerente = this.usuarioValidacaoGerente;
		
		for (AnaliseDocumento analiseDocumento: this.analisesDocumentos) {
			
			AnaliseDocumento copiaAnaliseDoc = analiseDocumento.gerarCopia();
			copiaAnaliseDoc.analiseTecnica = copia;
			copia.analisesDocumentos.add(copiaAnaliseDoc);
		}
		
		copia.analistasTecnicos = new ArrayList<>();
		
		for (AnalistaTecnico analistaTecnico: this.analistasTecnicos) {
			
			AnalistaTecnico copiaAnalistaTec = analistaTecnico.gerarCopia();
			
			copiaAnalistaTec.analiseTecnica = copia;
			copia.analistasTecnicos.add(copiaAnalistaTec);
		}
		
		copia.gerentesTecnicos = new ArrayList<>();
		
		for (GerenteTecnico gerenteTecnico: this.gerentesTecnicos) {
			
			GerenteTecnico copiaGerenteTec = gerenteTecnico.gerarCopia();
			
			copiaGerenteTec.analiseTecnica = copia;
			copia.gerentesTecnicos.add(copiaGerenteTec);
		}		
		
		copia.licencasAnalise = new ArrayList<>();
		for (LicencaAnalise licenca : this.licencasAnalise) {
			
			LicencaAnalise copiaLicencaAnalise = licenca.gerarCopia();
			
			copiaLicencaAnalise.analiseTecnica = copia;
			copia.licencasAnalise.add(copiaLicencaAnalise);				
		}
		
		copia.pareceresTecnicosRestricoes = new ArrayList<>();
		for (ParecerTecnicoRestricao parecerTecnicoRestricao : this.pareceresTecnicosRestricoes) {
			
			ParecerTecnicoRestricao copiaParecerTecnicoRestricao = parecerTecnicoRestricao.gerarCopia();
			
			copiaParecerTecnicoRestricao.analiseTecnica = copia;
			copia.pareceresTecnicosRestricoes.add(copiaParecerTecnicoRestricao);				
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
	
	public AnalistaTecnico getAnalistaTecnico() {
		
		return this.analistasTecnicos.get(0);
	}
	
	public boolean hasGerentes() {
		
		return this.gerentesTecnicos != null && this.gerentesTecnicos.size() > 0;
	}
	
	public GerenteTecnico getGerenteTecnico() {
		
		return this.gerentesTecnicos.get(0);
	}
}
