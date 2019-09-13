package models;

import com.vividsolutions.jts.geom.Geometry;
import enums.CamadaGeoEnum;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.GeometriaAtividade;
import models.licenciamento.SobreposicaoCaracterizacao;

import java.util.List;
import models.licenciamento.SobreposicaoCaracterizacaoAtividade;
import utils.GeoCalc;

public class CamadaGeoAtividadeVO {

	public String item;

	public String tipo;

	public String descricao;

	public Double area;

	public Geometry geometria;

	public AtividadeCaracterizacao atividadeCaracterizacao;

	public CamadaGeoAtividadeVO(AtividadeCaracterizacao atividadeCaracterizacao, Geometry geometria) {

		this.atividadeCaracterizacao = atividadeCaracterizacao;
		this.geometria = geometria;
		this.item = CamadaGeoEnum.ATIVIDADE.nome + "_" + Processo.index;
		this.tipo = CamadaGeoEnum.ATIVIDADE.tipo + "_" + Processo.index;
		this.descricao = Processo.getDescricaoSobreposicao(geometria);
		this.area = GeoCalc.area(geometria);

	}

}
