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
		
		List<Suspensao> suspensoes = Suspensao.findAll();
		
		if(!suspensoes.isEmpty()) {
			
			for(Suspensao suspensao: suspensoes){
				
				//mostra a data final da suspensao
				
				Date dataFinalSuspenso = addDays(suspensao.dataSuspensao, suspensao.qtdeDiasSuspensao);
				System.out.println("SOMANDO NA DATA "+suspensao.qtdeDiasSuspensao+" days: "+dataFinalSuspenso.toString());
				Date hoje =  new Date();
				
				if(dataFinalSuspenso.after(hoje)) {	
					
					
					Licenca novaLicenca = Licenca.findById(suspensao.licenca.id);
					novaLicenca.dataValidade = dataFinalSuspenso;
					//novaLicenca.
					//mudar o ativo
					//suspensao.ativo = false;
					//salvar a nova data de validade
					//suspensao.dataValidadeProrrogada = addDays(hoje, suspensao.qtdeDiasSuspensao;
					
					
					
					
					
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
