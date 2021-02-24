package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class UsuarioSerializer {

	UsuarioSerializer(){}

	public static JSONSerializer getConsultoresAnalistasGeo = SerializerUtil.create(
			"id",
			"login",
			"nome",
			"pessoa.id",
			"pessoa.nome");
	
	public static JSONSerializer getConsultoresAnalistasCoordenadores = SerializerUtil.create(
			"id",
			"login",
			"nome",
			"pessoa.id",
			"pessoa.nome");

	public static JSONSerializer getAnalistasTecnico = SerializerUtil.create(
			"id",
			"login",
			"nome",
			"pessoa.id",
			"pessoa.nome");

}
