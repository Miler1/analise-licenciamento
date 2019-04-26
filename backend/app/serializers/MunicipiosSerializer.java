package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class MunicipiosSerializer {

	public static JSONSerializer findByEstado = SerializerUtil.create(
			"id", 
			"nome");
}
