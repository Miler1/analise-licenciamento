package jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import models.Suspensao;
import models.licenciamento.Licenca;
import play.Logger;
import play.jobs.On;

@On("0 0/1 * 1/1 * ? *")
public class ProcessamentoPrazoSuspensao  extends GenericJob {
	
	@Override
	public void executar() throws Exception{

		Logger.info("[INICIO-JOB] ::ProcessamentoPrazoSuspensao:: [INICIO-JOB]");
		verificaPrazoSuspensao();
		Logger.info("[FIM-JOB] ::ProcessamentoPrazoSuspensao:: [FIM-JOB]");
		

	}
	
	public void verificaPrazoSuspensao() {
		
		List<Suspensao> suspensoes = Suspensao.findAtivas();
		
		if(!suspensoes.isEmpty()) {
			
			for(Suspensao suspensao: suspensoes){
				
				//mostra a data final da suspensao
				
				Date dataFinalSuspenso = addDays(suspensao.dataSuspensao, suspensao.qtdeDiasSuspensao);
				System.out.println("SOMANDO NA DATA "+suspensao.qtdeDiasSuspensao+" days: "+dataFinalSuspenso.toString());
				Date hoje =  new Date();
				
				if(dataFinalSuspenso.after(hoje)) {	
					
					Licenca dadosLicenca = Licenca.findById(suspensao.licenca.id);
					Licenca novaLicenca = new Licenca(dadosLicenca.caracterizacao);
					novaLicenca.dataValidade = suspensao.dataValidadeLicenca;
					novaLicenca.licencaAnterior = dadosLicenca.licencaAnalise;
					
					novaLicenca.save();
			
					suspensao.ativo = false;
					suspensao._save();
					
				}
				
			}
		}
		
	}
	
	public static Date addDays(Date date, int days) {
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
				
		return cal.getTime();
	}

}
