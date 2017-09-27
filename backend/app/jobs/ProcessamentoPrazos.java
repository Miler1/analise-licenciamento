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
			
			DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
			analise.diasAnalise = verificaDiasAnalise;
			
			if(verificaDiasAnalise == null) {
				
				DiasAnalise diasAnalise = new DiasAnalise(analise);
				analise.diasAnalise = diasAnalise;
				
			} else {
				
				analise.diasAnalise.qtdeDiasAnalise += 1;
			}
			
			if(analise.getAnaliseTecnica() != null) {
				
				analise.diasAnalise.qtdeDiasTecnica += 1;
				
			} else {
				analise.diasAnalise.qtdeDiasJuridica += 1;
			}
			
			analise.diasAnalise._save();
			
		}
		
	}

}
