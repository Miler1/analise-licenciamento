package controllers;

import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.licenciamento.TipoCaracterizacaoAtividade.FiltroAtividade;
import security.Acao;
import serializers.AtividadesSerializer;

public class Caracterizacoes extends InternalController {
	

	public static void findTiposLicencas (Long id) {
		
		Caracterizacao caracterizacao = Caracterizacao.findById(id);
		
		renderJSON(caracterizacao.tiposLicencaEmAndamento);
		
	}
}
