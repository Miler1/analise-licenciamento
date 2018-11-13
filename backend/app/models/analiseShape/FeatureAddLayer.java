package models.analiseShape;

import com.vividsolutions.jts.geom.Geometry;

public class FeatureAddLayer {

	public AtributosAddLayer attributes;
	public GeometryAddLayer geometry;

	public FeatureAddLayer(AtributosAddLayer attributes, Geometry geometry) {

		this.attributes = attributes;
		this.geometry = new GeometryAddLayer(geometry);
	}
}
