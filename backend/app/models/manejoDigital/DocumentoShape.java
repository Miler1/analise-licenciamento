package models.manejoDigital;

import models.Documento;
import models.manejoDigital.analise.analiseTecnica.AnaliseTecnicaManejo;
import play.data.validation.Required;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "documento_manejo_shape")
@PrimaryKeyJoinColumn(name = "id_documento", referencedColumnName = "id")
public class DocumentoShape extends Documento {

	@Required
	@Column(name = "geojson_arcgis")
	public String geoJsonArcgis;

	@Required
	@ManyToOne
	@JoinColumn(name = "id_analise_tecnica_manejo")
	public AnaliseTecnicaManejo analiseTecnicaManejo;
}