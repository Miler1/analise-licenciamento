package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ProcessoManejoSerializer {

	public static JSONSerializer save = SerializerUtil.create(
			"id"
	);

	public static JSONSerializer findById = SerializerUtil.create(
			"id",
			"numeroProcesso",
			"cpfCnpj",
			"denominacaoEmpreendimentoSimlam",
			"nomeMunicipioSimlam",
			"nomeCondicao",
			"analiseManejo.id"
	);

	public static JSONSerializer iniciarAnalise = SerializerUtil.create(
			"id",
			"analiseManejo.id"
	);
}
