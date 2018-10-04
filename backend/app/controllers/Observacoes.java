package controllers;

import models.manejoDigital.Observacao;
import security.Acao;
import serializers.ObeservacoesSerializer;
import utils.Mensagem;

public class Observacoes extends InternalController {

	public static void save(Observacao observacao) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(observacao);

		observacao.save();

		renderJSON(observacao, ObeservacoesSerializer.save);
	}

	public static void delete(String id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(id);

		Observacao observacao = Observacao.findById(id);

		observacao.delete();

		renderMensagem(Mensagem.OBSERVACAO_EXCLUIDA_SUCESSO);
	}
}
