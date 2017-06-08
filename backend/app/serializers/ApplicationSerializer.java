package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class ApplicationSerializer {

	public static JSONSerializer findInfo = SerializerUtil.create(
			"usuarioSessao.nome",
			"usuarioSessao.cpfCnpj",
			"usuarioSessao.permissoes",
			"usuarioSessao.perfilSelecionado.id",
			"usuarioSessao.perfilSelecionado.nome",
			"configuracoes.baseURL");
}
