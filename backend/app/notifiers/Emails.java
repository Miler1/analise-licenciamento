package notifiers;

import java.util.List;
import java.util.concurrent.Future;

import models.AnaliseDocumento;
import models.AnaliseJuridica;
import models.AnaliseTecnica;
import play.Play;
import play.mvc.Mailer;

public class Emails extends Mailer {
	
	public static Future<Boolean> notificarRequerenteAnaliseJuridica(List<String> destinatarios, String licencas, 
			List<AnaliseDocumento> documentosAnalisados, AnaliseJuridica analiseJuridica) {
		
		setSubject("Notificação referente a análise jurídica do processo %s", analiseJuridica.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {
			
			addRecipient(email);
		}
		return send(licencas, documentosAnalisados, analiseJuridica);
	}
	
	public static Future<Boolean> notificarRequerenteAnaliseTecnica(List<String> destinatarios, String licencas, 
			List<AnaliseDocumento> documentosAnalisados, AnaliseTecnica analiseTecnica) {
		
		setSubject("Notificação referente a análise técnica do processo %s", analiseTecnica.analise.processo);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {
			
			addRecipient(email);
		}
		return send(licencas, documentosAnalisados, analiseTecnica);
	}	

}
