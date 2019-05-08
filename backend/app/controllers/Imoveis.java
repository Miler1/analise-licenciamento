package controllers;

import exceptions.AppException;
import exceptions.ValidacaoException;
import models.licenciamento.Municipio;
import models.sicar.ImovelSicar;
import models.sicar.SicarWebService;
import play.libs.WS;
import security.Acao;
import utils.Configuracoes;
import utils.Mensagem;

import java.util.List;

public class Imoveis extends InternalController {

	public static void fichaImovelResumida(Integer idImovel, String tela){

		WS.WSRequest request = WS.url(Configuracoes.URL_SICAR_SITE + "imovel/" + idImovel +"/" + tela);

		WS.HttpResponse response = await(request.getAsync());

		renderBinary(response.getStream());
	}

	public static void fichaImovelResumidaDemonstrativo(String codigoImovel){

		WS.WSRequest request = WS.url(Configuracoes.URL_SICAR_SITE + "demonstrativo/imovel/" + codigoImovel);

		WS.HttpResponse response = await(request.getAsync());

		renderBinary(response.getStream());
	}

	public static void baixarDemonstrativo(String codigoImovel){

		WS.WSRequest request = WS.url(Configuracoes.URL_SICAR_SITE + "baixar/demonstrativo/imovel/" + codigoImovel);

		WS.HttpResponse demonstrativo = await(request.getAsync());

		response.setHeader("Content-Disposition", "attachment; filename=" +codigoImovel+".pdf");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/download");

		renderBinary(demonstrativo.getStream());
	}

	public static void baixarShapefile(String codigoImovel){

		WS.WSRequest request = WS.url(Configuracoes.URL_SICAR_SITE + "consulta/imoveis/baixarShapeFile/" + codigoImovel);

		WS.HttpResponse demonstrativo = await(request.getAsync());

		response.setHeader("Content-Disposition", "attachment; filename=shapefiles_" +codigoImovel+".zip");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/download");

		renderBinary(demonstrativo.getStream());
	}

	public static void getImovelByCodigo(String codigoImovel) {

		verificarPermissao(Acao.CADASTRAR_PROCESSO_MANEJO, Acao.VISUALIZAR_PROCESSO_MANEJO);

		notFoundIfNull(codigoImovel);

		SicarWebService sicarWebService = new SicarWebService();

		ImovelSicar imovelSicar = sicarWebService.getImovelByCodigo(codigoImovel);

		if (imovelSicar == null) {

			throw new AppException(Mensagem.IMOVEL_NAO_ENCONTRADO);
		}

		renderJSON(sicarWebService.getImovelById(imovelSicar.id.toString()));
	}

	public static void getImovelById(String id) {

		verificarPermissao(Acao.CADASTRAR_PROCESSO_MANEJO, Acao.VISUALIZAR_PROCESSO_MANEJO);

		notFoundIfNull(id);

		SicarWebService sicarWebService = new SicarWebService();

		renderJSON(sicarWebService.getImovelById(id));
	}

	public static void getImoveisSimplificadosPorCpfCnpj(String cpfCnpj, Long idMunicipio) {

		verificarPermissao(Acao.CADASTRAR_PROCESSO_MANEJO);

		if (cpfCnpj == null || cpfCnpj.isEmpty()) {

			throw new ValidacaoException(Mensagem.CPF_CNPJ_INVALIDO_NAO_INFORMADO);
		}

		if (idMunicipio == null) {

			throw new ValidacaoException(Mensagem.MUNICIPIO_INVALIDO_NAO_INFORMADO);
		}

		Municipio municipio = Municipio.findById(idMunicipio);

		List<ImovelSicar> imoveisEncontrados =
				new SicarWebService().getImoveisSimplificadosPorCpfCnpj(cpfCnpj, municipio);

		renderJSON(imoveisEncontrados);
	}
}
