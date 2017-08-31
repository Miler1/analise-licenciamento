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
			"processo.empreendimento.imovel.codigo",
			"processo.empreendimento.imovel.nome",
			"processo.empreendimento.imovel.municipio.nome",
			"processo.empreendimento.imovel.municipio.estado.codigo",
			"analiseJuridica.id",
			"analiseTecnica.id",
			"analiseTecnica.pareceresTecnicosRestricoes",
			"analiseTecnica.analise",
			"analiseTecnica.analise.processo.empreendimento",
			"analiseTecnica.analise.processo.empreendimento.imovel.codigo",
			"analiseTecnica.analise.processo.empreendimento.imovel.nome",
			"analiseTecnica.analise.processo.empreendimento.imovel.municipio.nome",
			"analiseTecnica.analise.processo.empreendimento.imovel.municipio.estado.codigo",
			"analiseTecnica.analise.processo.empreendimento.municipio.limite"

		);

}
