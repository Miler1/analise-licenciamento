package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class DocumentosManejoSerializer {

	public static JSONSerializer upload = SerializerUtil.create(

			"id",
			"nomeArquivo"
	);
}
