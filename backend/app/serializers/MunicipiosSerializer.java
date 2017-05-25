package serializers;

import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

import com.vividsolutions.jts.geom.Geometry;

import flexjson.JSONSerializer;

public class MunicipiosSerializer {

	public static JSONSerializer findByEstado = SerializerUtil.create(
			"id", 
			"nome");
}
