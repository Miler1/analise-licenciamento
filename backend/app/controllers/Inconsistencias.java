package controllers;

import exceptions.ValidacaoException;
import models.AnaliseGeo;
import models.Documento;
import models.Inconsistencia;
import serializers.InconsistenciaSerializer;
import utils.Mensagem;
import utils.ModelUtil;

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
            i.atividadeCaracterizacao = inconsistencia.atividadeCaracterizacao;
            i.geometriaAtividade = inconsistencia.geometriaAtividade;
            i.saveAnexos(inconsistencia.anexos);
            i.save();

            renderJSON(i,InconsistenciaSerializer.findInconsistencia);

        } else {
            if(inconsistencia.geometriaAtividade.id == null && inconsistencia.atividadeCaracterizacao.id == null){
                Inconsistencia novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo);

                novaInconsistencia.saveAnexos(inconsistencia.anexos);
                novaInconsistencia.save();
                renderJSON(novaInconsistencia,InconsistenciaSerializer.findInconsistencia);

            }else{
                Inconsistencia novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo, inconsistencia.atividadeCaracterizacao, inconsistencia.geometriaAtividade);

                novaInconsistencia.saveAnexos(inconsistencia.anexos);
                novaInconsistencia.save();
                renderJSON(novaInconsistencia,InconsistenciaSerializer.findInconsistencia);
            }

        }

    }

    public static void findInconsistencia(Inconsistencia inconsistencia) {

        if(inconsistencia.atividadeCaracterizacao == null && inconsistencia.geometriaAtividade == null){
            Inconsistencia i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria")
                    .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                    .setParameter("categoria",inconsistencia.categoria).first();

            renderJSON(i, InconsistenciaSerializer.findInconsistencia);
        }else{
            Inconsistencia i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and atividadeCaracterizacao.id = :atividadeCaracterizacao and  geometriaAtividade.id = :geometriaAtividade")
                    .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                    .setParameter("atividadeCaracterizacao",inconsistencia.atividadeCaracterizacao.id)
                    .setParameter("geometriaAtividade",inconsistencia.geometriaAtividade.id)
                    .setParameter("categoria",inconsistencia.categoria).first();

            renderJSON(i, InconsistenciaSerializer.findInconsistencia);
        }


    }

    public static void excluirInconsistencia(Long id) {

        returnIfNull(id, "Long");

        Inconsistencia i = Inconsistencia.findById(id);

        i.deleteAnexos();

        i.delete();

        renderText(Mensagem.INCONSISTENCIA_EXCLUIDA_SUCESSO.getTexto());

    }

}

