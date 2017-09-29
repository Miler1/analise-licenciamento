package jobs;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
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

		if(dAnalise.size() == 0) {

			List<Analise> analises = Analise.findAll();

			for(Analise analise : analises) {

				DiasAnalise diasAnalise = new DiasAnalise();

				if (analise.getAnaliseTecnica() != null) {
				// se existe analise tecnica

					if (analise.analiseTecnica.dataFim != null) {
					// se a analise tecnica acabou

						diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, analise.analiseTecnica.dataFim);
						diasAnalise.qtdeDiasJuridica = CalculaDiferencaDias(analise.dataCadastro, analise.analiseJuridica.dataFim);
						diasAnalise.qtdeDiasTecnica = CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, analise.analiseTecnica.dataFim);

					} else {
					// se a analise tecnica nao acabou

						diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, new Date());
						diasAnalise.qtdeDiasJuridica = CalculaDiferencaDias(analise.dataCadastro, analise.analiseJuridica.dataFim);							
						diasAnalise.qtdeDiasTecnica= CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, new Date());

					}

				} else {
				// se nao existe analise tecnica 

					if(analise.getAnaliseJuridica() != null) {
					// se existe analise juridica
						
						if(analise.analiseJuridica.dataFim != null) {
						// se a analise juridica acabou

							diasAnalise.qtdeDiasJuridica = CalculaDiferencaDias(analise.dataCadastro, analise.analiseJuridica.dataFim);
							
	
						} else {
						// se a analise juridica nao acabou

							diasAnalise.qtdeDiasJuridica = CalculaDiferencaDias(analise.dataCadastro, new Date());

						}

						diasAnalise.qtdeDiasAnalise = diasAnalise.qtdeDiasJuridica;
				
					}

				}

				diasAnalise.analise = analise;
				diasAnalise._save();
			}
		}

	}
	
	public int CalculaDiferencaDias(Date dataInicial, Date dataFinal) {
		
		LocalDate dataInicio = new LocalDate(dataInicial.getTime());
		LocalDate dataFim = new LocalDate(dataFinal.getTime());
		
		return Days.daysBetween(dataInicio, dataFim).getDays(); 
	}

}
