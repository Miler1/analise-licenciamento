package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class ApplicationSerializer {

	public static JSONSerializer findInfo = SerializerUtil.create(
			"usuarioSessao.perfilSelecionado.id",
			"usuarioSessao.perfilSelecionado.nome",
			"usuarioSessao.setorSelecionado.id",
			"usuarioSessao.perfilSelecionado.listaPermissoes",
			"usuarioSessao.setorSelecionado.sigla",
			"usuarioSessao.acoesPermitidas",
			"usuarioSessao.usuarioEntradaUnica.autenticadoViaToken",
			"usuarioSessao.usuarioEntradaUnica.id",
			"usuarioSessao.usuarioEntradaUnica.login",
			"usuarioSessao.usuarioEntradaUnica.nome",
			"usuarioSessao.usuarioEntradaUnica.email",
			"usuarioSessao.usuarioEntradaUnica.perfis.id",
			"usuarioSessao.usuarioEntradaUnica.perfis.nome",
			"usuarioSessao.usuarioEntradaUnica.perfis.codigo",
			"usuarioSessao.usuarioEntradaUnica.perfis.permissoes.nome",
			"usuarioSessao.usuarioEntradaUnica.perfis.permissoes.codigo",
			"usuarioSessao.usuarioEntradaUnica.perfilSelecionado.id",
			"usuarioSessao.usuarioEntradaUnica.perfilSelecionado.nome",
			"usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo",
			"usuarioSessao.usuarioEntradaUnica.perfilSelecionado.permissoes.nome",
			"usuarioSessao.usuarioEntradaUnica.perfilSelecionado.permissoes.codigo",
			"configuracoes.baseURL");
}
