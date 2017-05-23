package controllers;

import models.licenciamento.TipoCaracterizacaoAtividade;
import models.licenciamento.TipoCaracterizacaoAtividade.FiltroAtividade;

public class Tipologias extends InternalController {

	public static void list() {

//		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);
		
		FiltroAtividade filtro = new FiltroAtividade();
		filtro.licenciamentoSimplificado = getParamAsBoolean("licenciamentoSimplificado");
		filtro.dispensaLicenciamento = getParamAsBoolean("dispensaLicenciamento");
		
		renderJSON(TipoCaracterizacaoAtividade.findTipologias(filtro));
	}

}
