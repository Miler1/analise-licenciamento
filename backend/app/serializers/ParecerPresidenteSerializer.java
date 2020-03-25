package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerPresidenteSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
			"tipoResultadoAnalise.id",
			"parecer",
			"dataParecer"
	);

}
