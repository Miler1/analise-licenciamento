package controllers;

import models.licenciamento.TipoLicenca;
import security.Acao;
import serializers.TiposLicencasSerializer;

public class TiposLicencas extends InternalController {
	
	public static void list() {

		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		renderJSON(TipoLicenca.findAll(), TiposLicencasSerializer.list);
	}

}
