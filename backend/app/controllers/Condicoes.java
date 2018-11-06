package controllers;

import models.licenciamento.TipoCaracterizacaoAtividade;
import models.licenciamento.TipoCaracterizacaoAtividade.FiltroAtividade;
import models.tramitacao.Condicao;
import security.Acao;
import serializers.CondicaoSerializer;
import utils.Configuracoes;

public class Condicoes extends InternalController {
	
	public static void list() {

		verificarPermissao(Acao.CONSULTAR_PROCESSO);
		
		renderJSON(Condicao.findAll(), CondicaoSerializer.list);
	}

	public static void listManejo() {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		renderJSON(Condicao.find("idEtapa", Configuracoes.TRAMITACAO_ETAPA_MANEJO).fetch() ,CondicaoSerializer.list);
	}
}
