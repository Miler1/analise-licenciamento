package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class EmpreendimentoSerializer {
	
	public static JSONSerializer getDadosGeoEmpreendimento = SerializerUtil.create(
			"geometrias.item",
			"geometrias.descricao",
			"geometrias.area",
			"geometrias.geometria",
			"geometrias.tipo",
			"restricoes.item",
			"restricoes.descricao",
			"restricoes.area",
			"restricoes.geometria",
			"restricoes.tipo")
			.transform(new GeometryTransformer(), Geometry.class);

	public static JSONSerializer getDadosEmpreendimento = SerializerUtil.create(
			"id",
			"denominacao",
			"possuiShape");
}
