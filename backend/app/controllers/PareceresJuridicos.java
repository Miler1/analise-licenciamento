package controllers;

import models.*;
import models.licenciamento.SobreposicaoCaracterizacaoEmpreendimento;
import models.manejoDigital.analise.analiseShape.Sobreposicao;
import serializers.ComunicadoSerializer;
import serializers.ParecerJuridicoSerializer;
import utils.Mensagem;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PareceresJuridicos extends GenericController{


    public static void salvarParecerJuridico(ParecerJuridico parecerJuridico) throws Exception {

        returnIfNull(parecerJuridico, "ParecerJuridico");

        parecerJuridico.finalizar(parecerJuridico);

        renderMensagem(Mensagem.ANALISE_JURIDICA_VALIDADA);

    }


    public static void findParecerJuridico(Long id) {

        ParecerJuridico parecerJuridicoBanco =  ParecerJuridico.findById(id);

        renderJSON(parecerJuridicoBanco, ParecerJuridicoSerializer.findParecerJuridico);

    }

    public static void findPareceres(Long id) {

        List<ParecerJuridico> parecerJuridicoBanco =  ParecerJuridico.findAll();

        renderJSON(parecerJuridicoBanco, ParecerJuridicoSerializer.findParecerJuridico);

    }


    public static void getParecerJuridicoByAnaliseTecnica(Long idAnaliseTecnica) {

        ParecerJuridico parecerFinal = ParecerJuridico.getParecerJuridicoByAnaliseTecnica(idAnaliseTecnica);

        renderJSON(parecerFinal, ParecerJuridicoSerializer.findParecerJuridico);

    }

}

