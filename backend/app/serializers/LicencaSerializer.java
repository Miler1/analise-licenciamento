package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class LicencaSerializer {
	
public static JSONSerializer listSuspensao = SerializerUtil.create(
			
			"id",
			"dataValidade",
			"observacao",
			"caracterizacao.numero",
			"caracterizacao.atividadeCaracterizacaoMaiorPotencialPoluidorEPorte.porteEmpreendimento.nome",
			"caracterizacao.atividadeCaracterizacaoMaiorPotencialPoluidorEPorte.atividade.potencialPoluidor.nome",
			"caracterizacao.empreendimento.denominacao",
			"caracterizacao.empreendimento.pessoa.cpf",
			"caracterizacao.empreendimento.pessoa.cnpj",
			"caracterizacao.empreendimento.municipio.nome",
			"caracterizacao.empreendimento.municipio.estado.codigo",
			"caracterizacao.tipoLicenca.nome",			
			"caracterizacao.atividadeCaracterizacao.porteEmpreendimento.codigo",
			"caracterizacao.atividadeCaracterizacao.atividade.potencialPoluidor.codigo",
			"licencaAnalise.condicionantes.texto",
			"licencaAnalise.condicionantes.prazo",
			"licencaAnalise.condicionantes.ordem",
			"licencaAnalise.recomendacoes.texto",
			"licencaAnalise.validade",
			"licencaAnalise.observacao",
			"dataCadastro",
			"licencaAnalise.caracterizacao.numero",
			"licencaAnalise.recomendacoes.ordem");

}
