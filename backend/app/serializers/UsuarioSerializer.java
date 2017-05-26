package serializers;

import java.util.Date;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class UsuarioSerializer {
	
	public static JSONSerializer getConsultoresJuridico = SerializerUtil.create(
			"id",
			"pessoa.nome");
}
