package controllers;

import models.*;
import models.licenciamento.SobreposicaoCaracterizacaoEmpreendimento;
import models.manejoDigital.analise.analiseShape.Sobreposicao;
import serializers.ComunicadoSerializer;
import serializers.ParecerJuridicoSerializer;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PareceresJuridicos extends GenericController{


    public static void salvarParecerJuridico(ParecerJuridico parecerJuridico) {

        if(parecerJuridico.id != null){
            ParecerJuridico parecerJuridicoBanco = ParecerJuridico.findById(parecerJuridico.id);
            parecerJuridicoBanco.parecer = parecerJuridico.parecer;
            parecerJuridicoBanco.resolvido =true;
            parecerJuridicoBanco.saveAnexos(parecerJuridico.anexos);
            parecerJuridicoBanco.dataResposta = new Date();
            parecerJuridicoBanco.save();
            renderJSON(true);
        }

        renderJSON(false);
    }


    public static void findParecerJuridico(Long id) {

        ParecerJuridico parecerJuridicoBanco =  ParecerJuridico.findById(id);

        renderJSON(parecerJuridicoBanco, ParecerJuridicoSerializer.findParecerJuridico);

    }

}

