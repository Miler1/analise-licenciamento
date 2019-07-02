package jobs;

import models.Analise;
import models.DiasAnalise;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import play.Logger;
import play.jobs.OnApplicationStart;

import java.util.Date;
import java.util.List;

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
						
						if(analise.analiseTecnica.dataFimValidacaoAprovador != null) {
							// se o aprovador já finalizou a analise
							
							diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, analise.analiseTecnica.dataFimValidacaoAprovador);
							diasAnalise.qtdeDiasTecnica = CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, analise.findPrimeiraAnaliseTecnicaComDataFim().dataFim);
							diasAnalise.qtdeDiasAprovador = CalculaDiferencaDias(analise.findPrimeiraAnaliseTecnicaComDataFim().dataFim, analise.analiseTecnica.dataFimValidacaoAprovador);
							
						} else {
							
							diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, new Date());
							diasAnalise.qtdeDiasTecnica = CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, analise.findPrimeiraAnaliseTecnicaComDataFim().dataFim);
							diasAnalise.qtdeDiasAprovador = CalculaDiferencaDias(analise.findPrimeiraAnaliseTecnicaComDataFim().dataFim, new Date());
							
						}

					} else {
					// se a analise tecnica nao acabou

						diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, new Date());
						diasAnalise.qtdeDiasTecnica= CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, new Date());
					}

				} else if(analise.getAnaliseGeo() != null) {
					// se existe analise geo
						
						if(analise.analiseGeo.dataFim != null) {
						// se a analise geo acabou

							if(analise.analiseGeo.dataFimValidacaoAprovador != null) {
								// se o aprovador já finalizou a analise

								diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, analise.analiseGeo.dataFimValidacaoAprovador);
								diasAnalise.qtdeDiasGeo = CalculaDiferencaDias(analise.analiseGeo.dataCadastro, analise.findPrimeiraAnaliseGeoComDataFim().dataFim);
								diasAnalise.qtdeDiasAprovador = CalculaDiferencaDias(analise.findPrimeiraAnaliseGeoComDataFim().dataFim, analise.analiseGeo.dataFimValidacaoAprovador);

							} else {

								diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, new Date());
								diasAnalise.qtdeDiasGeo = CalculaDiferencaDias(analise.analiseGeo.dataCadastro, analise.findPrimeiraAnaliseGeoComDataFim().dataFim);
								diasAnalise.qtdeDiasAprovador = CalculaDiferencaDias(analise.findPrimeiraAnaliseGeoComDataFim().dataFim, new Date());

							}

						} else {
							// se a analise geo nao acabou

							diasAnalise.qtdeDiasAnalise = CalculaDiferencaDias(analise.dataCadastro, new Date());
							diasAnalise.qtdeDiasGeo= CalculaDiferencaDias(analise.analiseGeo.dataCadastro, new Date());

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
