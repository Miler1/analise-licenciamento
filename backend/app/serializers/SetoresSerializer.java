package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class SetoresSerializer {

	public static JSONSerializer getSetoresByNivel = SerializerUtil.create(
			"id", 
			"nome");
}
