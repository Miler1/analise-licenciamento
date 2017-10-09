package models;

import java.util.List;

import models.licenciamento.Licenca;
import notifiers.Emails;

public class EmailNotificacaoCancelamentoLicenca extends EmailNotificacao {
	
	private LicencaCancelada licencaCancelada;
	
	public EmailNotificacaoCancelamentoLicenca(LicencaCancelada licencaCancelada, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.licencaCancelada = licencaCancelada;
		
	}
	
	@Override
	public void enviar() {

		Emails.notificarRequerenteCancelamentoLicenca(this.emailsDestinatarios, this.licencaCancelada); 
	}

}
