package controllers;

import java.util.List;

import models.LicencaAnalise;
import play.mvc.*;

public class LicencasAnalise extends InternalController {

    public static void emitirLicencaAnalise(List<LicencaAnalise> licencasAnalise) {
    	
    	LicencaAnalise.emitirLicencas(licencasAnalise);
        
    }

}
