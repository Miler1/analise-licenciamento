package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class DesvinculoAnaliseTecnicaSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			"id",
			"justificativa",
			"respostaCoordenador",
			"aprovada",
			"dataSolicitacao",
			"dataResposta",
			"analiseGeo.id",
			"analiseTecnica.id",
			"analiseJuridica.id",
			"coordenador.id",
			"analistaGeo.id",
			"analistaGeo.pessoa.nome",
			"analistaTecnico.id",
			"analistaTecnico.pessoa.nome");

	public static JSONSerializer getPessoaAnalista = SerializerUtil.create(

	);

}
