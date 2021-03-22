package controllers;

import models.Analise;
import models.ParecerDiretorTecnico;
import models.UsuarioAnalise;
import serializers.ParecerDiretorSerializer;
import utils.Mensagem;

public class PareceresDiretores extends InternalController {

    public static void findParecerByAnalise(Long id) {

        ParecerDiretorTecnico parecerDiretorTecnico = ParecerDiretorTecnico.find("id_analise", id).first();

        renderJSON(parecerDiretorTecnico, ParecerDiretorSerializer.findByAnalise);

    }

    public static void concluirParecerDiretorTecnico(ParecerDiretorTecnico parecerDiretorTecnico) {

        returnIfNull(parecerDiretorTecnico, "ParecerDiretorTecnico");

        Analise analise = Analise.findById(parecerDiretorTecnico.analise.id);

        UsuarioAnalise diretor = getUsuarioSessao();

        parecerDiretorTecnico.finalizar(analise, diretor);

        renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

    }

    public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

        ParecerDiretorTecnico parecerDiretorTecnico = ParecerDiretorTecnico.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

        renderJSON(parecerDiretorTecnico, ParecerDiretorSerializer.findByAnalise);

    }

}
