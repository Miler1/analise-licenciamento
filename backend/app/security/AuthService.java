package security;

import models.portalSeguranca.UsuarioLicenciamento;
import play.mvc.Http;

public interface AuthService {

	UsuarioLicenciamento autenticar(Http.Request request);
}
