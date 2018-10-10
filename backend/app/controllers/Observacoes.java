package controllers;

import models.manejoDigital.Observacao;
import security.Acao;
import serializers.ObservacoesSerializer;
import utils.Mensagem;

public class Observacoes extends InternalController {

	public static void save(Observacao observacao) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(observacao);

		observacao.save();

		renderJSON(observacao, ObservacoesSerializer.save);
	}

	public static void delete(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(id);

		Observacao observacao = Observacao.findById(id);

		observacao.delete();

		renderMensagem(Mensagem.OBSERVACAO_EXCLUIDA_SUCESSO);
	}
}
