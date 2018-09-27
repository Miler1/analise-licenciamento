package controllers;

import models.manejoDigital.TipoLicencaManejo;
import security.Acao;
import serializers.TiposLicencasManejoSerializer;

public class TiposLicencaManejo extends InternalController {

	public static void findAll() {

		verificarPermissao(Acao.LISTAR_PROCESSOS_MANEJO);

		renderJSON(TipoLicencaManejo.findAll(), TiposLicencasManejoSerializer.findAll);
	}
}