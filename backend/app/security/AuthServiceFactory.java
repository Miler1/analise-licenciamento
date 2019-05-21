package security;

import utils.Configuracoes;

public class AuthServiceFactory {

	private static final String SERVICES_PACKAGE = "security.";
	
	private static AuthService service = null;

	public AuthService getInstance() {

		if (service == null) {
			service = createService();
		}

		return service;
	}
	
	private AuthService createService() {

		if (Configuracoes.AUTH_SERVICE == null) {
			
			throw new RuntimeException("Implementação da AuthenticationService não encontrada, "
					+ "favor checar a configuração.");
		}
		
		try {
			
			return (AuthService) Class.forName(SERVICES_PACKAGE + Configuracoes.AUTH_SERVICE).newInstance();

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}
}