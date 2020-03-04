package controllers;

import models.*;
import security.Acao;
import serializers.ParecerAnalistaTecnicoSerializer;
import utils.Mensagem;

import javax.validation.ValidationException;
import java.util.Comparator;
import java.util.List;

public class PareceresAnalistasTecnico extends InternalController {

	public static void concluir(ParecerAnalistaTecnico parecerAnalistaTecnico) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		parecerAnalistaTecnico.finalizar(usuarioExecutor);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerAnalistaTecnico parecerAnalistaTecnico = ParecerAnalistaTecnico.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerAnalistaTecnico, ParecerAnalistaTecnicoSerializer.findByIdHistoricoTramitacao);

	}

	public static void getUltimoParecerAnaliseTecnica(Long id) {

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(id);

		renderJSON(analiseTecnica.pareceresAnalistaTecnico.stream().max(Comparator.comparing(ParecerAnalistaTecnico::getDataParecer)).orElseThrow(ValidationException::new), ParecerAnalistaTecnicoSerializer.findByIdHistoricoTramitacao);

	}

}
