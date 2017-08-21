package models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
		
		Emails.notificarRequerenteAnaliseJuridica(this.emailsDestinatarios, licencas, documentosInvalidados, this.analiseJuridica); 
	}

}
