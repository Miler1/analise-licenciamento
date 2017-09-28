package jobs;

import java.util.Date;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.Period;

import com.vividsolutions.jts.index.strtree.Interval;

import models.Analise;
import models.DiasAnalise;
import play.Logger;
import play.jobs.On;
import utils.Configuracoes;

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
			
			DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
			analise.diasAnalise = verificaDiasAnalise;
			
			if(verificaDiasAnalise == null) {
				
				DiasAnalise diasAnalise = new DiasAnalise(analise);
				analise.diasAnalise = diasAnalise;
				
			} else{
				
				analise.diasAnalise.qtdeDiasAnalise -= 1;
			}
			
			if(analise.getAnaliseTecnica() != null) {
				
				if(analise.diasAnalise.qtdeDiasTecnica == null){
					
					Date dataHoje =  new Date();
					final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

					int diasAnaliseTecnica = (int) ((dataHoje.getTime() - analise.analiseTecnica.dataInicio.getTime())/ DAY_IN_MILLIS );
					
					analise.diasAnalise.qtdeDiasTecnica = Configuracoes.PRAZO_ANALISE_TECNICA - diasAnaliseTecnica;
				}
				
				analise.diasAnalise.qtdeDiasTecnica -= 1;
			} else {
				analise.diasAnalise.qtdeDiasJuridica -= 1;
			}
			
			analise.diasAnalise.save();
			
		}
		
	}

}
