package exceptions;

import play.data.validation.Validation;
import utils.Mensagem;

public class ValidacaoException extends AppException {

	public ValidacaoException(Mensagem mensagem) {
		
		super(mensagem);
	}
	
	public ValidacaoException(Mensagem mensagem, Object ... msgArgs) {
		
		super(mensagem, msgArgs);
	}

	public ValidacaoException(Mensagem mensagem, Validation validation) {
		
		this(mensagem);
	}

}
