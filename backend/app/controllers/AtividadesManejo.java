package controllers;

import models.manejoDigital.AtividadeManejo;
import security.Acao;
import serializers.AtividadesManejoSerializer;

public class AtividadesManejo extends InternalController {

	public static void list() {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		renderJSON(AtividadeManejo.findAll(), AtividadesManejoSerializer.list);
	}
}
