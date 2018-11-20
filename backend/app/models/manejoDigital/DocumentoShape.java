package models.manejoDigital;

import models.Documento;
import play.data.validation.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

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