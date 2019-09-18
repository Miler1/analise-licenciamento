package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.SobreposicaoCaracterizacaoEmpreendimento;
import utils.GeoCalc;

public class CamadaGeoRestricaoVO {

	public String item;

	public String tipo;

	public String descricao;

	public Geometry geometria;

	public Double area;

	public SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento;

	public CamadaGeoRestricaoVO(SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento) {

		this.sobreposicaoCaracterizacaoEmpreendimento = sobreposicaoCaracterizacaoEmpreendimento;
		this.item = sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.nome;
		this.tipo = sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.codigo + "_" + Processo.indexDadosRestricoes;
		this.area = GeoCalc.areaHectare(sobreposicaoCaracterizacaoEmpreendimento.geometria);
		this.geometria = sobreposicaoCaracterizacaoEmpreendimento.geometria;
		this.descricao = Processo.getDescricaoRestricao(sobreposicaoCaracterizacaoEmpreendimento);

	}

}
