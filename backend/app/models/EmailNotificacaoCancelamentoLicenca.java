package models;

import java.util.List;

import models.licenciamento.Licenca;
import notifiers.Emails;

public class EmailNotificacaoCancelamentoLicenca extends EmailNotificacao {
	
	private Licenca licenca;
	
	public EmailNotificacaoCancelamentoLicenca(Licenca licenca, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.licenca = licenca;
		
	}
	
	@Override
	public void enviar() {

		Emails.notificarRequerenteCancelamentoLicenca(this.emailsDestinatarios, this.licenca); 
	}

}
