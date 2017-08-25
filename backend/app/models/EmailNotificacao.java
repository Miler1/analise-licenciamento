package models;

import java.util.List;

public abstract class EmailNotificacao {
	
	protected List<String> emailsDestinatarios;
	
	public EmailNotificacao(List<String> emailsDestinatarios) {
		this.emailsDestinatarios = emailsDestinatarios;
	}
	
	abstract void enviar();
	

}
