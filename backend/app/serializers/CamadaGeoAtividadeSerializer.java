package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class CamadaGeoAtividadeSerializer {
	
	public static JSONSerializer getDadosGeoAtividade = SerializerUtil.create(
			"atividade.id",
			"atividade.nome",
			"camadasGeo.item",
			"camadasGeo.descricao",
			"camadasGeo.area",
			"camadasGeo.geometria",
			"camadasGeo.tipo",
			"camadasGeo.restricoes.item",
			"camadasGeo.restricoes.descricao",
			"camadasGeo.restricoes.area",
			"camadasGeo.restricoes.geometria",
			"camadasGeo.restricoes.tipo")
			.transform(new GeometryTransformer(), Geometry.class);

}
