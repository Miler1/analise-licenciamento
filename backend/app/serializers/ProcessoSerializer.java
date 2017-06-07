package serializers;

import java.util.Date;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ProcessoSerializer {
	
	public static JSONSerializer list = SerializerUtil.create(
			"id",
			"numero",
			"empreendimento.municipio.nome",
			"empreendimento.municipio.estado.codigo",
			"empreendimento.pessoa.cpf",
			"empreendimento.denominacao");
}
