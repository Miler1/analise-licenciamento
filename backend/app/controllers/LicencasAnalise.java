package controllers;

import java.util.List;

import models.LicencaAnalise;
import models.portalSeguranca.Usuario;
import play.mvc.*;
import security.UsuarioSessao;

public class LicencasAnalise extends InternalController {

    public static void emitirLicencaAnalise(List<LicencaAnalise> licencasAnalise) {
    	
    	UsuarioSessao usuarioSessao = getUsuarioSessao();
    	Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);
    	
    	LicencaAnalise.emitirLicencas(licencasAnalise, usuarioExecutor);
        
    }

}
