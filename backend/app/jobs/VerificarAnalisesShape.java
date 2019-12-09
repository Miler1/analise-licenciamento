package jobs;

import models.manejoDigital.ProcessoManejo;
import play.Logger;
import play.jobs.On;
import security.AuthManejo;

import java.util.List;

@On("cron.verificarAnalisesShape")
public class VerificarAnalisesShape extends GenericJob {

	@Override
	public void executar() throws Exception {

//		Logger.info("[INICIO-JOB] ::VerificarAnalisesShape:: [INICIO-JOB]");
//
//		List<ProcessoManejo> processos = ProcessoManejo.find("objetoTramitavel.condicao.id", 23l).fetch();
//
//		for (ProcessoManejo processo : processos) {
//
//			// TODO futuramente usar id do Semas do usuário
//			String cacheId = processo.numeroProcesso;
//			String token = AuthManejo.getToken(cacheId);
//			processo.verificarAnaliseShape(token);
//		}
//
//		Logger.info("[FIM-JOB] ::VerificarAnalisesShape:: [FIM-JOB]");
	}
}
