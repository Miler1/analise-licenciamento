package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AtividadesSerializer {

	public static JSONSerializer listAtividadesSimplificado = SerializerUtil.create(
			"id", 
			"nome");
}
