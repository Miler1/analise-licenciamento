package jobs;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import com.vividsolutions.jts.index.strtree.Interval;

import models.Analise;
import models.DiasAnalise;
import play.Logger;
import play.jobs.On;
import utils.Configuracoes;

//@On("cron.processamentoPrazos")
@On("	0 0/1 * 1/1 * ? *")
public class ProcessamentoPrazos extends GenericJob {

	@Override
	public void executar() throws Exception {
		
		Logger.info("[INICIO-JOB] ::ProcessamentoPrazos:: [INICIO-JOB]");
		contarDiasAnalise();
		Logger.info("[FIM-JOB] ::ProcessamentoPrazos:: [FIM-JOB]");
		
		
	}
	
	public void contarDiasAnalise() {
		
		List<Analise> analises = Analise.findAtivas();
		
		if(!analises.isEmpty()) {
			
			for(Analise analise : analises) {
				
				DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
				analise.diasAnalise = verificaDiasAnalise;
				
				if(verificaDiasAnalise == null) {
					
					DiasAnalise diasAnalise = new DiasAnalise(analise);
					analise.diasAnalise = diasAnalise;
					
				} else {
					//tem que verificar se a analise já terminou
					analise.diasAnalise.qtdeDiasAnalise += 1;
				}
				
				if(analise.getAnaliseTecnica() != null) {
					
					if(analise.diasAnalise.qtdeDiasTecnica == null){
						analise.diasAnalise.qtdeDiasTecnica = 0;
					}
					
					analise.diasAnalise.qtdeDiasTecnica += 1;
					
				} else {
					
					if(analise.getAnaliseJuridica() != null) {
						
						if(analise.diasAnalise.qtdeDiasJuridica == null){
							analise.diasAnalise.qtdeDiasJuridica = 0;
						}
						analise.diasAnalise.qtdeDiasJuridica += 1;
					}
				}
				
				analise.diasAnalise._save();
				
			}
		}
		
	}

}
