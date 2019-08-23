package controllers;

import models.Comunicado;
import serializers.ComunicadoSerializer;

public class Comunicados extends GenericController{


    public static void salvarParecerOrgao(Comunicado comunicado) {

        if(comunicado.id != null){
            Comunicado comunicadoBanco = Comunicado.findById(comunicado.id);
            comunicadoBanco.parecerOrgao = comunicado.parecerOrgao;
            comunicadoBanco.resolvido =true;
            comunicadoBanco.save();
            renderJSON(true);
        }

        renderJSON(false);
    }


    public static void findComunicado(Long id) {

        Comunicado comunicadoBanco =  Comunicado.findById(id);
        comunicadoBanco.valido =comunicadoBanco.isValido();
        renderJSON(comunicadoBanco, ComunicadoSerializer.findComunicado);

    }
}

