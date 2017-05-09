package exceptions;

import utils.Mensagem;

public class PermissaoNegadaException extends AppException {

	public PermissaoNegadaException() {
		
		super(Mensagem.PERMISSAO_NEGADA);
	}
	
	public PermissaoNegadaException(Mensagem msg) {
		
		super(msg);
	}
}
