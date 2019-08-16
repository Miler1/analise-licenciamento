package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class TiposSobreposicaoSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			"id",
			"codigo",
			"nome",
			"orgaosResponsaveis.nome",
			"orgaosResponsaveis.sigla",
			"orgaosResponsaveis.email");

}
