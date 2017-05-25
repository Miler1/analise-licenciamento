package serializers;

import java.util.Date;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ProcessoSerializer {
	
	public static JSONSerializer list = SerializerUtil.create(
			"id",
			"numero",
			"empreendimento.pessoa.cpf",
			"empreendimento.nome");
}
