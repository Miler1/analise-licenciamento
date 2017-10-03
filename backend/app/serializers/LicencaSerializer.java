package serializers;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;

public class LicencaSerializer {
	
public static JSONSerializer listSuspensao = SerializerUtil.create(
			
			"id",
			"dataValidade",
			"observacao",
			"caracterizacao.empreendimento.denominacao",
			"caracterizacao.empreendimento.responsaveis.pessoa.cpf",
			"caracterizacao.empreendimento.endereco.municipio.nome",
			"caracterizacao.tipoLicenca.nome",			
			"caracterizacao.atividadeCaracterizacao.porteEmpreendimento.codigo",
			"caracterizacao.atividadeCaracterizacao.atividade.potencialPoluidor.codigo",
			"licencaAnalise.condicionantes.texto",
			"licencaAnalise.condicionantes.prazo",
			"licencaAnalise.condicionantes.ordem",
			"licencaAnalise.recomendacoes.texto",
			"licencaAnalise.validade",
			"dataCadastro",
			"licencaAnalise.caracterizacao.numeroProcesso",
			"licencaAnalise.recomendacoes.ordem");

}
