package security;

import play.mvc.Http;
import play.mvc.Scope;

public interface AuthService {

	UsuarioSessao autenticar(Http.Request request);
}
