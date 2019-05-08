package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class DLASerializer {
	
public static JSONSerializer list = SerializerUtil.create(
			
			"id",
			"caracterizacao.empreendimento.denominacao",
			"caracterizacao.empreendimento.pessoa.cpf",
			"caracterizacao.empreendimento.pessoa.cnpj",
			"caracterizacao.empreendimento.municipio.nome",
			"caracterizacao.empreendimento.municipio.estado.codigo",
			"caracterizacao.tipoLicenca.nome",			
			"caracterizacao.atividadeCaracterizacao.porteEmpreendimento.codigo",
			"caracterizacao.numeroProcesso",
			"caracterizacao.atividadeCaracterizacao.atividade.potencialPoluidor.codigo",
			"dataCadastro");

}
