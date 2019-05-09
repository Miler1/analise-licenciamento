package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class CondicaoSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			"idCondicao", 
			"nomeCondicao");

	public static JSONSerializer listManejo = SerializerUtil.create(
			"idCondicao",
			"nome");
}
