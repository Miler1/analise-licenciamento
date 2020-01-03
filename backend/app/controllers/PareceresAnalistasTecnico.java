package controllers;
import models.*;
import security.Acao;
import utils.Mensagem;

public class PareceresAnalistasTecnico extends InternalController {

	public static void concluir(ParecerAnalistaTecnico parecerAnalistaTecnico) {

		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		parecerAnalistaTecnico.finalizar(usuarioExecutor);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

}
