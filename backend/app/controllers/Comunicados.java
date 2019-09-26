package controllers;

import models.Comunicado;
import models.licenciamento.SobreposicaoCaracterizacaoEmpreendimento;
import serializers.ComunicadoSerializer;

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

    public static void findByIdSobreposicaoEmpreendimento(Long id) {

        Comunicado comunicado = Comunicado.find("sobreposicaoCaracterizacaoEmpreendimento.id", id).first();

        renderJSON(comunicado, ComunicadoSerializer.findComunicado);

    }

    public static void listaComunicadosByIdAnaliseGeo(Long id) {

        List<Comunicado> comunicados = Comunicado.findByAnaliseGeo(id);
        renderJSON(comunicados, ComunicadoSerializer.findComunicado);

    }

}

