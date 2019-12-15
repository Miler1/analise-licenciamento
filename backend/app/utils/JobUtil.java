package utils;

import exceptions.JobException;
import jobs.GenericJob;
import play.Logger;

import java.util.Arrays;
import java.util.List;

public class JobUtil {

	private static final List<String> jobClassesNames = Arrays
			.asList("ProcessamentoCaracterizacaoEmAndamento",
					"ProcessamentoPrazos",
					"ProcessamentoProcessosEmAnalise",
					"ReenvioEmailJob",
					"VerificarAnalisesShape",
					"VerificarAnaliseVencida",
					"VerificarComunicado",
					"VerificarNotificacoes",
					"VerificarPrazoAtendimentoNotificacao");

	public static String callAll() throws Exception {

		Logger.info(Mensagem.JOB_INICIO_TODOS_MANUALMENTE.getTexto());

		for (String className : jobClassesNames) {

			JobUtil.callByClassName(className);

		}

		Logger.info(Mensagem.JOB_FIM_TODOS_MANUALMENTE.getTexto());

		return Mensagem.JOB_FIM_TODOS_EXECUTADOS_SUCESSO.getTexto();

	}

	public static String callByClassName(String className) throws Exception {

		Logger.info(Mensagem.JOB_INICIO_MANUALMENTE.getTexto());
		Class c;

		try {

			c = Class.forName(Configuracoes.JOBS_PACKAGE + className);

		} catch (ClassNotFoundException e) {

			throw new JobException(Mensagem.JOB_NAO_ENCONTRADO_ERRO.getTexto(className));

		}

		Object object = c.newInstance();

		if(object instanceof GenericJob) {

			((GenericJob) object).executar();

		} else {

			throw new JobException(Mensagem.JOB_ERRO_PADRAO.getTexto(className));

		}

		Logger.info(Mensagem.JOB_FIM_MANUALMENTE.getTexto());

		return Mensagem.JOB_EXECUTADO_SUCESSO.getTexto(className);

	}

}
