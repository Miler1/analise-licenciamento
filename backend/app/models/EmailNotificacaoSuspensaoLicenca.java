package models;

import java.util.List;

import notifiers.Emails;

public class EmailNotificacaoSuspensaoLicenca extends EmailNotificacao {
	
	private Suspensao suspensao;
	
	public EmailNotificacaoSuspensaoLicenca(Suspensao suspensao, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.suspensao = suspensao;		
				
	}	

	@Override
	public void enviar() {

		Emails.notificarRequerenteSuspensaoLicenca(this.emailsDestinatarios, this.suspensao); 
	}

}
