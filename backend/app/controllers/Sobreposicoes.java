package controllers;

import models.licenciamento.TipoSobreposicao;
import models.tramitacao.Condicao;
import security.Acao;
import serializers.CondicaoSerializer;
import serializers.TiposSobreposicaoSerializer;
import utils.Configuracoes;

import java.util.Arrays;
import java.util.List;

public class Sobreposicoes extends InternalController {
	
	public static void list() {

		List<TipoSobreposicao> tiposSobreposicao = TipoSobreposicao.findAll();

		renderJSON(tiposSobreposicao, TiposSobreposicaoSerializer.list);
	}

}
