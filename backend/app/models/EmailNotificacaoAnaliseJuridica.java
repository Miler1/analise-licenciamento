package models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import models.licenciamento.TipoLicenca;
import notifiers.Emails;
import utils.ListUtil;

public class EmailNotificacaoAnaliseJuridica {
	
	private AnaliseJuridica analiseJuridica;
	private List<String> emailsDestinatarios;
	
	public EmailNotificacaoAnaliseJuridica(AnaliseJuridica analiseJuridica, List<String> emailsDestinatarios) {
		
		this.analiseJuridica = analiseJuridica;		
		this.emailsDestinatarios = emailsDestinatarios;		
	}
	
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
		
		Emails.notificarRequerenteAnaliseJuridica(this.emailsDestinatarios, this.analiseJuridica.analise.processo.numero, 
				licencas, this.analiseJuridica.analise.processo.caracterizacoes.get(0).atividadeCaracterizacao.atividade.nome, 
				this.analiseJuridica.parecer, documentosInvalidados); 
	}

}
