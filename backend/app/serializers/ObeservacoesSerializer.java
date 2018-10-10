package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ObeservacoesSerializer {

	public static JSONSerializer save = SerializerUtil.createWithDateTime(
			"id",
			"texto",
			"analiseManejo.id",
			"passoAnalise.id",
			"data"
	);
}
