package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class ApplicationSerializer {

	public static JSONSerializer findInfo = SerializerUtil.create(
			"usuarioSessao.nome",
			"usuarioSessao.cpfCnpj",
			"usuarioSessao.permissoes");
}
