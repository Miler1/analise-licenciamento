package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.PortalSegurancaException;
import exceptions.ValidacaoException;
import play.Play;
import play.libs.WS;
import play.mvc.Http;
import results.NotFound;
import results.Unauthorized;
import serializers.DateSerializer;
import utils.Mensagem;

import java.util.Date;

public class BaseExternalService {

	protected static Integer HTTP_STATUS_UNPROCESSABLE_ENTITY = 422;
	protected static final String portalSeguranca = Play.configuration.getProperty("auth.login.url");
	protected static final Gson gson;

	static {

		GsonBuilder builder = new GsonBuilder()
				.serializeNulls()
				.setLenient()
				.registerTypeAdapter(Date.class, new DateSerializer());

		gson = builder.create();

	}

	protected static void verifyResponse(WS.HttpResponse httpResponse) {

		if(httpResponse.success()) {

			return;

		}

		if(httpResponse.getStatus().equals(Http.StatusCode.UNAUTHORIZED)) {

			throw new Unauthorized();

		} else if(httpResponse.getStatus().equals(Http.StatusCode.NOT_FOUND)) {

			throw new NotFound();

		} else if(httpResponse.getStatus().equals(HTTP_STATUS_UNPROCESSABLE_ENTITY)) {

			throw new ValidacaoException(Mensagem.ERRO_VALIDACAO);

		} else {

			throw new PortalSegurancaException().userMessage("erro.padrao");

		}

	}

}
