package controllers;
import java.util.List;

import models.licenciamento.Licenca;
import serializers.LicencaSerializer;
import utils.Mensagem;

public class Licencas extends InternalController{

	public static void findByIdLicenca(Long idLicenca) {
    	
    	returnIfNull(idLicenca, "Long");
    	
    	Licenca licenca = Licenca.findById(idLicenca);
    	
    	
    	renderJSON(licenca, LicencaSerializer.listSuspensao);
    	
    }
}
