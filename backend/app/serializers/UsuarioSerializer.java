package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class UsuarioSerializer {
	
	public static JSONSerializer getConsultoresAnalistasGerentes = SerializerUtil.create(
			"id",
			"pessoa.nome");
}
