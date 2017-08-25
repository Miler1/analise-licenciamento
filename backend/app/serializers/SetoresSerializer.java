package serializers;

import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

import com.vividsolutions.jts.geom.Geometry;

import flexjson.JSONSerializer;

public class SetoresSerializer {

	public static JSONSerializer getSetoresByNivel = SerializerUtil.create(
			"id", 
			"nome");
}
