package controllers;

import models.licenciamento.Licenca;
import serializers.LicencaSerializer;

public class Licencas extends InternalController{

	public static void findByIdLicenca(Long idLicenca) {
    	
    	returnIfNull(idLicenca, "Long");
    	
    	Licenca licenca = Licenca.findById(idLicenca);
    	
    	renderJSON(licenca, LicencaSerializer.listSuspensao);
    	
    }
}
