package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import models.GeometriaAtividadeVO;
import org.hibernate.annotations.Type;
import play.db.jpa.GenericModel;
import utils.GeoCalc;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "licenciamento", name = "geometria_atividade")
public class GeometriaAtividade extends GenericModel {

	private static final String SEQ = "licenciamento.geometria_atividade_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	public Double area;

	@Column(name = "the_geom", columnDefinition = "Geometry")
	public Geometry geometria;

	@ManyToOne
	@JoinColumn(name = "id_atividade_caracterizacao", referencedColumnName = "id")
	public AtividadeCaracterizacao atividadeCaracterizacao;

	public GeometriaAtividadeVO convertToVO() {

		return new GeometriaAtividadeVO(this.geometria);

	}

}
