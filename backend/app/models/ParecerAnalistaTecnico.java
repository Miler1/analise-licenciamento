package models;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "analise", name = "parecer_analista_tecnico")
public class ParecerAnalistaTecnico extends GenericModel {

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
	@JoinColumn(name = "id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@Column(name = "data")
	public Date data;

	@OneToOne
	@JoinColumn(name = "id_usuario_analista_tecnico", referencedColumnName = "id")
	public UsuarioAnalise analistaTecnico;

	@Column(name = "do_processo")
	public String doProcesso;

	@Column(name = "da_analise_tecnica")
	public String daAnaliseTecnica;

	@Column(name = "da_conclusao")
	public String daConclusao;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema="analise", name="rel_documento_parecer_analista_tecnico",
			joinColumns=@JoinColumn(name="id_parecer_analista_tecnico"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;

}
