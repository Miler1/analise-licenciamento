package security;

import models.portalSeguranca.UsuarioLicenciamento;
import play.mvc.Http;
import play.mvc.Scope;

public interface AuthService {

	UsuarioLicenciamento autenticar(Http.Request request);
}
