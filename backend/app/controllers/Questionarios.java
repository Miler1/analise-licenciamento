package controllers;

import models.Processo;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Questionario3;
import security.Acao;

import serializers.Questionario3Serializer;

import java.util.List;

public class Questionarios extends InternalController {

	public static void findQuestionario(Long idProcesso) {

		verificarPermissao(Acao.VISUALIZAR_QUESTIONARIO);

		Processo processo = Processo.findById(idProcesso);

		renderJSON(processo.caracterizacao.questionario3, Questionario3Serializer.findInfo);

	}

}
