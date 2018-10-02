package controllers;

import models.manejoDigital.ProcessoManejo;
import security.Acao;
import serializers.ProcessoManejoSerializer;

public class ProcessosManejo extends InternalController {

	public static void save(ProcessoManejo processo) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(processo);

		// Para não replicar processos enquanto o serviço de alteração de status não existe no SIMLAM
		ProcessoManejo processoAntigo = ProcessoManejo.find("numeroProcesso", processo.numeroProcesso).first();

		if (processoAntigo != null) {

			processoAntigo.delete();
		}

		processo.save();

		// TODO Enviar requisição de alteração de status de EM_ANALISE para o SIMLAM

		renderJSON(processo, ProcessoManejoSerializer.save);
	}
}
