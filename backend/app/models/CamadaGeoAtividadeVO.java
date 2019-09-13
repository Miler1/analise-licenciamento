package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.GeometriaAtividade;
import models.licenciamento.SobreposicaoCaracterizacao;

import java.util.List;
import models.licenciamento.SobreposicaoCaracterizacaoAtividade;

public class CamadaGeoAtividadeVO {

	public String item;

	public String tipo;

	public String descricao;

	public Double area;

	public Geometry geometria;

	public AtividadeCaracterizacao atividadeCaracterizacao;

	public CamadaGeoAtividadeVO(String item, String tipo, String descricao, Double area, Geometry geometria, AtividadeCaracterizacao atividadeCaracterizacao) {

		this.item = item;
		this.tipo = tipo;
		this.descricao = descricao;
		this.area = area;
		this.geometria = geometria;
		this.atividadeCaracterizacao = atividadeCaracterizacao;

	}

}
