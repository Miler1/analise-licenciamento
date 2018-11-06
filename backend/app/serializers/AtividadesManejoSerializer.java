package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AtividadesManejoSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			"id",
			"nome",
			"codigo");
}
