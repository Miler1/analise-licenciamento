package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.GeometriaAtividade;
import models.licenciamento.SobreposicaoCaracterizacao;

import java.util.List;

public class CamadaGeo {

	public String item;

	public String tipo;

	public String descricao;

	public Double area;

	public Geometry geometria;

	public GeometriaAtividade geometriaAtividade;

	public SobreposicaoCaracterizacao sobreposicaoCaracterizacao;

	public CamadaGeo(String item, String tipo, String descricao, Double area, Geometry geometria) {

		this.item = item;
		this.tipo = tipo;
		this.descricao = descricao;
		this.area = area;
		this.geometria = geometria;

	}

	public CamadaGeo(String item, String tipo, String descricao, Double area, Geometry geometria, SobreposicaoCaracterizacao sobreposicaoCaracterizacao) {

		this.item = item;
		this.tipo = tipo;
		this.descricao = descricao;
		this.area = area;
		this.geometria = geometria;
		this.sobreposicaoCaracterizacao = sobreposicaoCaracterizacao;

	}
}
