package controllers;

import models.licenciamento.TipoCaracterizacaoAtividade;
import models.licenciamento.TipoCaracterizacaoAtividade.FiltroAtividade;
import serializers.AtividadesSerializer;

public class Atividades extends InternalController {

	public static void list() {
		
		//verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);
		
		FiltroAtividade filtro = new FiltroAtividade();
		filtro.licenciamentoSimplificado = getParamAsBoolean("licenciamentoSimplificado");
		filtro.dispensaLicenciamento = getParamAsBoolean("dispensaLicenciamento");
		
		renderJSON(TipoCaracterizacaoAtividade.findAtividades(filtro), AtividadesSerializer.listAtividadesSimplificado);
	}
	
}
