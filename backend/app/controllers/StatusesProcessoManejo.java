package controllers;

import models.manejoDigital.StatusProcessoManejo;
import security.Acao;
import serializers.StatusProcessoManejoSerializer;

public class StatusesProcessoManejo extends InternalController {

	public static void findAll() {

		verificarPermissao(Acao.LISTAR_PROCESSOS_MANEJO);

		renderJSON(StatusProcessoManejo.findAll(), StatusProcessoManejoSerializer.findAll);
	}
}
