package controllers;

import exceptions.ValidacaoException;
import models.AnaliseGeo;
import models.Documento;
import models.Inconsistencia;
import serializers.InconsistenciaSerializer;
import utils.Mensagem;
import utils.ModelUtil;

import java.util.Objects;

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
            i.atividadeCaracterizacao = Objects.nonNull(inconsistencia.atividadeCaracterizacao.id) ? inconsistencia.atividadeCaracterizacao : null;
            i.geometriaAtividade = Objects.nonNull(inconsistencia.geometriaAtividade.id) ? inconsistencia.geometriaAtividade :null;
            i.sobreposicaoCaracterizacaoAtividade = Objects.nonNull(inconsistencia.sobreposicaoCaracterizacaoAtividade.id) ? inconsistencia.sobreposicaoCaracterizacaoAtividade : null;
            i.saveAnexos(inconsistencia.anexos);
            i.save();

            renderJSON(i,InconsistenciaSerializer.findInconsistencia);

        } else {
            Inconsistencia novaInconsistencia = null;
            if(inconsistencia.categoria.equals(Inconsistencia.Categoria.PROPRIEDADE)){

                novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo);

                novaInconsistencia.saveAnexos(inconsistencia.anexos);
                novaInconsistencia.save();
            }

            if(inconsistencia.categoria.equals(Inconsistencia.Categoria.ATIVIDADE)){

                novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo,inconsistencia.atividadeCaracterizacao, inconsistencia.geometriaAtividade);

                novaInconsistencia.saveAnexos(inconsistencia.anexos);
                novaInconsistencia.save();

            }
            if(inconsistencia.categoria.equals(Inconsistencia.Categoria.RESTRICAO)){

                novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo, inconsistencia.atividadeCaracterizacao, inconsistencia.sobreposicaoCaracterizacaoAtividade);

                novaInconsistencia.saveAnexos(inconsistencia.anexos);
                novaInconsistencia.save();
            }

            renderJSON(novaInconsistencia,InconsistenciaSerializer.findInconsistencia);

        }

    }

    public static void findInconsistencia(Inconsistencia inconsistencia) {

        Inconsistencia i = null;

        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.PROPRIEDADE)){
             i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria")
                    .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                    .setParameter("categoria",inconsistencia.categoria).first();

        }
        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.ATIVIDADE)){
             i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and atividadeCaracterizacao.id = :atividadeCaracterizacao and  geometriaAtividade.id = :geometriaAtividade")
                    .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                    .setParameter("atividadeCaracterizacao",inconsistencia.atividadeCaracterizacao.id)
                    .setParameter("geometriaAtividade",inconsistencia.geometriaAtividade.id)
                    .setParameter("categoria",inconsistencia.categoria).first();

        }
        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.RESTRICAO)){
             i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and atividadeCaracterizacao.id = :atividadeCaracterizacao and  sobreposicaoCaracterizacaoAtividade.id = :sobreposicaoCaracterizacaoAtividade")
                    .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                    .setParameter("atividadeCaracterizacao",inconsistencia.atividadeCaracterizacao.id)
                    .setParameter("sobreposicaoCaracterizacaoAtividade",inconsistencia.sobreposicaoCaracterizacaoAtividade.id)
                    .setParameter("categoria",inconsistencia.categoria).first();
        }

        renderJSON(i, InconsistenciaSerializer.findInconsistencia);
    }

    public static void excluirInconsistencia(Long id) {

        returnIfNull(id, "Long");

        Inconsistencia i = Inconsistencia.findById(id);

        i.deleteAnexos();

        i.delete();

        renderText(Mensagem.INCONSISTENCIA_EXCLUIDA_SUCESSO.getTexto());

    }

}

