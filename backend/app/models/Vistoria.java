package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "analise", name = "vistoria")
public class Vistoria extends GenericModel {

	public static final String SEQ = "analise.vistoria_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_parecer_analista_tecnico", referencedColumnName = "id")
	public ParecerAnalistaTecnico parecerAnalistaTecnico;

	@Column(name = "tx_conclusao")
	public String conclusao;

	@Column(name = "realizada")
	public Boolean realizada;

	@OneToOne
	@Required
	@JoinColumn(name = "id_documento_rit")
	public Documento documentoRit;

	@OneToOne(mappedBy = "vistoria")
	public InconsistenciaVistoria inconsistenciaVistoria;

	@Column(name = "data")
	public Date data;

	@Column(name = "hora")
	@Temporal(TemporalType.TIMESTAMP)
	public Date hora;

	@Column(name = "tx_descricao")
	public String descricao;

	@Column(name = "tx_cursos_dagua")
	public String cursosDagua;

	@Column(name = "tx_tipologia_vegetal")
	public String tipologiaVegetal;

	@Column(name = "tx_app")
	public String app;

	@Column(name = "tx_ocorrencia")
	public String ocorrencia;

	@Column(name = "tx_residuos_liquidos")
	public String residuosLiquidos;

	@Column(name = "tx_outras_informacoes")
	public String outrasInformacoes;

	@OneToMany(mappedBy = "vistoria")
	public List<EquipeVistoria> equipe;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema="analise", name="rel_documento_vistoria",
			joinColumns=@JoinColumn(name="id_vistoria"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> anexos;

}
