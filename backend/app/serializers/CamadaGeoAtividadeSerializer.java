package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class CamadaGeoAtividadeSerializer {
	
	public static JSONSerializer getDadosProjeto = SerializerUtil.create(
			"caracterizacao.id",
			"categoria",
			"atividades.geometrias.item",
			"atividades.geometrias.tipo",
			"atividades.geometrias.parametro",
			"atividades.geometrias.descricao",
			"atividades.geometrias.area",
			"atividades.geometrias.geometria",
			"atividades.atividadeCaracterizacao.id",
			"atividades.atividadeCaracterizacao.atividade.id",
			"atividades.atividadeCaracterizacao.atividade.nome",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade.id",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.codigo",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.nome",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis.sigla",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.id",
			"atividades.atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade.geometria",
			"restricoes.item",
			"restricoes.tipo",
			"restricoes.descricao",
			"restricoes.geometria",
			"restricoes.area",
			"restricoes.nomeAreaSobreposicao",
			"restricoes.dataAreaSobreposicao",
			"restricoes.cpfCnpjAreaSobreposicao",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.id",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.id",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis.sigla",
			"restricoes.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.codigo",
			"restricoes.sobreposicaoCaracterizacaoComplexo.id",
			"restricoes.sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.id",
			"restricoes.sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis",
			"restricoes.sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis.sigla",
			"restricoes.sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.codigo",
			"restricoes.sobreposicaoCaracterizacaoAtividade.id",
			"restricoes.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.id",
			"restricoes.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis",
			"restricoes.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis.sigla",
			"restricoes.sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.codigo",
			"complexo.geometrias.item",
			"complexo.geometrias.tipo",
			"complexo.geometrias.descricao",
			"complexo.geometrias.area",
			"complexo.geometrias.geometria")
			.transform(new GeometryTransformer(), Geometry.class);

	public static JSONSerializer getDadosRestricoesProjeto = SerializerUtil.create(
			"item",
			"tipo",
			"descricao",
			"sobreposicaoCaracterizacaoAtividade",
			"sobreposicaoCaracterizacaoAtividade.id",
			"sobreposicaoCaracterizacaoAtividade.tipoSobreposicao",
			"sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis",
			"sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis.sigla",
			"sobreposicaoCaracterizacaoEmpreendimento",
			"sobreposicaoCaracterizacaoEmpreendimento.id",
			"sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao",
			"sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis",
			"sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis.sigla",
			"sobreposicaoCaracterizacaoComplexo",
			"sobreposicaoCaracterizacaoComplexo.id",
			"sobreposicaoCaracterizacaoComplexo.tipoSobreposicao",
			"sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis",
			"sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis.sigla");

}
