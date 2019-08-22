package controllers;

import models.Comunicado;

public class Comunicados extends GenericController{


    public static void salvarParecerOrgao(Comunicado comunicado) {

        if(comunicado.id != null){
            renderJSON(true);
        }

        renderJSON(false);
    }

}

