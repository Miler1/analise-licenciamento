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
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import models.validacaoParecer.Analisavel;
import models.validacaoParecer.ParecerNaoValidadoTecnico;
import models.validacaoParecer.ParecerValidadoTecnico;
import models.validacaoParecer.SolicitarAjustesTecnico;
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
	
	private void validarParecer() {
		
		if(StringUtils.isBlank(this.parecer)) 
			throw new ValidacaoException(Mensagem.ANALISE_PARECER_NAO_PREENCHIDO);
	}	
	
	public static AnaliseTecnica findByProcesso(Processo processo) {
		return AnaliseTecnica.find("analise.processo.id = ? AND ativo = true", processo.id).first();
	}
	
	public AnaliseTecnica save() {
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, Configuracoes.PRAZO_ANALISE_TECNICA);
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
			
			iniciarLicencas();
		}
		
		this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_TECNICA, usuarioExecutor);
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
	
	private void updatePareceresTecnicosRestricoes(List<ParecerTecnicoRestricao> pareceresSalvar) {
		
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

	private void updateLicencasAnalise(List<LicencaAnalise> novasLicencasAnalise) {
		
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

	public static AnaliseTecnica findByNumeroProcesso(String numeroProcesso) {
		
		return AnaliseTecnica.find("analise.processo.numero = ? AND ativo = true", numeroProcesso).first();
	}
	
	public void finalizar(AnaliseTecnica analise, Usuario usuarioExecultor) {
		
		this.update(analise);
		
		validarLicencasAnalise();
		validarParecer();
		validarAnaliseDocumentos();
		validarResultado();						
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());		
		this.dataFim = c.getTime();
		
		this._save();
				
		if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
			
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_TECNICA, usuarioExecultor);
		
		} else if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {
			
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_TECNICA, usuarioExecultor);
		
		} else {
		
			this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.NOTIFICAR, usuarioExecultor);
		}
	}
	
	public void validaParecer(AnaliseTecnica analiseTecnica, Usuario usuarioExecultor) {
		
		TipoResultadoAnaliseChain<AnaliseTecnica> tiposResultadosAnalise = new ParecerValidadoTecnico();		
		tiposResultadosAnalise.setNext(new SolicitarAjustesTecnico());
		tiposResultadosAnalise.setNext(new ParecerNaoValidadoTecnico());
		
		tiposResultadosAnalise.validarParecer(this, analiseTecnica, usuarioExecultor);		
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
			
			if(analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {
				
				throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
			}
			todosDocumentosValidados &= analise.validado;
		}	
		
		if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO && !todosDocumentosValidados) {
			
			throw new ValidacaoException(Mensagem.TODOS_OS_DOCUMENTOS_VALIDOS);
		}
	}

	private void validarAnaliseDocumentos() {

		if(this.analisesDocumentos == null || this.analisesDocumentos.size() == 0)
			throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
			
		for(AnaliseDocumento analise : this.analisesDocumentos) {
			
			if(analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {
				
				throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
			}
		}		
	}

	@Override
	public TipoResultadoAnalise getTipoResultadoValidacao() {
		
		return this.tipoResultadoValidacao;
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

	public AnaliseTecnica gerarCopia() {
		
		AnaliseTecnica copia = new AnaliseTecnica();
		
		copia.analise = this.analise;
		copia.parecer = this.parecer;
		copia.dataVencimentoPrazo = this.dataVencimentoPrazo;
		copia.revisaoSolicitada = true;
		copia.ativo = true;
		copia.analiseTecnicaRevisada = this;
		copia.dataInicio = this.dataInicio;
		copia.tipoResultadoAnalise = this.tipoResultadoAnalise;
		copia.documentos = new ArrayList<>(this.documentos);
		copia.analisesDocumentos = new ArrayList<>();
		
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
		
		return copia;
	}	
}
