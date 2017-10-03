package jobs;

import java.util.List;

import models.Suspensao;
import play.Logger;
import play.jobs.On;

@On("0 0/1 * 1/1 * ? *")
public class ProcessamentoPrazoSuspensao  extends GenericJob{
	
	@Override
	public void executar() throws Exception{

		Logger.info("[INICIO-JOB] ::ProcessamentoPrazoSuspensao:: [INICIO-JOB]");
		//verificaPrazoSuspensao();
		Logger.info("[FIM-JOB] ::ProcessamentoPrazoSuspensao:: [FIM-JOB]");
		

	}
	
	public void verificaPrazoSuspensao() {
		
		List<Suspensao> suspensoes = Suspensao.findAll();
		
		if(!suspensoes.isEmpty()) {
			
			for(Suspensao suspensao: suspensoes){
				
				if(suspensao.dataSuspensao != null) {
					
					
					
				}
				
			}
		}
		
	}

}
