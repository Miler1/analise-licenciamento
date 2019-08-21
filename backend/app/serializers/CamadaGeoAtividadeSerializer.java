package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class CamadaGeoAtividadeSerializer {
	
	public static JSONSerializer getDadosGeoAtividade = SerializerUtil.create(
			"atividadeCaracterizacao.id",
			"atividadeCaracterizacao.atividade.id",
			"atividadeCaracterizacao.atividade.nome",
			"atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.codigo",
			"atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.nome",
			"atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.orgaosResponsaveis",
			"atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.orgaosResponsaveis.sigla",
			"atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.tipoSobreposicao.id",
			"atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.geometria",
			"camadasGeo.item",
			"camadasGeo.descricao",
			"camadasGeo.area",
			"camadasGeo.geometria",
			"camadasGeo.tipo",
			"camadasGeo.geometriaAtividade.id",
			"camadasGeo.restricoes.item",
			"camadasGeo.restricoes.descricao",
			"camadasGeo.restricoes.area",
			"camadasGeo.restricoes.geometria",
			"camadasGeo.restricoes.tipo")
			.transform(new GeometryTransformer(), Geometry.class);

}
