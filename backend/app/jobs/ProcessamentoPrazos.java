package jobs;

import java.util.List;

import models.Analise;
import play.Logger;
import play.jobs.On;

@On("cron.processamentoPrazos")
public class ProcessamentoPrazos extends GenericJob {

	@Override
	public void executar() throws Exception {
		
		Logger.info("[INICIO-JOB] ::ProcessamentoPrazos:: [INICIO-JOB]");
		contarDiasAnalise();
		Logger.info("[FIM-JOB] ::ProcessamentoPrazos:: [FIM-JOB]");
		
		
	}
	
	public void contarDiasAnalise() {
		
		List<Analise> analises = Analise.findAtivas();
		
		for(Analise analise : analises) {
			
			if(analise.diasAnalise == null) {
				DiasAnalise diasAnalise = new DiasAnalise(analise);
				analise.diasAnalise = diasAnalise;
			}
			
			if(analise.getAnaliseTecnica() != null) {
				analise.diasAnalise.qtdeDiasAnaliseTecnica += 1;
			} else {
				analise.diasAnalise.qtdeDiasAnaliseJuridica += 1;
			}
			
			analise.save();
			
		}
		
	}

}
