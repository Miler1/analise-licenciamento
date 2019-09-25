package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.SobreposicaoCaracterizacaoAtividade;
import models.licenciamento.SobreposicaoCaracterizacaoComplexo;
import models.licenciamento.SobreposicaoCaracterizacaoEmpreendimento;
import utils.GeoCalc;

public class CamadaGeoRestricaoVO {

	public String item;

	public String tipo;

	public String descricao;

	public Geometry geometria;

	public Double area;

	public SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento;

	public SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade;

	public SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo;

	public CamadaGeoRestricaoVO(SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento) {

		this.sobreposicaoCaracterizacaoEmpreendimento = sobreposicaoCaracterizacaoEmpreendimento;
		this.item = sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.nome;
		this.tipo = sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.codigo + "_" + Processo.indexDadosRestricoes;
		this.area = GeoCalc.areaHectare(sobreposicaoCaracterizacaoEmpreendimento.geometria);
		this.geometria = sobreposicaoCaracterizacaoEmpreendimento.geometria;
		this.descricao = Processo.getDescricaoRestricao(sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao, sobreposicaoCaracterizacaoEmpreendimento.geometria, sobreposicaoCaracterizacaoEmpreendimento.distancia);

	}

	public CamadaGeoRestricaoVO(SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade) {

		this.sobreposicaoCaracterizacaoAtividade = sobreposicaoCaracterizacaoAtividade;
		this.item = sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.nome;
		this.tipo = sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.codigo + "_" + Processo.indexDadosRestricoes;
		this.area = GeoCalc.areaHectare(sobreposicaoCaracterizacaoAtividade.geometria);
		this.geometria = sobreposicaoCaracterizacaoAtividade.geometria;
		this.descricao = Processo.getDescricaoRestricao(sobreposicaoCaracterizacaoAtividade.tipoSobreposicao, sobreposicaoCaracterizacaoAtividade.geometria, sobreposicaoCaracterizacaoAtividade.distancia);

	}

	public CamadaGeoRestricaoVO(SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo) {

		this.sobreposicaoCaracterizacaoComplexo = sobreposicaoCaracterizacaoComplexo;
		this.item = sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.nome;
		this.tipo = sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.codigo + "_" + Processo.indexDadosRestricoes;
		this.area = GeoCalc.areaHectare(sobreposicaoCaracterizacaoComplexo.geometria);
		this.geometria = sobreposicaoCaracterizacaoComplexo.geometria;
		this.descricao = Processo.getDescricaoRestricao(sobreposicaoCaracterizacaoComplexo.tipoSobreposicao, sobreposicaoCaracterizacaoComplexo.geometria, sobreposicaoCaracterizacaoComplexo.distancia);

	}

}
