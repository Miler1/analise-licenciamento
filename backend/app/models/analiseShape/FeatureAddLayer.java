package models.analiseShape;

import com.vividsolutions.jts.geom.Geometry;

public class FeatureAddLayer {

	public AtributosAddLayer attributes;
	public String geometry;

	public FeatureAddLayer(AtributosAddLayer attributes, String geometry) {

		this.attributes = attributes;
		this.geometry = geometry;
	}
}
