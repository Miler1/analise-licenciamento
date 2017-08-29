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
			"processo.empreendimento.endereco.municipio.nome",
			"processo.empreendimento.endereco.municipio.estado.codigo",
			"analiseJuridica.id",
			"analiseTecnica.id",
			"analiseTecnica.pareceresTecnicosRestricoes",
			"analiseTecnica.analise",
			"analiseTecnica.analise.processo.empreendimento",
			"analiseTecnica.analise.processo.empreendimento.imovel",
			"analiseTecnica.analise.processo.empreendimento..municipio.limite"

		);

}
