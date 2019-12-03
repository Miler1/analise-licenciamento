package controllers;

import models.Analise;
import models.AnaliseGeo;
import models.Comunicado;
import models.licenciamento.SobreposicaoCaracterizacaoEmpreendimento;
import models.manejoDigital.analise.analiseShape.Sobreposicao;
import serializers.ComunicadoSerializer;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Comunicados extends GenericController{


    public static void salvarParecerOrgao(Comunicado comunicado) {

        if(comunicado.parecerOrgao == null || comunicado.parecerOrgao.equals("")){
            renderJSON(false);
        }
        if(comunicado.id != null){
            Comunicado comunicadoBanco = Comunicado.findById(comunicado.id);
            comunicadoBanco.parecerOrgao = comunicado.parecerOrgao;
            comunicadoBanco.resolvido =true;
            comunicadoBanco.saveAnexos(comunicado.anexos);
            comunicadoBanco.dataResposta = new Date();
            comunicadoBanco.save();
            renderJSON(true);
        }

        renderJSON(false);
    }


    public static void findComunicado(Long id) {

        Comunicado comunicadoBanco =  Comunicado.findById(id);
        comunicadoBanco.valido =comunicadoBanco.isValido();
        renderJSON(comunicadoBanco, ComunicadoSerializer.findComunicado);

    }

    public static void findComunicadoByIdAnaliseGeo(Long idAnaliseGeo) {

        List<Comunicado> comunicados = Comunicado.findByAnaliseGeo(idAnaliseGeo);

        Comunicado comunicadoFinal = comunicados.stream().max( Comparator.comparing( comunicado -> comunicado.id )).get();

        renderJSON(comunicadoFinal, ComunicadoSerializer.findComunicado);

    }

    public static void listaComunicadosByIdAnaliseGeo(Long id) {

        List<Comunicado> comunicados = Comunicado.findByAnaliseGeo(id);
        renderJSON(comunicados, ComunicadoSerializer.findComunicado);

    }

}

