package models;

import com.vividsolutions.jts.geom.Geometry;

public class CamadaGeo {

	public String item;

	public String descricao;

	public Double area;

	public Geometry geometria;

	public CamadaGeo(String item, String descricao, Double area, Geometry geometria) {

		this.item = item;
		this.descricao = descricao;
		this.area = area;
		this.geometria = geometria;
	}
}
