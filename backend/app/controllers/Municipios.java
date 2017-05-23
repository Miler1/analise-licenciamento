package controllers;

import java.util.List;

import models.licenciamento.Municipio;
import serializers.MunicipiosSerializer;

public class Municipios extends InternalController {

	public static void listByEstado(String uf) {
		
		List<Municipio> municipios = Municipio.findByEstado(uf);
		renderJSON(municipios, MunicipiosSerializer.findByEstado);
	}
	
}
