package controllers;

import models.DispensaLicenciamentoCancelada;
import models.LicencaCancelada;
import models.UsuarioAnalise;
import security.Acao;
import utils.Mensagem;

public class LicencaCanceladas extends InternalController {
	
	public static void cancelarLicenca(LicencaCancelada licencaCancelada) {
		
		verificarPermissao(Acao.CANCELAR_LICENCA_EMITIDA);
		
		returnIfNull(licencaCancelada, "LicencaCancelada");

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();
		
		licencaCancelada.cancelarLicenca(usuarioExecutor);
		
		renderMensagem(Mensagem.LICENCA_CANCELADA_SUCESSO);
		
	}

	public static void cancelarDispensa(DispensaLicenciamentoCancelada dispensaLicenciamentoCancelada) {

		verificarPermissao(Acao.CANCELAR_LICENCA_EMITIDA);

		returnIfNull(dispensaLicenciamentoCancelada, "LicencaCancelada");

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		dispensaLicenciamentoCancelada.cancelarDispensa(usuarioExecutor);

		renderMensagem(Mensagem.LICENCA_CANCELADA_SUCESSO);
	}

}
