package controllers;

import exceptions.ValidacaoException;
import models.Inconsistencia;
import models.licenciamento.AtividadeCaracterizacao;
import serializers.InconsistenciaSerializer;
import utils.Mensagem;

import java.util.Objects;

public class Inconsistencias extends GenericController{


    public static void salvarInconsistencia(Inconsistencia inconsistencia) {

        renderJSON(inconsistencia.salvaInconsistencia(),InconsistenciaSerializer.findInconsistencia);

    }

    public static void findById(Long id) {

        Inconsistencia inconsistencia = Inconsistencia.findById(id);

        renderJSON(inconsistencia, InconsistenciaSerializer.findInconsistencia);

    }

    public static void findInconsistencia(Inconsistencia inconsistencia) {

        Inconsistencia i = null;

        if(inconsistencia.categoria == null || (inconsistencia.categoria.equals(Inconsistencia.Categoria.RESTRICAO) && (inconsistencia.sobreposicaoCaracterizacaoEmpreendimento != null && inconsistencia.sobreposicaoCaracterizacaoEmpreendimento.id == null))) {
            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
        }

        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.PROPRIEDADE)){
             i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria")
                     .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                     .setParameter("categoria",inconsistencia.categoria).first();
        }

        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.ATIVIDADE)) {
             i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao")
                     .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                     .setParameter("caracterizacao",inconsistencia.caracterizacao.id)
                     .setParameter("categoria",inconsistencia.categoria).first();
        }

        if(inconsistencia.categoria.equals(Inconsistencia.Categoria.RESTRICAO)) {

            if(inconsistencia.sobreposicaoCaracterizacaoAtividade != null) {

                i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao and sobreposicaoCaracterizacaoAtividade.id = :sobreposicaoCaracterizacaoAtividade")
                        .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                        .setParameter("caracterizacao",inconsistencia.caracterizacao.id)
                        .setParameter("categoria",inconsistencia.categoria)
                        .setParameter("sobreposicaoCaracterizacaoAtividade", inconsistencia.sobreposicaoCaracterizacaoAtividade.id).first();

            } else if(inconsistencia.sobreposicaoCaracterizacaoEmpreendimento != null) {

                i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao and sobreposicaoCaracterizacaoEmpreendimento.id = :sobreposicaoCaracterizacaoEmpreendimento")
                        .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                        .setParameter("caracterizacao",inconsistencia.caracterizacao.id)
                        .setParameter("categoria",inconsistencia.categoria)
                        .setParameter("sobreposicaoCaracterizacaoEmpreendimento", inconsistencia.sobreposicaoCaracterizacaoEmpreendimento.id).first();

            } else if(inconsistencia.sobreposicaoCaracterizacaoComplexo != null) {

                i = Inconsistencia.find("analiseGeo.id = :idAnaliseGeo and categoria = :categoria and caracterizacao.id = :caracterizacao and sobreposicaoCaracterizacaoComplexo.id = :sobreposicaoCaracterizacaoComplexo")
                        .setParameter("idAnaliseGeo",inconsistencia.analiseGeo.id)
                        .setParameter("caracterizacao",inconsistencia.caracterizacao.id)
                        .setParameter("categoria",inconsistencia.categoria)
                        .setParameter("sobreposicaoCaracterizacaoComplexo", inconsistencia.sobreposicaoCaracterizacaoComplexo.id).first();

            }

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

