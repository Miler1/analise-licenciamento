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
import play.jobs.OnApplicationStart;
import utils.Configuracoes;

@OnApplicationStart
public class ProcessamentoProcessosEmAnalise extends GenericJob {

	@Override
	public void executar() throws Exception {
		
		Logger.info("[INICIO-JOB] ::ProcessamentoProcessosEmAnalise:: [INICIO-JOB]");
		recuperarProcessosEmAnalise();
		Logger.info("[FIM-JOB] ::ProcessamentoProcessosEmAnalise:: [FIM-JOB]");
		
		
	}
	
	public void recuperarProcessosEmAnalise() {
		
		List<DiasAnalise> dAnalise = DiasAnalise.findAll();
		
		if(dAnalise.isEmpty()) {
			
			List<Analise> analises = Analise.findAll();
			
			for(Analise analise : analises) {
				
				DiasAnalise diasAnalise = new DiasAnalise();
	
				if (analise.getAnaliseTecnica() != null) {
					
					if (analise.analiseTecnica.dataFim != null) {
					
						diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, analise.analiseTecnica.dataFim);
						diasAnalise.qtdeDiasJuridica = CalculaDiferencaDias(analise.dataCadastro, analise.analiseJuridica.dataFim);
						diasAnalise.qtdeDiasTecnica = CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, analise.analiseTecnica.dataFim);
					
					} else {
						
							
						diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, new Date());
						
						
						if(analise.analiseJuridica.dataFim != null) {
							
							diasAnalise.qtdeDiasJuridica = CalculaDiferencaDias(analise.dataCadastro, analise.analiseJuridica.dataFim);
							
							if(analise.analiseTecnica.dataFim != null) {
								
								diasAnalise.qtdeDiasTecnica = CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, analise.analiseTecnica.dataFim);
								
							} else { 
								
								diasAnalise.qtdeDiasTecnica= CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, new Date());
							}
							
						} else {
							
							if(analise.getAnaliseJuridica() != null) {
							
								if(analise.analiseJuridica.dataFim != null) {
									
									diasAnalise.qtdeDiasJuridica = CalculaDiferencaDias(analise.dataCadastro, analise.analiseJuridica.dataFim);
								} else {
									
									diasAnalise.qtdeDiasJuridica = CalculaDiferencaDias(analise.dataCadastro, new Date());
								}
								
								diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, new Date());
							}
							
						}
						
					}
				}else {
					
					diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, new Date());
					
				}
				
				diasAnalise.analise = analise;
				diasAnalise._save();
			}
		}
				
		
		
	}
	
	public int CalculaDiferencaDias(Date dataInicial, Date dataFinal) {
		
		DateTime dataInicio = new DateTime(dataInicial);
		DateTime dataFim = new DateTime(dataFinal);
		
		return Days.daysBetween(dataInicio, dataFim).getDays(); 
	}

}
