package controllers;

import models.manejoDigital.TipologiaManejo;
import security.Acao;
import serializers.TipologiasManejoSerializer;

public class TipologiasManejo extends InternalController {

	public static void list() {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		renderJSON(TipologiaManejo.findAll(), TipologiasManejoSerializer.list);
	}
}
