package models;

import java.util.List;
import java.util.concurrent.ExecutionException;

import exceptions.AppException;
import models.ReenvioEmail.TipoEmail;
import notifiers.Emails;

public class EmailNotificacaoSuspensaoLicenca extends EmailNotificacao {
	
	private Suspensao suspensao;
	
	public EmailNotificacaoSuspensaoLicenca(Suspensao suspensao, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.suspensao = suspensao;		
				
	}	

	@Override
	public void enviar() {

		try {

			if(!Emails.notificarRequerenteSuspensaoLicenca(this.emailsDestinatarios, this.suspensao).get()) {
				
				throw new AppException();
				
			}
			
		} catch (InterruptedException | ExecutionException | AppException e) {
			
			ReenvioEmail reenvioEmail = new ReenvioEmail(this.suspensao.id, TipoEmail.SUSPENSAO_LICENCA, e.getMessage(), this.emailsDestinatarios);
			reenvioEmail.save();
			
			e.printStackTrace();
			
		} 
	}

}
