package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ObservacoesSerializer {

	public static JSONSerializer save = SerializerUtil.createWithDateTime(
			"id",
			"texto",
			"analiseTecnicaManejo.id",
			"passoAnalise.id",
			"data"
	);
}
