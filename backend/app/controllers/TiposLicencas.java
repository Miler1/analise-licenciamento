package controllers;

import models.licenciamento.TipoLicenca;
import security.Acao;
import serializers.TiposLicencasSerializer;

import java.util.List;
import java.util.stream.Collectors;

public class TiposLicencas extends InternalController {
	
	public static void list() {

		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);

		List<TipoLicenca> listaTipoLicenca =  TipoLicenca.findAll();

		renderJSON(listaTipoLicenca.stream().filter(tipoLicenca -> !
				tipoLicenca.sigla.equals("ALP") && !tipoLicenca.sigla.equals("LAU-SV-AF")).collect(Collectors.toList()), TiposLicencasSerializer.list);

	}

}
