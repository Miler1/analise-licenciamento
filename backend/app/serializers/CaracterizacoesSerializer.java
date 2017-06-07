package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class CaracterizacoesSerializer {

	public static JSONSerializer findDocumentos = SerializerUtil.create(
			"solicitacoesDocumento.documento.id",
			"solicitacoesDocumento.documento.nome",
			"solicitacoesDocumento.documento.dataCadastro",
			"solicitacoesDocumento.id",
			"solicitacoesDocumento.obrigatorio",
			"solicitacoesDocumento.tipoDocumento.id",
			"solicitacoesDocumento.tipoDocumento.nome",
			"solicitacoesDocumento.tipoDocumento.caminhoModelo");
}
