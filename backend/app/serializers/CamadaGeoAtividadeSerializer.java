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
			"atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividades.geometria",
			"camadasGeo.item",
			"camadasGeo.descricao",
			"camadasGeo.area",
			"camadasGeo.geometria",
			"camadasGeo.tipo",
			"camadasGeo.geometriaAtividade.id",
			"restricoes.item",
			"restricoes.descricao",
			"restricoes.area",
			"restricoes.geometria",
			"restricoes.tipo")
			.transform(new GeometryTransformer(), Geometry.class);

}
