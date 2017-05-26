package controllers;

import models.licenciamento.TipoCaracterizacaoAtividade;
import models.licenciamento.TipoCaracterizacaoAtividade.FiltroAtividade;
import security.Acao;

public class Tipologias extends InternalController {

	public static void list() {

		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		FiltroAtividade filtro = new FiltroAtividade();
		filtro.licenciamentoSimplificado = getParamAsBoolean("licenciamentoSimplificado");
		filtro.dispensaLicenciamento = getParamAsBoolean("dispensaLicenciamento");
		
		renderJSON(TipoCaracterizacaoAtividade.findTipologias(filtro));
	}

}
