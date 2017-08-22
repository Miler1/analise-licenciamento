package models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import notifiers.Emails;

public class EmailNotificacaoAnaliseTecnica extends EmailNotificacao {
	
	private AnaliseTecnica analiseTecnica;
	
	public EmailNotificacaoAnaliseTecnica(AnaliseTecnica analiseTecnica, List<String> emailsDestinatarios) {
		
		super(emailsDestinatarios);
		this.analiseTecnica = analiseTecnica;		
				
	}	

	@Override
	public void enviar() {
		
		List<String> tiposlicenca = new ArrayList<String>();
		for(Caracterizacao caracterizacao : this.analiseTecnica.analise.processo.caracterizacoes) {
			
			tiposlicenca.add(caracterizacao.tipoLicenca.nome);
		}
		String licencas = StringUtils.join(tiposlicenca, ",");
		
		List<AnaliseDocumento> documentosInvalidados = new ArrayList<AnaliseDocumento>();
		for(AnaliseDocumento analiseDocumento : this.analiseTecnica.analisesDocumentos) {
			
			if(analiseDocumento.documento.tipo.tipoAnalise.equals(TipoAnalise.TECNICA) && !analiseDocumento.validado) {
				documentosInvalidados.add(analiseDocumento);
			}
		}
		
		Emails.notificarRequerenteAnaliseTecnica(this.emailsDestinatarios, licencas, documentosInvalidados, this.analiseTecnica); 
	}

}
