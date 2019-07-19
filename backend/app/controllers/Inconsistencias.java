package controllers;

import exceptions.ValidacaoException;
import models.AnaliseGeo;
import models.Inconsistencia;
import utils.Mensagem;

public class Inconsistencias extends GenericController{


    public static void salvarInconsistencia(Inconsistencia inconsistencia) {

        if (inconsistencia.descricaoInconsistencia == null) {

            throw new ValidacaoException(Mensagem.DESCRICAO_OBRIGATORIA);
        }

        if(inconsistencia.tipoInconsistencia == null){

            throw new ValidacaoException(Mensagem.TIPO_INCONSISTENCIA_OBRIGATORIA);
        }

        if (inconsistencia.id != null) {

        } else {
            Inconsistencia novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo);

            novaInconsistencia.saveAnexos(inconsistencia.anexos);
            novaInconsistencia.save();

        }


        renderMensagem(Mensagem.INCONSISTENCIA_SALVA_SUCESSO);

    }
}

