package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class UsuarioSerializer {

	public static JSONSerializer getConsultoresAnalistasGeo = SerializerUtil.create(
			"id",
			"login",
			"nome",
			"pessoa.id",
			"pessoa.nome");
	
	public static JSONSerializer getConsultoresAnalistasGerentes = SerializerUtil.create(
			"id",
			"login",
			"nome",
			"pessoa.id",
			"pessoa.nome");
}
