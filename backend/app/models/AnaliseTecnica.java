package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Configuracoes;

@Entity
@Table(schema="analise", name="analise_tecnica")
public class AnaliseTecnica extends GenericModel {

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
}
