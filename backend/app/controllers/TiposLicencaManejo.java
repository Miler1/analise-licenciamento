package controllers;

import models.licenciamento.TipoLicenca;
import security.Acao;
import serializers.TiposLicencaManejoSerializer;

public class TiposLicencaManejo extends InternalController {

	public static void list() {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		renderJSON(TipoLicenca.findAll(), TiposLicencaManejoSerializer.list);
	}
}
