package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.GeometriaAtividade;
import models.licenciamento.SobreposicaoCaracterizacao;

import java.util.List;
import models.licenciamento.SobreposicaoCaracterizacaoAtividade;

public class CamadaGeoAtividade {

	public String item;

	public String tipo;

	public String descricao;

	public Double area;

	public Geometry geometria;

	public AtividadeCaracterizacao atividadeCaracterizacao;

	public CamadaGeoAtividade(String item, String tipo, String descricao, Double area, Geometry geometria) {

		this.item = item;
		this.tipo = tipo;
		this.descricao = descricao;
		this.area = area;
		this.geometria = geometria;

	}

}
