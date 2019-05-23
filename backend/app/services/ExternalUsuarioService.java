package services;

import models.UsuarioAnalise;
import utils.WebService;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ExternalUsuarioService extends BaseExternalService {

	protected static String FIND_USUARIOS_BY_PERFIL = "/external/usuario/perfil/";

	public static List<UsuarioAnalise> findUsuariosByPerfil(String codigoPerfil) {

		return response(codigoPerfil, null);
	}

	public static List<UsuarioAnalise> findUsuariosByPerfilAndSetor(String codigoPerfil, String siglaSetor) {

		return response(codigoPerfil, siglaSetor);
	}

	public static List<UsuarioAnalise> findUsuariosByPerfilAndSetores(String codigoPerfil, List<String> siglaSetores) {

		StringBuilder stringBuilder = new StringBuilder();

		for(String sigla: siglaSetores){
			stringBuilder.append(',');
			stringBuilder.append(sigla);
		}

		return response(codigoPerfil, stringBuilder.toString());
	}

	public static List<UsuarioAnalise> response(String codigoPerfil, @Nullable String siglaSetores){

		String separator = "/";

		if(siglaSetores == null){
			separator = "";
			siglaSetores = "";
		}

		WebService wsRequest = new WebService();

		return Arrays.asList(wsRequest.getJson(portalSeguranca + FIND_USUARIOS_BY_PERFIL + codigoPerfil + separator + siglaSetores, UsuarioAnalise[].class));

	}

}
