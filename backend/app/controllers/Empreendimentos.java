package controllers;

import models.licenciamento.Empreendimento;
import security.Acao;
import serializers.EmpreendimentoSerializer;
import services.IntegracaoEntradaUnicaService;

public class Empreendimentos extends InternalController {

	public static void buscaDadosGeoEmpreendimento(String cpfCnpj) {

		returnIfNull(cpfCnpj, "String");

		verificarPermissao(Acao.INICIAR_PARECER_GEO);

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		main.java.br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(cpfCnpj);

		Empreendimento empreendimento = Empreendimento.buscaEmpreendimentoByCpfCnpj(cpfCnpj);

		renderJSON(empreendimento.buscaDadosGeoEmpreendimento(empreendimentoEU.localizacao.geometria), EmpreendimentoSerializer.getDadosGeoEmpreendimento);

	}
	
}
