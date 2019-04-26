package security.auth;

import com.google.gson.GsonBuilder;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import models.portalSeguranca.UsuarioLicenciamento;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.mvc.Http.Request;
import security.AuthService;


public class PortalSegurancaAuth implements AuthService {

	@Override
	public UsuarioLicenciamento autenticar(Request request) {

		String authorizatioCode = request.params.get("code");

		if(authorizatioCode == null) {

			Logger.error("Authorization code not found!");

			return null;

		}

		WSRequest authRequest = WS.url(Play.configuration.getProperty("auth.portalSeguranca.token.url"));
		authRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
		authRequest.setParameter("siglaModulo", Play.configuration.getProperty("application.code"));
		authRequest.setParameter("code", authorizatioCode);

		HttpResponse response = authRequest.post();

		if(!response.success()) {

			Logger.error("Auth request error! Status: " + response.getStatus());

			return null;

		}

		String json = response.getString();

		JWEObject jweObject = null;

		// Decrypt
		try {

			// Parse into JWE object again...
			jweObject = JWEObject.parse(json);

			jweObject.decrypt(new DirectDecrypter( Play.configuration.getProperty("application.secret").getBytes()));

		} catch (Exception e) {

			throw new RuntimeException(e);

		}

		// Get the plain text
		Payload payload = jweObject.getPayload();

		UsuarioLicenciamento usuario = new GsonBuilder().create().fromJson(payload.toString(), UsuarioLicenciamento.class);

		return usuario;
	}

}