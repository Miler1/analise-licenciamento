package serializers;

import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

import com.vividsolutions.jts.geom.Geometry;

import flexjson.JSONSerializer;

public class AtividadesSerializer {

	public static JSONSerializer listAtividadesSimplificado = SerializerUtil.create(
			"id", 
			"nome");
}
