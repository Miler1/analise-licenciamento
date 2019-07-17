package controllers;

import models.AnaliseGeo;
import models.Inconsistencia;
import utils.Mensagem;

public class Inconsistencias extends GenericController{


    public static void salvarInconsistencia(Inconsistencia inconsistencia) {

        if (inconsistencia.descricaoInconsistencia == null) {

            renderMensagem(Mensagem.DESCRICAO_OBRIGATORIA);

        }else if(inconsistencia.tipoInconsistencia == null){

            renderMensagem(Mensagem.TIPO_INCONSISTENCIA_OBRIGATORIA);

        }else {
            inconsistencia.save();
            renderMensagem(Mensagem.INCONSISTENCIA_SALVA_SUCESSO);
        }
    }
}

