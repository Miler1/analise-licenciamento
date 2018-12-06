package jobs;

import models.manejoDigital.ProcessoManejo;
import play.Logger;
import play.jobs.On;

import java.util.List;

@On("cron.verificarAnalisesShape")
public class VerificarAnalisesShape extends GenericJob {

	@Override
	public void executar() throws Exception {

		Logger.info("[INICIO-JOB] ::VerificarAnalisesShape:: [INICIO-JOB]");

		List<ProcessoManejo> processos = ProcessoManejo.find("objetoTramitavel.condicao.id", 23l).fetch();

		for (ProcessoManejo processo : processos) {

			processo.verificarAnaliseShape();
		}

		Logger.info("[FIM-JOB] ::VerificarAnalisesShape:: [FIM-JOB]");
	}
}
