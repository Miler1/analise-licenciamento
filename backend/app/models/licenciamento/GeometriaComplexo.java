package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;

import com.vividsolutions.jts.io.WKBWriter;
import enums.CamadaGeoEnum;
import models.GeometriaAtividadeVO;
import org.hibernate.annotations.Type;

import play.db.jpa.GenericModel;
import utils.GeoCalc;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "licenciamento", name = "geometria_complexo")
public class GeometriaComplexo extends GenericModel {

	private static final String SEQ = "licenciamento.geometria_complexo_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	public Double area;

	@Column(name = "geometria", columnDefinition="Geometry")
	public Geometry geometria;

	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id")
	public Caracterizacao caracterizacao;

	public GeometriaAtividadeVO convertToVO() {

		return new GeometriaAtividadeVO(this.geometria, CamadaGeoEnum.COMPLEXO);

	}

}
