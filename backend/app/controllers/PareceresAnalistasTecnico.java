package controllers;
import models.*;
import security.Acao;
import serializers.ParecerAnalistaGeoSerializer;
import utils.Mensagem;

import java.util.Comparator;
import java.util.List;

public class PareceresAnalistasTecnico extends InternalController {

    public static void concluir(ParecerAnalistaTecnico parecerAnalistaTecnico) throws Exception {

        verificarPermissao(Acao.INICIAR_PARECER_TECNICO);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        parecerAnalistaTecnico.finalizar(usuarioExecutor);

        renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

    }

}