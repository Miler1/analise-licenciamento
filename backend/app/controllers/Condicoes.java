package controllers;

import models.tramitacao.Condicao;
import security.Acao;
import serializers.CondicaoSerializer;
import utils.Configuracoes;

import java.util.List;

public class Condicoes extends InternalController {
	
	public static void list() {

		verificarPermissao(Acao.CONSULTAR_PROCESSO);
		
		renderJSON(Condicao.findAll(), CondicaoSerializer.list);
	}

	public static void listManejo() {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		List<Condicao> condicoes = Condicao.find("idEtapa", Configuracoes.TRAMITACAO_ETAPA_MANEJO).fetch();

		renderJSON(condicoes, CondicaoSerializer.listManejo);
	}
}
