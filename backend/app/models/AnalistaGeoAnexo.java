package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.Empreendimento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema="analise", name="analise_geo_anexo")
public class AnalistaGeoAnexo extends GenericModel {

	public static final String SEQ = "analise.analise_geo_anexo_id_seq";

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

	public AnalistaGeoAnexo() {

	}

	public AnalistaGeoAnexo(Empreendimento empreendimento, TipoAreaGeometria tipoAreaGeometria, Geometry geom) {

		super();
		this.empreendimento = empreendimento;
		this.tipoAreaGeometria = tipoAreaGeometria;
		this.geom = geom;

	}


}
