package security;

import utils.Configuracoes;

public class AuthServiceFactory {

	private static final String SERVICES_PACKAGE = "security.auth.";
	
	private static AuthService service = null;

	public AuthService getInstance() {

		if (service == null) {
			service = createService();
		}

		return service;
	}

	public static <T extends AuthService> boolean usingImplementation(Class<T> clazz) {
	
		return getServiceClass().equals(clazz);
	}
	
	private static Class getServiceClass() {
		
		try {
			return Class.forName(SERVICES_PACKAGE + Configuracoes.AUTH_SERVICE);
			
		} catch (ClassNotFoundException e) {

			throw new RuntimeException(e);
		}
	}
	
	private AuthService createService() {

		if (Configuracoes.AUTH_SERVICE == null) {
			
			throw new RuntimeException("Implementação da AuthenticationService não encontrada, "
					+ "favor checar a configuração.");
		}
		
		try {
			
			return (AuthService) getServiceClass().newInstance();

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}
}