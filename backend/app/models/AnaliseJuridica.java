package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.ListUtil;
import utils.Mensagem;
import utils.ModelUtil;

@Entity
@Table(schema="analise", name="analise_juridica")
public class AnaliseJuridica extends GenericModel {

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
	
	@OneToMany(mappedBy="analiseJuridica")
	public List<AnaliseDocumento> analisesDocumentos;
	
	@OneToMany(mappedBy="analiseJuridica")
	public List<ConsultorJuridico> consultoresJuridicos;
	
	@Column(name="parecer_validacao")
	public String parecerValidacao;
	
	private void validarParecer() {
		
		if(StringUtils.isBlank(this.parecer)) 
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_PARECER_NAO_PREENCHIDO);
	}
	
	private void validarResultado() {
		
		if(this.tipoResultadoAnalise == null)
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_SEM_RESULTADO);
		
		boolean todosDocumentosValidados = true;
		for(AnaliseDocumento analise : this.analisesDocumentos) {
			
			if(analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {
				
				throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_DOCUMENTO_NAO_AVALIADO);
			}
			todosDocumentosValidados &= analise.validado;
		}	
		
		if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO && !todosDocumentosValidados) {
			
			throw new ValidacaoException(Mensagem.TODOS_OS_DOCUMENTOS_VALIDOS);
		}		
	}
	
	private void validarAnaliseDocumentos() {
		
		if(this.analisesDocumentos == null || this.analisesDocumentos.size() == 0)
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_DOCUMENTO_NAO_AVALIADO);
			
		for(AnaliseDocumento analise : this.analisesDocumentos) {
			
			if(analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {
				
				throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_DOCUMENTO_NAO_AVALIADO);
			}
		}
	}
	
	private void validarTipoResultadoValidacao() {
		
		if (tipoResultadoValidacao == null) {
			
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_SEM_RESULTADO_VALIDACAO);
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
				documentosDeletar.add(docCadastrado);
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
		c.setTime(new Date());
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
			
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_JURIDICA, usuarioExecutor);
		}		
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
		
		this._save();
		
		this.analisesDocumentos = novaAnalise.analisesDocumentos;
		
		for(AnaliseDocumento analiseDocumento : this.analisesDocumentos) {
			
			if(analiseDocumento.id != null) {
				
				analiseDocumento.update(analiseDocumento);
			
			} else {
				
				analiseDocumento.analiseJuridica = this;
				analiseDocumento.save();
			}			
		}		
	}
	
	public void finalizar(AnaliseJuridica analise, Usuario usuarioExecultor) {
					
		this.update(analise);
		
		validarParecer();
		validarAnaliseDocumentos();
		validarResultado();						
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());		
		this.dataFim = c.getTime();
		
		this.save();
				
		if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
			
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_JURIDICA, usuarioExecultor);
		
		} else if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {
			
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_JURIDICA, usuarioExecultor);
		
		} else {
		
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.NOTIFICAR, usuarioExecultor);
		}				
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
	
	public void validaParecer(AnaliseJuridica analiseJuridica) {
		
		TipoResultadoAnaliseChain tiposResultadosAnalise = new ParecerValidado();
		tiposResultadosAnalise.setNext(new SolicitarAjustes());
		tiposResultadosAnalise.setNext(new ParecerNaoValidado());
		
		tiposResultadosAnalise.validarParecer(analiseJuridica);		
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
		copia.documentos = new ArrayList<>(this.documentos);
		copia.analisesDocumentos = new ArrayList<>();
		
		for (AnaliseDocumento analiseDocumento: this.analisesDocumentos) {
			
			AnaliseDocumento copiaAnaliseDoc = analiseDocumento.gerarCopia();
			copiaAnaliseDoc.analiseJuridica = copia;
			copia.analisesDocumentos.add(copiaAnaliseDoc);
		}
		
		copia.consultoresJuridicos = new ArrayList<>(this.consultoresJuridicos);
		
		for (ConsultorJuridico consultorJuridico: this.consultoresJuridicos) {
			
			ConsultorJuridico copiaConsultorJur = consultorJuridico.gerarCopia();
			
			copiaConsultorJur.analiseJuridica = copia;
			copia.consultoresJuridicos.add(copiaConsultorJur);
		}		
		
		return copia;
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
		
		private void setAnaliseJuridica(AnaliseJuridica novaAnaliseJuridica) {
			
			tipoResultadoValidacao = novaAnaliseJuridica.tipoResultadoValidacao;
			parecerValidacao = novaAnaliseJuridica.parecerValidacao;			
		}
		
		public void validarParecer(AnaliseJuridica novaAnaliseJuridica) {
			
			if (novaAnaliseJuridica.tipoResultadoValidacao.id.equals(idResultadoAnalise)) {
				
				setAnaliseJuridica(novaAnaliseJuridica);				
				validaParecer(novaAnaliseJuridica);
				
			} else if (next != null) {
				
				next.validarParecer(novaAnaliseJuridica);
			}
		}
		
		protected abstract void validaParecer(AnaliseJuridica novaAnaliseJuridica);
	}
	
	private class ParecerValidado extends TipoResultadoAnaliseChain{
		
		public ParecerValidado() {
			super(TipoResultadoAnalise.PARECER_VALIDADO);
		}

		@Override
		protected void validaParecer(AnaliseJuridica novaAnaliseJuridica) {
			
			validarTipoResultadoValidacao();
			
			_save();
			
			//TODO Tramitar e criar a análise técnica
		}
	}
	
	private class SolicitarAjustes extends TipoResultadoAnaliseChain{
		
		public SolicitarAjustes() {
			super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
		}	

		@Override
		protected void validaParecer(AnaliseJuridica novaAnaliseJuridica) {
			
			validarTipoResultadoValidacao();
			
			_save();
			
			AnaliseJuridica copia = gerarCopia();
			
			copia._save();
			
			//TODO tramitar
		}
	}
	
	private class ParecerNaoValidado extends TipoResultadoAnaliseChain{
		
		public ParecerNaoValidado() {
			super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
		}	

		@Override
		protected void validaParecer(AnaliseJuridica novaAnaliseJuridica) {
			
			validarAnaliseJuridica(novaAnaliseJuridica);
			
			_save();
			
			AnaliseJuridica novaAnalise = new AnaliseJuridica();
			
			criarNovaAnalise(novaAnalise);
			
			//TODO tramitar
		}

		private void criarNovaAnalise(AnaliseJuridica novaAnalise) {
			
			novaAnalise.analise = analise;
			novaAnalise.dataVencimentoPrazo = dataVencimentoPrazo;
			novaAnalise.ativo = true;
			_save();
		}

		private void validarAnaliseJuridica(AnaliseJuridica novaAnaliseJuridica) {
			
			validarTipoResultadoValidacao();
			
			if (novaAnaliseJuridica.consultoresJuridicos == null || novaAnaliseJuridica.consultoresJuridicos.isEmpty()) {
				
				throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO);
			}
			
		}
	}	
}
