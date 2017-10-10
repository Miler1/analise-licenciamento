package models;

import java.util.List;

import notifiers.Emails;

public class EmailNotificacaoCancelamentoDla extends EmailNotificacao {
	
	private DlaCancelada dlaCancelada;
	
	public EmailNotificacaoCancelamentoDla(DlaCancelada dlaCancelada, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.dlaCancelada = dlaCancelada;
		
	}
	
	@Override
	public void enviar() {

		Emails.notificarRequerenteCancelamentoDla(this.emailsDestinatarios, this.dlaCancelada); 
	}

}
