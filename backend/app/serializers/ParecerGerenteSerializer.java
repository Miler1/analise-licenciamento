package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerGerenteSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
			"tipoResultadoAnalise.id",
			"parecer"
	);

}