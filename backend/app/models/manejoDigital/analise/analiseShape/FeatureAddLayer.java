package models.manejoDigital.analise.analiseShape;

public class FeatureAddLayer {

	public AtributosAddLayer attributes;
	public String geometry;

	public FeatureAddLayer(AtributosAddLayer attributes, String geometry) {

		this.attributes = attributes;
		this.geometry = geometry;
	}
}
