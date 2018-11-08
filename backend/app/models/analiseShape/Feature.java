package models.analiseShape;

import com.vividsolutions.jts.geom.Geometry;

public class Feature {

	public AtributosFeature attributes;
	public Geometry geometry;

	public Feature(AtributosFeature attributes, Geometry geometry) {

		this.attributes = attributes;
		this.geometry = geometry;
	}
}
