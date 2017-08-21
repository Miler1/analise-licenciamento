package notifiers;

import java.util.List;
import java.util.concurrent.Future;

import models.AnaliseDocumento;
import models.AnaliseJuridica;
import play.Play;
import play.mvc.Mailer;

public class Emails extends Mailer {
	
	public static Future<Boolean> notificarRequerenteAnaliseJuridica(List<String> destinatarios, String numeroProcesso, String licencas, 
			List<AnaliseDocumento> documentosAnalisados, AnaliseJuridica analiseJuridica) {
		
		setSubject("Notificação referente a análise jurídica do processo %s", numeroProcesso);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {
			
			addRecipient(email);
		}
		return send(numeroProcesso, licencas, documentosAnalisados, analiseJuridica);
		
	}

}
