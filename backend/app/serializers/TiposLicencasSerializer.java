package serializers;

import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

import com.vividsolutions.jts.geom.Geometry;

import flexjson.JSONSerializer;

public class TiposLicencasSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			"id", 
			"nome");
}
