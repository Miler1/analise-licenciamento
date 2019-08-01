package controllers;

import models.licenciamento.Empreendimento;
import security.Acao;
import serializers.EmpreendimentoSerializer;
import services.IntegracaoEntradaUnicaService;

public class Empreendimentos extends InternalController {

	public static void buscaDadosGeoEmpreendimento(String cpfCnpj) {

		returnIfNull(cpfCnpj, "String");

		verificarPermissao(Acao.INICIAR_PARECER_GEO);

		renderJSON(Empreendimento.buscaDadosGeoEmpreendimento(cpfCnpj), EmpreendimentoSerializer.getDadosGeoEmpreendimento);

	}
}
