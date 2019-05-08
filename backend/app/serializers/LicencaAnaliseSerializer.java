package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class LicencaAnaliseSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			
			"id",
			"validade",
			"observacao",
			"emitir",
			"caracterizacao.tipoLicenca.nome",			
			"caracterizacao.atividadeCaracterizacao.porteEmpreendimento.codigo",
			"caracterizacao.atividadeCaracterizacao.atividade.potencialPoluidor.codigo",
			"condicionantes.id",
			"condicionantes.texto",
			"condicionantes.prazo",
			"condicionantes.ordem",
			"recomendacoes.id",
			"recomendacoes.texto",
			"recomendacoes.ordem");

}
