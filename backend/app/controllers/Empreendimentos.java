package controllers;

import br.ufla.lemaf.beans.historico.EmpreendimentoSobreposicao;
import br.ufla.lemaf.beans.historico.EmpreendimentoSobreposicaoVO;
import models.licenciamento.Empreendimento;
import security.Acao;
import serializers.EmpreendimentoSerializer;
import services.IntegracaoEntradaUnicaService;

public class Empreendimentos extends InternalController {

	public static void buscaDadosGeoEmpreendimento(String cpfCnpj) {

		returnIfNull(cpfCnpj, "String");

//		TODO: quando criar as permissões do coordenador, adicionar aqui e descomentar.
//		verificarPermissao(Acao.INICIAR_PARECER_GEO);

		renderJSON(Empreendimento.buscaDadosGeoEmpreendimento(cpfCnpj), EmpreendimentoSerializer.getDadosGeoEmpreendimento);

	}

	public static void sobreposicoes(String cpfCnpj){

		IntegracaoEntradaUnicaService integracao = new IntegracaoEntradaUnicaService();
		EmpreendimentoSobreposicao intersects = integracao.intersects(cpfCnpj);
		renderJSON(intersects);

	}
}
