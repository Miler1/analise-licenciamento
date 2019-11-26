package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerAnalistaGeoSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
			"tipoResultadoAnalise.id",
			"parecer"
	);

}
