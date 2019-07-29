package controllers;

import models.tramitacao.Condicao;
import org.hibernate.mapping.Array;
import security.Acao;
import serializers.CondicaoSerializer;
import utils.Configuracoes;

import java.util.Arrays;
import java.util.List;

public class Condicoes extends InternalController {
	
	public static void list() {

		verificarPermissao(Acao.CONSULTAR_PROCESSO);

		List<Condicao> condicoesVisiveis = Condicao.find("idCondicao in (:idsCondicoes)")
				.setParameter("idsCondicoes", Arrays.asList(Condicao.AGUARDANDO_ANALISE_GEO, Condicao.EM_ANALISE_GEO))
				.fetch();
		
		renderJSON(condicoesVisiveis, CondicaoSerializer.list);
	}

	public static void listManejo() {

		verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

		List<Condicao> condicoes = Condicao.find("idEtapa", Configuracoes.TRAMITACAO_ETAPA_MANEJO).fetch();

		renderJSON(condicoes, CondicaoSerializer.listManejo);
	}
}
