package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class CamadaGeoAtividadeSerializer {
	
	public static JSONSerializer getDadosProjeto = SerializerUtil.create(
			"caracterizacao.id",
			"atividades.geometrias.item",
			"atividades.geometrias.tipo",
			"atividades.geometrias.descricao",
			"atividades.geometrias.area",
			"atividades.geometrias.geometria",
			"atividades.atividadeCaracterizacao.id",
			"atividades.atividadeCaracterizacao.atividade.id",
			"atividades.atividadeCaracterizacao.atividade.nome",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.id",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.codigo",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.nome",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.orgaosResponsaveis",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.orgaosResponsaveis.sigla",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.id",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.geometria",
			"restricoes.item",
			"restricoes.tipo",
			"restricoes.descricao",
			"restricoes.geometria",
			"restricoes.area",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.id",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.id",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis.sigla",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.codigo")
			.transform(new GeometryTransformer(), Geometry.class);

	public static JSONSerializer getDadosRestricoesProjeto = SerializerUtil.create(
			"item",
			"tipo",
			"descricao",
			"sobreposicaoCaracterizacaoAtividades",
			"sobreposicaoCaracterizacaoAtividades.id",
			"sobreposicaoCaracterizacaoEmpreendimento",
			"sobreposicaoCaracterizacaoEmpreendimento.id",
			"sobreposicaoCaracterizacaoComplexo",
			"sobreposicaoCaracterizacaoComplexo.id");

}
