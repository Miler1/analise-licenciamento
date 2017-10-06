package controllers;

import java.util.List;

import models.LicencaAnalise;
import models.licenciamento.Licenca;
import models.portalSeguranca.Usuario;
import security.UsuarioSessao;
import serializers.LicencaAnaliseSerializer;
import utils.Mensagem;

public class LicencasAnalise extends InternalController {

    public static void emitirLicencaAnalise(LicencaAnalise... licencasAnalise) {
    	
    	UsuarioSessao usuarioSessao = getUsuarioSessao();
    	Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);
    	
    	LicencaAnalise.emitirLicencas(licencasAnalise, usuarioExecutor);
    	
    	renderMensagem(Mensagem.LICENCAS_EMITIDAS_SUCESSO);
        
    }

}
