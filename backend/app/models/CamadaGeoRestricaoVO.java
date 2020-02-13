package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.Orgao;
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

	public Orgao orgao;

	public String nomeAreaSobreposicao;

	public String dataAreaSobreposicao;

	public String cpfCnpjAreaSobreposicao;

	public SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento;

	public SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade;

	public SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo;

	public CamadaGeoRestricaoVO(SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento) {

		this.sobreposicaoCaracterizacaoEmpreendimento = sobreposicaoCaracterizacaoEmpreendimento;
		this.item = sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.nome;
		this.tipo = sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.codigo + "_" + Processo.indexDadosRestricoes++;
		this.area = GeoCalc.areaHectare(sobreposicaoCaracterizacaoEmpreendimento.geometria);
		this.geometria = sobreposicaoCaracterizacaoEmpreendimento.geometria;
		this.descricao = Processo.getDescricaoRestricao(sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao, sobreposicaoCaracterizacaoEmpreendimento.geometria, sobreposicaoCaracterizacaoEmpreendimento.distancia);
		this.orgao = sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis.get(0);

		this.nomeAreaSobreposicao = sobreposicaoCaracterizacaoEmpreendimento.nomeAreaSobreposicao;
		this.dataAreaSobreposicao = sobreposicaoCaracterizacaoEmpreendimento.dataAreaSobreposicao;
		this.cpfCnpjAreaSobreposicao = sobreposicaoCaracterizacaoEmpreendimento.cpfCnpjAreaSobreposicao;

	}

	public CamadaGeoRestricaoVO(SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade) {

		this.sobreposicaoCaracterizacaoAtividade = sobreposicaoCaracterizacaoAtividade;
		this.item = sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.nome;
		this.tipo = sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.codigo + "_" + Processo.indexDadosRestricoes++;
		this.area = GeoCalc.areaHectare(sobreposicaoCaracterizacaoAtividade.geometria);
		this.geometria = sobreposicaoCaracterizacaoAtividade.geometria;
		this.descricao = Processo.getDescricaoRestricao(sobreposicaoCaracterizacaoAtividade.tipoSobreposicao, sobreposicaoCaracterizacaoAtividade.geometria, sobreposicaoCaracterizacaoAtividade.distancia);
		this.orgao = sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis.get(0);

		this.nomeAreaSobreposicao = sobreposicaoCaracterizacaoAtividade.nomeAreaSobreposicao;
		this.dataAreaSobreposicao = sobreposicaoCaracterizacaoAtividade.dataAreaSobreposicao;
		this.cpfCnpjAreaSobreposicao = sobreposicaoCaracterizacaoAtividade.cpfCnpjAreaSobreposicao;

	}

	public CamadaGeoRestricaoVO(SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo) {

		this.sobreposicaoCaracterizacaoComplexo = sobreposicaoCaracterizacaoComplexo;
		this.item = sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.nome;
		this.tipo = sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.codigo + "_" + Processo.indexDadosRestricoes++;
		this.area = GeoCalc.areaHectare(sobreposicaoCaracterizacaoComplexo.geometria);
		this.geometria = sobreposicaoCaracterizacaoComplexo.geometria;
		this.descricao = Processo.getDescricaoRestricao(sobreposicaoCaracterizacaoComplexo.tipoSobreposicao, sobreposicaoCaracterizacaoComplexo.geometria, sobreposicaoCaracterizacaoComplexo.distancia);
		this.orgao = sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis.get(0);

		this.nomeAreaSobreposicao = sobreposicaoCaracterizacaoComplexo.nomeAreaSobreposicao;
		this.dataAreaSobreposicao = sobreposicaoCaracterizacaoComplexo.dataAreaSobreposicao;
		this.cpfCnpjAreaSobreposicao = sobreposicaoCaracterizacaoComplexo.cpfCnpjAreaSobreposicao;

	}

}
