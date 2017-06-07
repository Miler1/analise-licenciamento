package controllers;

import models.licenciamento.TipoCaracterizacaoAtividade;
import models.licenciamento.TipoCaracterizacaoAtividade.FiltroAtividade;
import models.tramitacao.Condicao;
import security.Acao;
import serializers.CondicaoSerializer;

public class Condicoes extends InternalController {
	
	public static void list() {

		verificarPermissao(Acao.CONSULTAR_PROCESSO);
		
		renderJSON(Condicao.findAll(), CondicaoSerializer.list);
	}	
}
