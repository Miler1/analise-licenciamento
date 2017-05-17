package models.licenciamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name = "geometria_atividade")
public class GeometriaAtividade extends GenericModel {

	private static final String SEQ = "licenciamento.geometria_atividade_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	public Double area;

	@Column(name = "the_geom")
	@Type(type = "org.hibernate.spatial.GeometryType")
	public Geometry geometria;

	@ManyToOne
	@JoinColumn(name = "id_atividade_caracterizacao", referencedColumnName = "id")
	public AtividadeCaracterizacao atividadeCaracterizacao;
}
