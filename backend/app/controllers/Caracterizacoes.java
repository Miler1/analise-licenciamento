package controllers;

import java.util.List;

import models.LicencaAnalise;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.licenciamento.TipoCaracterizacaoAtividade.FiltroAtividade;
import security.Acao;
import serializers.AtividadesSerializer;
import serializers.LicencaAnaliseSerializer;

public class Caracterizacoes extends InternalController {
	

	public static void findAnalisesLicencas (Long id) {
		
		//verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);
		
		List<LicencaAnalise> analisesLicencas = LicencaAnalise.find("caracterizacao.id = ?", id).fetch();
		
		renderJSON(analisesLicencas, LicencaAnaliseSerializer.list);
		
		
	}
}
