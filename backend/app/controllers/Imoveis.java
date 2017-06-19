package controllers;

import play.libs.WS;
import utils.Configuracoes;

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
}
