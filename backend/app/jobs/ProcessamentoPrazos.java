package jobs;

import java.util.List;

import models.Analise;
import models.DiasAnalise;
import play.Logger;
import play.jobs.On;

@On("0 0/1 * 1/1 * ? *")
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
				
			} else{
				analise.diasAnalise.qtdeDiasAnalise -= 1;
			}
			
			if(analise.getAnaliseTecnica() != null) {
				analise.diasAnalise.qtdeDiasTecnica = 1;
			} else {
				analise.diasAnalise.qtdeDiasJuridica -= 1;
			}
			analise.diasAnalise._save();
			
		}
		
	}

}
