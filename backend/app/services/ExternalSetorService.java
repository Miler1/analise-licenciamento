package services;

import models.EntradaUnica.Setor;
import utils.WebService;

import java.util.Arrays;
import java.util.List;

public class ExternalSetorService extends BaseExternalService {

	protected static String URL_SETOR = "/external/setor/";

	public static Setor findBySigla(String siglaSetor) {

		WebService wsRequest = new WebService();
		return wsRequest.getJson(portalSeguranca + URL_SETOR + siglaSetor, Setor.class);
	}

	public static List<String> getSiglasSetoresByNivel(String siglaSetor, int nivel){

		WebService wsRequest = new WebService();
		return Arrays.asList(wsRequest.getJson(portalSeguranca + URL_SETOR + "nivel/" + siglaSetor + "/" + nivel , String[].class));
	}

}
