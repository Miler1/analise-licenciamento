package notifiers;

import java.util.List;
import java.util.concurrent.Future;

import models.*;
import models.licenciamento.Licenca;
import play.Play;
import play.mvc.Mailer;

public class Emails extends Mailer {
	
	public static Future<Boolean> notificarRequerenteAnaliseJuridica(List<String> destinatarios, String licencas,
			List<AnaliseDocumento> documentosAnalisados, AnaliseJuridica analiseJuridica, Notificacao notificacacao) {
		
		setSubject("Movimentação do processo %s", analiseJuridica.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {
			
			addRecipient(email);
		}
		return send(licencas, documentosAnalisados, analiseJuridica, notificacacao);
	}
	
	public static Future<Boolean> notificarRequerenteAnaliseTecnica(List<String> destinatarios, String licencas, 
			List<AnaliseDocumento> documentosAnalisados, AnaliseTecnica analiseTecnica, Notificacao notificacacao) {
		
		setSubject("Movimentação do processo %s", analiseTecnica.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {
			
			addRecipient(email);
		}
		return send(licencas, documentosAnalisados, analiseTecnica, notificacacao);
	}
	
	public static Future<Boolean> notificarRequerenteSuspensaoLicenca(List<String> destinatarios, Suspensao suspensao) {
		
		setSubject("Notificação referente a suspensão da licença %s(%s)", suspensao.licenca.caracterizacao.tipoLicenca.nome, suspensao.licenca.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {
			
			addRecipient(email);
		}
		return send(suspensao);
	}
	
	public static Future<Boolean> notificarRequerenteCancelamentoLicenca(List<String> destinatarios, LicencaCancelada licencaCancelada) {
		
		setSubject("Notificacao referente ao cancelamento da licenca %s(%s)", licencaCancelada.licenca.caracterizacao.tipoLicenca.nome, licencaCancelada.licenca.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") + ">");
		for(String email:destinatarios) {
			
			addRecipient(email);
		}
		return send(licencaCancelada);
		
	}
	
	public static Future<Boolean> notificarRequerenteCancelamentoDla(List<String> destinatarios, DlaCancelada dlaCancelada) {
		
		setSubject("Notificacao referente ao cancelamento %s(%s)", dlaCancelada.dispensaLicenciamento.caracterizacao.tipoLicenca.nome, dlaCancelada.dispensaLicenciamento.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") + ">");
		for(String email:destinatarios) {
			
			addRecipient(email);
		}
		return send(dlaCancelada);
		
	}
}
