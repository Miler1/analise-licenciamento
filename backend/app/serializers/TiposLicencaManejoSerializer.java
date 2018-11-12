package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class TiposLicencaManejoSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			"id",
			"nome",
			"codigo");
}
