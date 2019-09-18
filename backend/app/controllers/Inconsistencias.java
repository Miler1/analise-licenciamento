package controllers;

import exceptions.ValidacaoException;
import models.Inconsistencia;
import serializers.InconsistenciaSerializer;
import utils.Mensagem;

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
            i.caracterizacao = Objects.nonNull(inconsistencia.caracterizacao) ? inconsistencia.caracterizacao : null;
            i.sobreposicaoCaracterizacaoEmpreendimento = Objects.nonNull(inconsistencia.sobreposicaoCaracterizacaoEmpreendimento.id) ? inconsistencia.sobreposicaoCaracterizacaoEmpreendimento : null;
            i.saveAnexos(inconsistencia.anexos);
            i.save();

            renderJSON(i, InconsistenciaSerializer.findInconsistencia);

        } else {
            Inconsistencia novaInconsistencia = null;
            if(inconsistencia.categoria.equals(Inconsistencia.Categoria.PROPRIEDADE)){

                novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo);

                novaInconsistencia.saveAnexos(inconsistencia.anexos);
                novaInconsistencia.save();
            }

            if(inconsistencia.categoria.equals(Inconsistencia.Categoria.ATIVIDADE)){

                novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo, inconsistencia.caracterizacao);

                novaInconsistencia.saveAnexos(inconsistencia.anexos);
                novaInconsistencia.save();

            }
            if(inconsistencia.categoria.equals(Inconsistencia.Categoria.RESTRICAO)){

                novaInconsistencia = new Inconsistencia(inconsistencia.descricaoInconsistencia, inconsistencia.tipoInconsistencia, inconsistencia.categoria, inconsistencia.analiseGeo, inconsistencia.caracterizacao, inconsistencia.sobreposicaoCaracterizacaoEmpreendimento);

                novaInconsistencia.saveAnexos(inconsistencia.anexos);
                novaInconsistencia.save();
            }

            renderJSON(novaInconsistencia,InconsistenciaSerializer.findInconsistencia);

        }

    }

    public static void findInconsistencia(Inconsistencia inconsistencia) {

        Inconsistencia i = null;

        if(inconsistencia.categoria == null || (inconsistencia.categoria.equals(Inconsistencia.Categoria.RESTRICAO) && (inconsistencia.caracterizacao != null && inconsistencia.caracterizacao.id == null))) {
            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
        }

        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.PROPRIEDADE)){
             i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria")
                     .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                     .setParameter("categoria",inconsistencia.categoria).first();
        }

        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.ATIVIDADE)){
             i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao")
                     .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                     .setParameter("caracterizacao",inconsistencia.caracterizacao.id)
                     .setParameter("categoria",inconsistencia.categoria).first();
        }

        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.RESTRICAO)){
             i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao and sobreposicaoCaracterizacaoEmpreendimento.id = :sobreposicaoCaracterizacaoEmpreendimento")
                     .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                     .setParameter("caracterizacao",inconsistencia.caracterizacao.id)
                     .setParameter("sobreposicaoCaracterizacaoEmpreendimento",inconsistencia.sobreposicaoCaracterizacaoEmpreendimento.id)
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

