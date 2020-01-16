package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class CondicionanteSerializer {

	public static JSONSerializer find = SerializerUtil.create(
			"id",
			"parecerAnalistaTecnico",
			"texto",
			"prazo");

}
