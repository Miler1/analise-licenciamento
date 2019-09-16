package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.GeometriaAtividade;
import models.licenciamento.SobreposicaoCaracterizacao;
import models.licenciamento.SobreposicaoCaracterizacaoAtividade;
import utils.GeoCalc;

public class CamadaGeoRestricaoVO {

	public String item;

	public String tipo;

	public String descricao;

	public Geometry geometria;

	public Double area;

	public SobreposicaoCaracterizacao sobreposicaoCaracterizacao;

	public CamadaGeoRestricaoVO(SobreposicaoCaracterizacao sobreposicaoCaracterizacao) {

		this.sobreposicaoCaracterizacao = sobreposicaoCaracterizacao;
		this.item = sobreposicaoCaracterizacao.tipoSobreposicao.nome;
		this.tipo = sobreposicaoCaracterizacao.tipoSobreposicao.codigo + "_" + Processo.indexDadosRestricoes;
		this.area = GeoCalc.areaHectare(sobreposicaoCaracterizacao.geometria);
		this.geometria = sobreposicaoCaracterizacao.geometria;
		this.descricao = Processo.getDescricaoRestricao(sobreposicaoCaracterizacao);

	}

}
