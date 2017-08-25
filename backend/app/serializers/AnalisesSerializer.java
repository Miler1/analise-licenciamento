package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AnalisesSerializer {
	
	public static JSONSerializer findInfo = SerializerUtil.create(
			
			"id",
			"processo.id",
			"processo.numero",
			"processo.empreendimento.denominacao",
			"processo.empreendimento.pessoa.cpf",
			"processo.empreendimento.pessoa.cnpj",
			"analiseJuridica.id",
			"analiseTecnica.id"

		);

}
