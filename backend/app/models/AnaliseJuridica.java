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

import models.portalSeguranca.PerfilUsuario;
import models.portalSeguranca.Setor;
import models.tramitacao.HistoricoTramitacao;
import org.apache.commons.lang.StringUtils;

import exceptions.ValidacaoException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.StatusCaracterizacao;
import models.licenciamento.TipoAnalise;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import models.validacaoParecer.Analisavel;
import models.validacaoParecer.SolicitarAjustesJuridicoAprovador;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.libs.Crypto;
import utils.Configuracoes;
import utils.ListUtil;
import utils.Mensagem;
import utils.ModelUtil;

import models.pdf.PDFGenerator;

@Entity
@Table(schema="analise", name="analise_juridica")
public class AnaliseJuridica extends GenericModel implements Analisavel {

	public static final String SEQ = "analise.analise_juridica_id_seq";
	
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
	@JoinColumn(name="id_analise_juridica_revisada", referencedColumnName="id")
	public AnaliseJuridica analiseJuridicaRevisada;
	
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
	@JoinTable(schema="analise", name="rel_documento_analise_juridica", 
		joinColumns=@JoinColumn(name="id_analise_juridica"), 
		inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;
	
	@OneToMany(mappedBy="analiseJuridica", cascade=CascadeType.ALL)
	public List<AnaliseDocumento> analisesDocumentos;
	
	@OneToMany(mappedBy="analiseJuridica", cascade=CascadeType.ALL)
	public List<ConsultorJuridico> consultoresJuridicos;
	
	@Column(name="parecer_validacao")
	public String parecerValidacao;
	
 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "id_usuario_validacao", referencedColumnName = "id")
	public Usuario usuarioValidacao;
	
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
	
	private void validarResultado() {
		
		if(this.tipoResultadoAnalise == null)
			throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO);
		
		boolean todosDocumentosValidados = true;
		for(AnaliseDocumento analise : this.analisesDocumentos) {
			
			if(analise.documento.tipo.tipoAnalise.equals(TipoAnalise.JURIDICA)) {
				
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
						
			if(analise.documento.tipo.tipoAnalise.equals(TipoAnalise.JURIDICA)) {
				
				if(analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {
					
					throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
				}
			}					 
		}
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
	
	private void updateDocumentos(List<Documento> novosDocumentos) {
		
		TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_ANALISE_JURIDICA);
		
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
				List<AnaliseJuridica> analiseJuridicasRelacionadas = docCadastrado.getAnaliseJuridicasRelacionadas();
				if(analiseJuridicasRelacionadas.size() == 0) {
					
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
		
	public AnaliseJuridica save() {
		
		Calendar c = Calendar.getInstance();
		c.setTime(this.analise.dataCadastro);
		c.add(Calendar.DAY_OF_MONTH, Configuracoes.PRAZO_ANALISE_JURIDICA);
		this.dataVencimentoPrazo = c.getTime();
			
		this.ativo = true;
		
		return super.save();
	}
	
	public void iniciar(Usuario usuarioExecutor) {
		
		if(this.dataInicio == null) {
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			
			this.dataInicio = c.getTime();
			
			this._save();
			
		}
		
		this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_JURIDICA, usuarioExecutor);
		Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
	}
	
	public void update(AnaliseJuridica novaAnalise) {
		
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
				
				novaAnaliseDocumento.analiseJuridica = this;
				this.analisesDocumentos.add(novaAnaliseDocumento);
			}			
		}
		
		this._save();
	}
	
	public void finalizar(AnaliseJuridica analise, Usuario usuarioExecutor) {
					
		this.update(analise);
		
		validarParecer();
		validarAnaliseDocumentos();
		validarResultado();						
				
		this._save();
		this.refresh();
				
		if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
			
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_JURIDICA, usuarioExecutor);
			Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
		
		} else if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {
			
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_JURIDICA, usuarioExecutor);
			Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
		
		} else {
			
			Notificacao.criarNotificacoesAnaliseJuridica(analise);
					
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.NOTIFICAR, usuarioExecutor);

			HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id);

			List<Notificacao> notificacoes = Notificacao.find("analiseJuridica.id", this.id).fetch();
			Notificacao.setHistoricoAlteracoes(notificacoes, historicoTramitacao);

			Setor.setHistoricoTramitacao(historicoTramitacao, usuarioExecutor);

			enviarEmailNotificacao();
		}				
	}
	
	public void enviarEmailNotificacao() {
				
		List<String> destinatarios = new ArrayList<String>();
		destinatarios.addAll(this.analise.processo.empreendimento.emailsProprietarios());
		destinatarios.addAll(this.analise.processo.empreendimento.emailsResponsaveis());
				
		EmailNotificacaoAnaliseJuridica notificacao = new EmailNotificacaoAnaliseJuridica(this, destinatarios);
		notificacao.enviar();				
	}
	
	public static AnaliseJuridica findByNumeroProcesso(String numeroProcesso) {
		return AnaliseJuridica.find("analise.processo.numero = ? AND ativo = true", numeroProcesso).first();
	}	
		
	public static AnaliseJuridica findByProcesso(Processo processo) {
		return AnaliseJuridica.find("analise.processo.id = ? AND ativo = true", processo.id).first();
	}
	
	public static List<AnaliseDocumento> findDocumentos(Long idAnaliseJuridica) {
		
		return AnaliseDocumento.find("analiseJuridica.id = ? ", idAnaliseJuridica).fetch();
	}
	
	public void validaParecer(AnaliseJuridica analiseJuridica, Usuario usuarioExecultor) {
		
		TipoResultadoAnaliseChain tiposResultadosAnalise = new ParecerValidado();
		tiposResultadosAnalise.setNext(new SolicitarAjustes());
		tiposResultadosAnalise.setNext(new ParecerNaoValidado());
		
		tiposResultadosAnalise.validarParecer(analiseJuridica, usuarioExecultor);		
	}
	
	public AnaliseJuridica gerarCopia(){
		
		AnaliseJuridica copia = new AnaliseJuridica();
		
		copia.analise = this.analise;
		copia.parecer = this.parecer;
		copia.dataVencimentoPrazo = this.dataVencimentoPrazo;
		copia.revisaoSolicitada = true;
		copia.ativo = true;
		copia.analiseJuridicaRevisada = this;
		copia.dataInicio = this.dataInicio;
		copia.tipoResultadoAnalise = this.tipoResultadoAnalise;
		copia.documentos = new ArrayList<>(this.documentos);
		copia.analisesDocumentos = new ArrayList<>();
		copia.usuarioValidacao = this.usuarioValidacao;
		copia.usuarioValidacaoAprovador = this.usuarioValidacaoAprovador;
		
		for (AnaliseDocumento analiseDocumento: this.analisesDocumentos) {
			
			AnaliseDocumento copiaAnaliseDoc = analiseDocumento.gerarCopia();
			copiaAnaliseDoc.analiseJuridica = copia;
			copia.analisesDocumentos.add(copiaAnaliseDoc);
		}
		
		copia.consultoresJuridicos = new ArrayList<>();
		
		for (ConsultorJuridico consultorJuridico: this.consultoresJuridicos) {
			
			ConsultorJuridico copiaConsultorJur = consultorJuridico.gerarCopia();
			
			copiaConsultorJur.analiseJuridica = copia;
			copia.consultoresJuridicos.add(copiaConsultorJur);
		}		
		
		return copia;
	}
	
	public void setDataFim(Date data) {

		this.dataFim = data;		
	}
	
	private abstract class TipoResultadoAnaliseChain {
		
		protected TipoResultadoAnaliseChain next;
		protected Long idResultadoAnalise;
		
		public TipoResultadoAnaliseChain(Long idResultadoAnalise){
			
			this.next = null;
			this.idResultadoAnalise = idResultadoAnalise;			
		}
		
		public void setNext(TipoResultadoAnaliseChain tipoResultadoAnalise) {
	        if (next == null) {
	            next = tipoResultadoAnalise;
	        } else {
	            next.setNext(tipoResultadoAnalise);
	        }
	    }
		
		private void setAnaliseJuridica(AnaliseJuridica novaAnaliseJuridica, Usuario usuarioExecutor) {
			
			tipoResultadoValidacao = novaAnaliseJuridica.tipoResultadoValidacao;
			parecerValidacao = novaAnaliseJuridica.parecerValidacao;
		}
		
		public void validarParecer(AnaliseJuridica novaAnaliseJuridica, Usuario usuarioExecutor) {
			
			if (novaAnaliseJuridica.tipoResultadoValidacao.id.equals(idResultadoAnalise)) {
				
				setAnaliseJuridica(novaAnaliseJuridica, usuarioExecutor);				
				validaParecer(novaAnaliseJuridica, usuarioExecutor);
				
			} else if (next != null) {
				
				next.validarParecer(novaAnaliseJuridica, usuarioExecutor);
			}
		}
		
		protected abstract void validaParecer(AnaliseJuridica novaAnaliseJuridica, Usuario usuarioExecutor);
	}
	
	private class ParecerValidado extends AnaliseJuridica.TipoResultadoAnaliseChain {
		
		public ParecerValidado() {
			super(TipoResultadoAnalise.PARECER_VALIDADO);
		}

		@Override
		protected void validaParecer(AnaliseJuridica novaAnaliseJuridica, Usuario usuarioExecutor) {
			
			validarTipoResultadoValidacao();
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());					
			setDataFim(c.getTime());
			
			_save();
			
			if (tipoResultadoAnalise.id == tipoResultadoAnalise.INDEFERIDO) {
				
				List<Long> idsCaracterizacoes = ListUtil.getIds(analise.processo.caracterizacoes);
				
				Caracterizacao.setStatusCaracterizacao(idsCaracterizacoes, StatusCaracterizacao.ARQUIVADO);
				
				analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_JURIDICO, usuarioExecutor);

				Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analise.processo.objetoTramitavel.id), usuarioExecutor);
				
				return;
			}
			
			if (tipoResultadoAnalise.id == tipoResultadoAnalise.DEFERIDO) {
				
				/**
				 * Se tiver usuário aprovador, então volta o processo diretamente para o aguardando assinatura aprovador,
				 * pois foi uma solicitação de ajuste do aprovador para o coordenador.
				 */
				if (usuarioValidacaoAprovador != null) {
					
					analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.DEFERIR_ANALISE_JURIDICA_COORDENADOR_APROVADOR, usuarioExecutor);
					Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analise.processo.objetoTramitavel.id), usuarioExecutor);

				} else {
					
					AnaliseTecnica analiseTecnica = new AnaliseTecnica();
					analiseTecnica.analise = AnaliseJuridica.this.analise;
					
					analiseTecnica.save();
					
					analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_JURIDICO, usuarioExecutor);
					Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analise.processo.objetoTramitavel.id), usuarioExecutor);
				}
				
			}
		}
	}
	
	private class SolicitarAjustes extends AnaliseJuridica.TipoResultadoAnaliseChain {
		
		public SolicitarAjustes() {
			super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
		}	

		@Override
		protected void validaParecer(AnaliseJuridica novaAnaliseJuridica, Usuario usuarioExecutor) {
			
			validarAnaliseJuridica();
			
			ativo = false;
			
			_save();
			
			AnaliseJuridica copia = gerarCopia();
			
			copia._save();
			
			analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_JURIDICO, usuarioExecutor);
			Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analise.processo.objetoTramitavel.id), usuarioExecutor);
		}

		private void validarAnaliseJuridica() {
			
			validarTipoResultadoValidacao();
			
			validarParecerValidacao();
		}
	}
	
	private class ParecerNaoValidado extends TipoResultadoAnaliseChain{
		
		public ParecerNaoValidado() {
			super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
		}	

		@Override
		protected void validaParecer(AnaliseJuridica novaAnaliseJuridica, Usuario usuarioExecutor) {
			
			validarAnaliseJuridica(novaAnaliseJuridica);
			
			ativo = false;
			
			_save();
						
			criarNovaAnalise(novaAnaliseJuridica.consultoresJuridicos.get(0).usuario, usuarioExecutor);
			
			analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.INVALIDAR_PARECER_JURIDICO, usuarioExecutor);
			Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analise.processo.objetoTramitavel.id), usuarioExecutor);
		}

		private void criarNovaAnalise(Usuario usuarioConsultor, Usuario usuarioValidacao) {
			
			AnaliseJuridica novaAnalise = new AnaliseJuridica();
			
			novaAnalise.analise = analise;
			novaAnalise.dataVencimentoPrazo = dataVencimentoPrazo;
			novaAnalise.revisaoSolicitada = true;
			novaAnalise.ativo = true;
			novaAnalise.usuarioValidacao = usuarioValidacao;
			novaAnalise.usuarioValidacaoAprovador = usuarioValidacaoAprovador;
			
			novaAnalise.consultoresJuridicos = new ArrayList<>();
			ConsultorJuridico consultorJuridico = new ConsultorJuridico(novaAnalise, usuarioConsultor);
			novaAnalise.consultoresJuridicos.add(consultorJuridico);
			
			novaAnalise._save();
		}

		private void validarAnaliseJuridica(AnaliseJuridica novaAnaliseJuridica) {
			
			validarTipoResultadoValidacao();
			
			validarParecerValidacao();
			
			if (novaAnaliseJuridica.consultoresJuridicos == null || novaAnaliseJuridica.consultoresJuridicos.isEmpty()) {
				
				throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO);
			}
			
		}
	}

	@Override
	public TipoResultadoAnalise getTipoResultadoValidacao() {
		
		return this.tipoResultadoValidacao;
	}

	public void setValidacaoCoordenador(AnaliseJuridica analiseJuridica) {
		this.tipoResultadoValidacao = analiseJuridica.tipoResultadoValidacao;
		this.parecerValidacao = analiseJuridica.parecerValidacao;
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
	
	public void validarParecerValidacaoAprovador(AnaliseJuridica analiseJuridica, Usuario usuarioExecutor) {
		
		models.validacaoParecer.TipoResultadoAnaliseChain<AnaliseJuridica> tiposResultadosAnalise = new SolicitarAjustesJuridicoAprovador();	
		tiposResultadosAnalise.validarParecer(this, analiseJuridica, usuarioExecutor);		
	}

	public Documento gerarPDFParecer() throws Exception {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_JURIDICA);

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("analiseEspecifica", this)
				.addParam("analiseArea", "ANALISE_JURIDICA")
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 1.5D);

		pdf.generate();

		Documento documento = new Documento(tipoDocumento, pdf.getFile());

		return documento;

	}
}
