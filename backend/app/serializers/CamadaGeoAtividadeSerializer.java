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
			"restricoes.sobreposicaoCaracterizacao.id",
			"restricoes.sobreposicaoCaracterizacao.tipoSobreposicao.id",
			"restricoes.sobreposicaoCaracterizacao.tipoSobreposicao.orgaosResponsaveis",
			"restricoes.sobreposicaoCaracterizacao.tipoSobreposicao.orgaosResponsaveis.sigla",
			"restricoes.sobreposicaoCaracterizacao.tipoSobreposicao.codigo")
			.transform(new GeometryTransformer(), Geometry.class);

}
