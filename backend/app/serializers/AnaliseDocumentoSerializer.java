package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AnaliseDocumentoSerializer {
	
	public static JSONSerializer analiseJuridica = SerializerUtil.create(
			
			"id",
			"validado",
			"parecer",
			"documento.id",
			"documento.nome",
			"documento.tipo.nome"
		);	

}
