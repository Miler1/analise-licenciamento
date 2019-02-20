package controllers;

import models.licenciamento.Municipio;
import serializers.MunicipiosSerializer;

import java.util.List;

public class Municipios extends InternalController {

	public static void listByEstado(String uf) {
		
		List<Municipio> municipios = Municipio.findByEstado(uf);
		renderJSON(municipios, MunicipiosSerializer.findByEstado);
	}
	
}
