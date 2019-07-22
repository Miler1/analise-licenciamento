package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.Empreendimento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema="analise", name="empreendimento_camada_geo")
public class AnalistaGeoAnexo extends GenericModel {

	public static final String SEQ = "analise.empreendimento_camada_geo_id_seq";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@Required
	@ManyToOne
	@JoinColumn(name="id_empreendimento", referencedColumnName = "id")
	public Empreendimento empreendimento;

	@Required
	@ManyToOne
	@JoinColumn(name="id_tipo_area_geometria", referencedColumnName = "id")
	public TipoAreaGeometria tipoAreaGeometria;

	@Required
	@Column(name="geom")
	public Geometry geom;

	@Required
	@Column(name="area")
	public Double area;

	public AnalistaGeoAnexo() {

	}

	public AnalistaGeoAnexo(Empreendimento empreendimento, TipoAreaGeometria tipoAreaGeometria, Geometry geom) {

		super();
		this.empreendimento = empreendimento;
		this.tipoAreaGeometria = tipoAreaGeometria;
		this.geom = geom;

	}


}
