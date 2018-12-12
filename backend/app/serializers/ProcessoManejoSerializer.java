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
			"empreendimento.cpfCnpj",
			"empreendimento.denominacao",
			"empreendimento.municipio.nome",
			"nomeCondicao",
			"analiseTecnica.id",
			"revisaoSolicitada"
	);

	public static JSONSerializer findCompletoById = SerializerUtil.create(
			"id",
			"numeroProcesso",
			"atividadeManejo.tipologiaManejo.nome",
			"empreendimento.cpfCnpj",
			"empreendimento.denominacao",
			"empreendimento.municipio.nome",
			"empreendimento.municipio.estado.nome",
			"atividadeManejo.nome",
			"tipoLicenca.nome",
			"atividadeManejo.nome",
			"empreendimento.imovel.registroCar",
			"empreendimento.imovel.nome",
			"empreendimento.imovel.municipio.nome",
			"empreendimento.imovel.municipio.estado.nome",
			"empreendimento.imovel.descricaoAcesso",
			"empreendimento.imovel.status",
			"justificativaIndeferimento"
	);

	public static JSONSerializer iniciarAnalise = SerializerUtil.create(
			"id",
			"analiseTecnica.id"
	);
}
