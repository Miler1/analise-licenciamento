package controllers;

import exceptions.ValidacaoException;
import models.AnaliseGeo;
import models.Documento;
import models.Inconsistencia;
import serializers.InconsistenciaSerializer;
import utils.Mensagem;

public class Inconsistencias extends GenericController{


    public static void salvarInconsistencia(Inconsistencia inconsistencia) {

        if (inconsistencia.descricaoInconsistencia == null || inconsistencia.descricaoInconsistencia.equals("")) {

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
        }

        if(inconsistencia.tipoInconsistencia == null || inconsistencia.tipoInconsistencia.equals("")){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
        }

        if (inconsistencia.id != null) {

            Inconsistencia i = Inconsistencia.findById(inconsistencia.id);
            i.descricaoInconsistencia = inconsistencia.descricaoInconsistencia;
            i.tipoInconsistencia = inconsistencia.tipoInconsistencia;
            i.categoria = inconsistencia.categoria;
            i.analiseGeo = inconsistencia.analiseGeo;
            i.id = inconsistencia.id;
            i.saveAnexos(inconsistencia.anexos);
            i.save();

            renderJSON(i,InconsistenciaSerializer.findInconsistencia);

        } else {
            Inconsistencia novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo);

            novaInconsistencia.saveAnexos(inconsistencia.anexos);
            novaInconsistencia.save();
            renderJSON(novaInconsistencia,InconsistenciaSerializer.findInconsistencia);
        }

    }

    public static void findInconsistencia(Inconsistencia inconsistencia) {

        Inconsistencia i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria")
                .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                .setParameter("categoria",inconsistencia.categoria).first();

        renderJSON(i, InconsistenciaSerializer.findInconsistencia);

    }

}

