package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class RestricaoSerializer {

	public static JSONSerializer find = SerializerUtil.create(
			"id",
			"parecerAnalistaTecnico",
			"texto");

}
