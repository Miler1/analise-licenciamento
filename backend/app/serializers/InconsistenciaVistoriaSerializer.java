package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class InconsistenciaVistoriaSerializer {

	public static JSONSerializer find = SerializerUtil.create(
			"id",
			"descricaoInconsistencia",
			"tipoInconsistencia",
			"anexos.id",
			"anexos.nomeDoArquivo");

}
