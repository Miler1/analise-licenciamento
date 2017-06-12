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
	@JoinColumn(name="id_tipo_resultado")
	public TipoResultadoAnalise tipoResultadoAnalise;
	
	@ManyToMany
	@JoinTable(schema="analise", name="rel_documento_analise_juridica", 
		joinColumns=@JoinColumn(name="id_analise_juridica"), 
		inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;
	
	@OneToMany(mappedBy="analiseJuridica")
	public List<AnaliseDocumento> analisesDocumentos;
	
	@OneToMany(mappedBy="analiseJuridica")
	public List<ConsultorJuridico> consultoresJuridicos;
	
	private void validarParecer() {
		
		if(StringUtils.isBlank(this.parecer)) 
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_PARECER_NAO_PREENCHIDO);
	}
	
	private void validarResultado() {
		
		if(this.tipoResultadoAnalise == null)
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_SEM_RESULTADO);
	}
	
	private void validarAnaliseDocumentos() {
		
		if(this.analisesDocumentos == null || this.analisesDocumentos.size() == 0)
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_DOCUMENTO_NAO_AVALIADO);
			
		for(AnaliseDocumento analise : this.analisesDocumentos) {
			
			if(analise.validado == null || (!analise.validado || StringUtils.isBlank(analise.parecer))) {
				
				throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_DOCUMENTO_NAO_AVALIADO);
			}
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
	
	public void update(AnaliseJuridica novaAnalise) {
		
		//this.analisesDocumentos = this.analisesDocumentos;
		this.parecer = novaAnalise.parecer;
		this.tipoResultadoAnalise = novaAnalise.tipoResultadoAnalise;
		
		updateDocumentos(novaAnalise.documentos);		
		
		this._save();
	}
	
	public void finalizar() {
		
		validarParecer();
		validarAnaliseDocumentos();
		validarResultado();
		
		AnaliseJuridica analiseAlterada = AnaliseJuridica.findById(this.id);
		
		analiseAlterada.documentos = this.documentos;
		analiseAlterada.analisesDocumentos = this.analisesDocumentos;
		analiseAlterada.parecer = this.parecer;
		analiseAlterada.tipoResultadoAnalise = this.tipoResultadoAnalise;			
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());		
		analiseAlterada.dataFim = c.getTime();
		
		//TODO tramitar		
		
		analiseAlterada._save();
	}
		
	public static AnaliseJuridica findByProcesso(Processo processo) {
		return AnaliseJuridica.find("analise.processo.id = ? AND ativo = true", processo.id).first();
	}
	
	public static List<AnaliseDocumento> findDocumentos(Long idAnaliseJuridica) {
		
		return AnaliseDocumento.find("analiseJuridica.id = ? ", idAnaliseJuridica).fetch();
	}
}
