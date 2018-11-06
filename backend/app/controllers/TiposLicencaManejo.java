package controllers;

import models.manejoDigital.TipoLicencaManejo;
import security.Acao;
import serializers.TiposLicencaManejoSerializer;

public class TiposLicencaManejo extends InternalController {

	public static void list() {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		renderJSON(TipoLicencaManejo.findAll(), TiposLicencaManejoSerializer.list);
	}
}