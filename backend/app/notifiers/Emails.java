package notifiers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import models.AnaliseDocumento;
import models.AnaliseJuridica;
import play.Play;
import play.mvc.Mailer;

public class Emails extends Mailer {
	
	public static Future<Boolean> notificarRequerenteAnaliseJuridica(List<String> destinatarios, String processo, String licencas, 
			String atividade, String observacao, List<AnaliseDocumento> documentosAnalisados) {
		
		setSubject("Notificação referente a análise jurídica do processo %s", processo);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {
			
			addRecipient(email);
		}
		return send(processo, licencas, atividade, observacao, documentosAnalisados);
		
	}

}
