package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class TiposLicencasSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			"id", 
			"nome");
}
