package controllers;

import exceptions.ValidacaoException;
import models.manejoDigital.AnaliseManejo;
import models.manejoDigital.ProcessoManejo;
import models.portalSeguranca.Usuario;
import security.Acao;
import serializers.ProcessoManejoSerializer;
import utils.Mensagem;

public class ProcessosManejo extends InternalController {

	public static void save(ProcessoManejo processo) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(processo);

		// Para não replicar processos enquanto o serviço de alteração de status não existe no SIMLAM
		ProcessoManejo processoAntigo = ProcessoManejo.find("numeroProcesso", processo.numeroProcesso).first();

		if (processoAntigo != null) {

			if (processoAntigo.analiseManejo != null &&
					!processoAntigo.analiseManejo.usuario.id.equals(getUsuarioSessao().id)) {

				throw new ValidacaoException(Mensagem.PROCESSO_ANALISE_USUARIO_DIFERENTE);
			}

			renderJSON(processoAntigo, ProcessoManejoSerializer.save);

		} else {

			processo.save();

			// TODO Enviar requisição de alteração de status de EM_ANALISE para o SIMLAM

			renderJSON(processo, ProcessoManejoSerializer.save);
		}
	}

	public static void findById(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO, Acao.VISUALIZAR_PROCESSO_MANEJO);

		notFoundIfNull(id);

		ProcessoManejo processo = ProcessoManejo.findById(id);

		renderJSON(processo, ProcessoManejoSerializer.findById);
	}

	public static void iniciarAnalise(ProcessoManejo processo) {


		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		notFoundIfNull(processo);

		// TODO enviar processo para analise na imagem
		processo.analiseManejo = AnaliseManejo.gerarAnalise(processo,
				(Usuario) Usuario.findById(getUsuarioSessao().id));

		ProcessoManejo processoAntigo = ProcessoManejo.findById(processo.id);
		processoAntigo = processoAntigo.iniciarAnalise(processo);

		renderJSON(processoAntigo, ProcessoManejoSerializer.iniciarAnalise);
	}


}
