package security;

import models.EntradaUnica.Usuario;

public interface AuthService {
	Usuario usuarioLogadoBySessionKey(String sessionKey);
}
