package notifiers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import models.AnaliseJuridica;
import play.Play;
import play.mvc.Mailer;

public class Emails extends Mailer {
	
	public static Future<Boolean> notificarRequerenteAnaliseJuridica(AnaliseJuridica analiseJuridica) {
		
		setSubject("Notificação referente a análise jurídica do processo %s", analiseJuridica.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		
		return send(analiseJuridica);
		
	}

}
