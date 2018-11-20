package models.manejoDigital;

import models.Documento;
import play.data.validation.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "analise", name = "documento_manejo_shape")
public class DocumentoShape extends Documento {

	@Required
	@Column(name = "geojson_arcgis")
	public String geoJsonArcgis;

	@Required
	@ManyToOne
	@JoinColumn(name = "id_analise_manejo")
	public AnaliseManejo analiseManejo;
}