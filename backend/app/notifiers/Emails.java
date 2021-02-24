package notifiers;

import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.Municipio;
import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import org.apache.commons.mail.EmailAttachment;
import play.Play;
import play.mvc.Mailer;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

public class Emails extends Mailer {
	
	public static Future<Boolean> notificarRequerenteAnaliseJuridica(List<String> destinatarios, String licencas,
			List<AnaliseDocumento> documentosAnalisados, AnaliseJuridica analiseJuridica, Notificacao notificacao) {
		
		setSubject("Movimentação do protocolo %s", analiseJuridica.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {
			
			addRecipient(email);
		}
		return send(licencas, documentosAnalisados, analiseJuridica, notificacao);
	}

	public static Future<Boolean> notificarRequerenteAnaliseGeo(List<String> destinatarios, String licencas,
			AnaliseGeo analiseGeo, ParecerAnalistaGeo parecerAnalistaGeo, Endereco enderecoCompleto, List<Documento> pdfsNotificacao) {

		setSubject("Movimentação do protocolo %s", analiseGeo.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {

			addRecipient(email);
		}

		pdfsNotificacao.stream().forEach(pdfNotificacao -> {

            EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(new File(pdfNotificacao.getFile().getPath()).getPath());
			addAttachment(attachment);

		});

		return send(licencas, analiseGeo, parecerAnalistaGeo, enderecoCompleto);

	}

	public static Future<Boolean> notificarInteressadoComunicado(List<String> destinatarios, String licencas,
																 AnaliseGeo analiseGeo, ParecerAnalistaGeo parecerAnalistaGeo, Endereco enderecoCompleto, Caracterizacao caracterizacao, Comunicado comunicado) {

		setSubject("Movimentação do protocolo %s", analiseGeo.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {

			addRecipient(email);
		}

		return send(licencas, analiseGeo, parecerAnalistaGeo, enderecoCompleto, caracterizacao, comunicado);

	}

	public static Future<Boolean> notificarRequerenteStatusAnalise(List<String> destinatarios, Analise analise, ParecerSecretario parecerSecretario) {

		setSubject("Movimentação do protocolo %s", analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {

			addRecipient(email);
		}

		return send(analise, parecerSecretario);

	}

	public static Future<Boolean> notificarRequerenteStatusDispensa(List<String> destinatarios, Caracterizacao caracterizacao) {

		setSubject("Movimentação do protocolo %s", caracterizacao.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {

			addRecipient(email);
		}

		return send(caracterizacao);

	}

	public static Future<Boolean> notificarRequerenteAnaliseTecnica(List<String> destinatarios, String licencas,
																AnaliseTecnica analiseTecnica, ParecerAnalistaTecnico parecerAnalistaTecnico, Endereco enderecoCompleto, List<Documento> pdfsNotificacao) {

		setSubject("Movimentação do protocolo %s", analiseTecnica.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {

			addRecipient(email);
		}

		pdfsNotificacao.stream().forEach(pdfNotificacao -> {

			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(new File(pdfNotificacao.getFile().getPath()).getPath());
			addAttachment(attachment);

		});

		return send(licencas, analiseTecnica, parecerAnalistaTecnico, enderecoCompleto);

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

	public static Future<Boolean> notificarRequerenteArquivamentoProcesso(List<String> destinatarios, Processo processo,
	                                                                      Date arquivamento, List<Notificacao> notificacoes,
	                                                                      String siglaSetor) {

		setSubject("Notificacao referente ao arquivamento do protocolo: " + processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") + ">");

		for(String email:destinatarios) {

			addRecipient(email);
		}

		return send(processo, arquivamento, notificacoes, siglaSetor);
	}

	public static Future<Boolean> notificarRequerenteProrrogacaoLicenca(List<String> destinatarios, Licenca licenca) {

		setSubject("Notificação referente a prorrogacao da licença %s(%s)", licenca.caracterizacao.tipoLicenca.nome, licenca.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {

			addRecipient(email);
		}
		return send(licenca);
	}

	public static Future<Boolean> comunicarOrgaoResponsavelAnaliseGeo(List<String> destinatarios,
																	  AnaliseGeo analiseGeo, Comunicado comunicado, Municipio municipio, File filePdfParecer, File cartaImagem) {

		setSubject("Movimentação do protocolo %s", analiseGeo.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {

			addRecipient(email);
		}
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(new File(filePdfParecer.getPath()).getPath());
		addAttachment(attachment);

		EmailAttachment attachmentCartaImagem = new EmailAttachment();
		attachmentCartaImagem.setPath(new File(cartaImagem.getPath()).getPath());
		addAttachment(attachmentCartaImagem);

		return send(analiseGeo, comunicado, municipio);
	}

	public static Future<Boolean> comunicarJuridicoAnalise(List<String> destinatarios,
											AnaliseGeo analiseGeo, Municipio municipio, ParecerJuridico parecerJuridico, ParecerAnalistaGeo parecerAnalistaGeo,File filePdfParecer, File cartaImagem) {

		setSubject("Movimentação do protocolo %s", analiseGeo.analise.processo.numero);
		setFrom("Análise <"+ Play.configuration.getProperty("mail.smtp.sender") +">");
		for(String email : destinatarios) {

			addRecipient(email);
		}

		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(new File(filePdfParecer.getPath()).getPath());
		addAttachment(attachment);

		EmailAttachment attachmentCartaImagem = new EmailAttachment();
		attachmentCartaImagem.setPath(new File(cartaImagem.getPath()).getPath());
		addAttachment(attachmentCartaImagem);


		return send(analiseGeo, municipio, parecerJuridico, parecerAnalistaGeo);
	}

}
