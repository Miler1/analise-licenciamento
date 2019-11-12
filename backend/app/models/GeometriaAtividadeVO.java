package models;

import com.vividsolutions.jts.geom.Geometry;
import enums.CamadaGeoEnum;
import utils.GeoCalc;

public class GeometriaAtividadeVO {

	public String item;

	public String tipo;

	public String descricao;

	public Double area;

	public Geometry geometria;

	public GeometriaAtividadeVO(Geometry geometria) {

		Processo.indexDadosGeometriasAtividade++;

		this.geometria = geometria;
		this.item = CamadaGeoEnum.ATIVIDADE.nome + "_" + Processo.indexDadosGeometriasAtividade;
		this.tipo = CamadaGeoEnum.ATIVIDADE.tipo + "_" + Processo.indexDadosAtividades;
		this.descricao = Processo.getDescricao(geometria);
		this.area = GeoCalc.area(geometria);

	}

	public GeometriaAtividadeVO(String item, String tipo, String descricao, Double area, Geometry geometria) {

		this.item = item;
		this.tipo = tipo;
		this.descricao = descricao;
		this.area = area;
		this.geometria = geometria;

	}

}
