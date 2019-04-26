package models;

import exceptions.AppException;
import models.ReenvioEmail.TipoEmail;
import notifiers.Emails;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoCancelamentoLicenca extends EmailNotificacao {
	
	private LicencaCancelada licencaCancelada;
	
	public EmailNotificacaoCancelamentoLicenca(LicencaCancelada licencaCancelada, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.licencaCancelada = licencaCancelada;
		
	}
	
	@Override
	public void enviar() {

		try {
			
			if(!Emails.notificarRequerenteCancelamentoLicenca(this.emailsDestinatarios, this.licencaCancelada).get()) {
				
				throw new AppException();
				
			}
			
		} catch (InterruptedException | ExecutionException | AppException e) {
			
			ReenvioEmail reenvioEmail = new ReenvioEmail(this.licencaCancelada.id, TipoEmail.CANCELAMENTO_LICENCA, e.getMessage(), this.emailsDestinatarios);
			reenvioEmail.save();
			
			e.printStackTrace();
			
		} 
	}

}
