package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerAnalistaTecnicoSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
			"tipoResultadoAnalise.id",
			"parecer"
	);

	public static JSONSerializer findByIdNumeroProcesso = SerializerUtil.create(
			"parecer",
			"situacaoFundiaria",
			"analiseTemporal",
			"conclusao",
			"doProcesso",
			"daAnaliseTecnica",
			"daConclusao"
	);

}
