package exceptions;

import play.i18n.Messages;
import utils.Mensagem;

public class AppException extends RuntimeException {

	private Mensagem mensagem;
	private Object [] msgArgs;

	
	public AppException(Mensagem mensagem, Object ... msgArgs) {
		
		this.mensagem = mensagem;
		this.msgArgs = msgArgs;
	}
	
	public AppException(Throwable throwable, Mensagem mensagem, Object ... msgArgs) {
		
		super(throwable);
		this.mensagem = mensagem;
		this.msgArgs = msgArgs;
	}
	
	public String getTextoMensagem () {
		
		return this.mensagem.getTexto(this.msgArgs);
	}
}
