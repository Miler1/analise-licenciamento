package models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringUtils;

import exceptions.AppException;
import models.ReenvioEmail.TipoEmail;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import notifiers.Emails;

public class EmailNotificacaoAnaliseJuridica extends EmailNotificacao {
	
	private AnaliseJuridica analiseJuridica;
	
	public EmailNotificacaoAnaliseJuridica(AnaliseJuridica analiseJuridica, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.analiseJuridica = analiseJuridica;		
				
	}
	
	@Override	
	public void enviar() {
		
		try {
		
			List<String> tiposlicenca = new ArrayList<String>();
			for(Caracterizacao caracterizacao : this.analiseJuridica.analise.processo.caracterizacoes) {
				
				tiposlicenca.add(caracterizacao.tipoLicenca.nome);
			}
			String licencas = StringUtils.join(tiposlicenca, ",");
			
			List<AnaliseDocumento> documentosInvalidados = new ArrayList<AnaliseDocumento>();
			for(AnaliseDocumento analiseDocumento : this.analiseJuridica.analisesDocumentos) {
				
				if(analiseDocumento.documento.tipo.tipoAnalise.equals(TipoAnalise.JURIDICA) && !analiseDocumento.validado) {
					documentosInvalidados.add(analiseDocumento);
				}
			}

			List<Notificacao> notificacoes = Notificacao.find("analiseJuridica.id", this.analiseJuridica.id).fetch();

			if(!Emails.notificarRequerenteAnaliseJuridica(this.emailsDestinatarios, licencas, documentosInvalidados, this.analiseJuridica, notificacoes).get()) {
				
				throw new AppException();
				
			}
			
		} catch (InterruptedException | ExecutionException | AppException e) {
			
			ReenvioEmail reenvioEmail = new ReenvioEmail(this.analiseJuridica.id, TipoEmail.NOTIFICACAO_ANALISE_JURIDICA, e.getMessage(), this.emailsDestinatarios);
			reenvioEmail.save();
			
			e.printStackTrace();
		} 
	}

}
