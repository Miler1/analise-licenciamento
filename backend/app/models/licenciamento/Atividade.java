package models.licenciamento;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;


@Entity
@Table(schema = "licenciamento", name = "atividade")
public class Atividade extends GenericModel {

	@Id
	public Long id;

	public String nome;

	@ManyToOne
	@JoinColumn(name="id_tipologia")
	public Tipologia tipologia;

	@Column(name = "geo_linha")
	public Boolean temLinha;

	@Column(name = "geo_ponto")
	public Boolean temPonto;

	@Column(name = "geo_poligono")
	public Boolean temPoligono;

	@ManyToMany
	@OrderBy("ordem ASC")
	@JoinTable(schema = "licenciamento", name = "rel_atividade_pergunta",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_pergunta"))
	public List<Pergunta> perguntas;

	public String codigo;

	@Column(name = "licenciamento_municipal")
	public Boolean licenciamentoMunicipal;
	
	@Column(name = "limite_parametro_municipal")
	public Double limiteParametroMunicipal;
	
	@Column(name = "limite_inferior_simplificado")
	public Double limiteInferiorLicenciamentoSimplificado;
	
	@Column(name = "limite_superior_simplificado")
	public Double limiteSuperiorLicenciamentoSimplificado;
	
	@ManyToOne
	@JoinColumn(name="id_parametro")
	public ParametroAtividade parametro;

	@ManyToOne
	@JoinColumn(name="id_potencial_poluidor")
	public PotencialPoluidor potencialPoluidor;
	
	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_tipo_licenca",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_tipo_licenca"))
	public List<TipoLicenca> tiposLicenca;
	
	@Transient
	public List<AtividadeCnae> atividadesCnae;

}

