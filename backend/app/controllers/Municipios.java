package controllers;

import java.util.List;

import models.licenciamento.Municipio;
import security.Acao;
import serializers.MunicipiosSerializer;

public class Municipios extends InternalController {

	public static void listByEstado(String uf) {
		
		verificarPermissao(Acao.LISTAR_PROCESSO);
		
		List<Municipio> municipios = Municipio.findByEstado(uf);
		renderJSON(municipios, MunicipiosSerializer.findByEstado);
	}
	
}
