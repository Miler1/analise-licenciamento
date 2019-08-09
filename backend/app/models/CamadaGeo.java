package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.Atividade;

import java.util.List;

public class CamadaGeo {

	public String item;

	public String tipo;

	public String descricao;

	public Double area;

	public Geometry geometria;

	List<CamadaGeo> restricoes;

	public CamadaGeo(String item, String tipo, String descricao, Double area, Geometry geometria) {

		this.item = item;
		this.tipo = tipo;
		this.descricao = descricao;
		this.area = area;
		this.geometria = geometria;

	}
}
