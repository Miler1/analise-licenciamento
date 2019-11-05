package models;

import exceptions.AppException;
import models.ReenvioEmail.TipoEmail;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import notifiers.Emails;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
			for(Caracterizacao caracterizacao : this.analiseJuridica.analise.processo.empreendimento.caracterizacoes) {
				
				tiposlicenca.add(caracterizacao.tipoLicenca.nome);
			}
			String licencas = StringUtils.join(tiposlicenca, ",");
			
			List<AnaliseDocumento> documentosInvalidados = new ArrayList<AnaliseDocumento>();
			for(AnaliseDocumento analiseDocumento : this.analiseJuridica.analisesDocumentos) {
				
				if(analiseDocumento.documento.tipo.tipoAnalise.equals(TipoAnalise.JURIDICA) && !analiseDocumento.validado) {
					documentosInvalidados.add(analiseDocumento);
				}
			}

			Notificacao notificacao = Notificacao.find("id_analise_juridica", this.analiseJuridica.id).first();

			if(!Emails.notificarRequerenteAnaliseJuridica(this.emailsDestinatarios, licencas, documentosInvalidados, this.analiseJuridica, notificacao).get()) {
				
				throw new AppException();
				
			}
			
		} catch (InterruptedException | ExecutionException | AppException e) {
			
			ReenvioEmail reenvioEmail = new ReenvioEmail(this.analiseJuridica.id, TipoEmail.NOTIFICACAO_ANALISE_JURIDICA, e.getMessage(), this.emailsDestinatarios);
			reenvioEmail.save();
			
			e.printStackTrace();
		} 
	}

}
