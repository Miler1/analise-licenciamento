package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class DocumentosImovelManejoSerializer {

	public static JSONSerializer upload = SerializerUtil.create(

			"id",
			"nome"
	);
}
