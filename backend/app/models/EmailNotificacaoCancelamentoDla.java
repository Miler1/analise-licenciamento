package models;

import java.util.List;
import java.util.concurrent.ExecutionException;

import exceptions.AppException;
import models.ReenvioEmail.TipoEmail;
import notifiers.Emails;

public class EmailNotificacaoCancelamentoDla extends EmailNotificacao {
	
	private DlaCancelada dlaCancelada;
	
	public EmailNotificacaoCancelamentoDla(DlaCancelada dlaCancelada, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.dlaCancelada = dlaCancelada;
		
	}
	
	@Override
	public void enviar() {

		try {
			
			if(!Emails.notificarRequerenteCancelamentoDla(this.emailsDestinatarios, this.dlaCancelada).get()) {
				
				throw new AppException();
				
			}
			
		} catch (InterruptedException | ExecutionException | AppException e) {
			
			ReenvioEmail reenvioEmail = new ReenvioEmail(this.dlaCancelada.id, TipoEmail.CANCELAMENTO_DLA, e.getMessage(), this.emailsDestinatarios);
			reenvioEmail.save();
			
			e.printStackTrace();
			
		} 
	}

}
