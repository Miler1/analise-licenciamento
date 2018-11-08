package jobs;

import models.manejoDigital.ProcessoManejo;
import play.jobs.On;

import java.util.List;

@On("cron.verificarAnalisesShape")
public class VerificarAnalisesShape extends GenericJob {

	@Override
	public void executar() throws Exception {

		//TODO VERIFICAR ID DE CONDICAO
		List<ProcessoManejo> processos = ProcessoManejo.find("objetoTramitavel.condicao.id", 22).fetch();

		for (ProcessoManejo processo : processos) {

			processo.verificarAnaliseShape();
		}
	}
}
