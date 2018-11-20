package models.manejoDigital;

import models.Documento;
import play.data.validation.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	@Column(name="tp_documento_manejo_shape")
	@Enumerated(EnumType.ORDINAL)
	public TipoDocumentoManejoShape tipo;

	@Required
	@ManyToOne
	@JoinColumn(name = "id_analise_manejo")
	public AnaliseManejo analiseManejo;
}