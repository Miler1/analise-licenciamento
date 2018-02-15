package jobs;


import java.util.List;

import models.Analise;
import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.DiasAnalise;
import models.Notificacao;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.jobs.On;

@On("cron.processamentoPrazos")
public class VerificarNotificacoes extends GenericJob {

	@Override
	public void executar() throws Exception {
		
		Logger.info("[INICIO-JOB] ::VerificarNotificacoes:: [INICIO-JOB]");
		verificarStatusNotificacoes();
		Logger.info("[FIM-JOB] ::VerificarNotificacoes:: [FIM-JOB]");
		
		
	}
	
	public void verificarStatusNotificacoes() {
		
		List<Analise> analises = Analise.findComNotificacao();
		
		if(!analises.isEmpty()) {
			
			for(Analise analise : analises) {
				
				DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();

				if(analise.hasNotificacaoNaoResolvida() && verificaDiasAnalise.qtdeDiasNotificacao >= 10) {
					
					analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.ARQUIVAR_PROCESSO);
					
				} else if(!analise.hasNotificacaoNaoResolvida()) {

					List<Notificacao> notificacoes = Notificacao.getByAnalise(analise);
					analise.temNotificacaoAberta = false;
					analise.save();
					
					if(notificacoes.get(0).analiseJuridica != null) {
						
						analise.analiseJuridica.ativo = false;
						analise.analiseJuridica._save();
						
						AnaliseJuridica novaAnaliseJuridica = analise.analiseJuridica.gerarCopia();
						novaAnaliseJuridica._save();
						
						analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.RESOLVER_NOTIFICACAO_JURIDICA);
						
					} else if(notificacoes.get(0).analiseTecnica != null) {
						
						analise.analiseTecnica.ativo = false;
						analise.analiseTecnica._save();
						
						AnaliseTecnica novaAnaliseTecnica = analise.analiseTecnica.gerarCopia();
						novaAnaliseTecnica._save();
						
						analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.RESOLVER_NOTIFICACAO_TECNICA);
						
					}
					
				}
				
			}
			
		}
		
		
	}
	
}
