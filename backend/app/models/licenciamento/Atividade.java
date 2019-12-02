package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;


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

	@Column(name = "dentro_empreendimento")
	public Boolean dentroEmpreendimento;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_parametro_atividade",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_parametro_atividade"))
	public List<ParametroAtividade> parametros;

	@ManyToOne
	@JoinColumn(name="id_potencial_poluidor")
	public PotencialPoluidor potencialPoluidor;
	
	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_tipo_licenca",
			joinColumns = @JoinColumn(name = "id_atividade"),
			inverseJoinColumns = @JoinColumn(name = "id_tipo_licenca"))
	public List<TipoLicenca> tiposLicenca;
	
	@OneToMany(mappedBy="atividade")
	public List<TipoCaracterizacaoAtividade> tiposCaracterizacoesAtividade;

	@Column(name = "sigla_setor")
	public String siglaSetor;

	@Transient
	public List<AtividadeCnae> atividadesCnae;
	
	public static List<Atividade> listAtividadesSimplificado() {
		
		List<Atividade> atividades =
				Atividade.find("SELECT a FROM atividade a JOIN a.tiposCaracterizacoesAtividade t WHERE t.licenciamentoSimplificado = TRUE").fetch();
		
		return atividades;
	}

}

