package controllers;

import models.AnaliseTecnica;
import models.ParecerAnalistaTecnico;
import models.Processo;
import models.UsuarioAnalise;
import security.Acao;
import serializers.ParecerAnalistaGeoSerializer;
import serializers.ParecerAnalistaTecnicoSerializer;
import utils.Mensagem;

import javax.validation.ValidationException;
import java.util.Comparator;
import java.util.List;

public class PareceresAnalistasTecnico extends InternalController {

	public static void concluir(ParecerAnalistaTecnico parecerAnalistaTecnico) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		if(parecerAnalistaTecnico.id != null) {

			parecerAnalistaTecnico.finalizarSolicitacaoAjuste(usuarioExecutor);

		} else {

			parecerAnalistaTecnico.finalizar(usuarioExecutor);

		}

		renderMensagem(Mensagem.ANALISE_FINALIZADA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerAnalistaTecnico parecerAnalistaTecnico = ParecerAnalistaTecnico.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerAnalistaTecnico, ParecerAnalistaTecnicoSerializer.findByIdHistoricoTramitacao);

	}

	public static void findByNumeroProcesso() {

		String numeroProcesso = getParamAsString("numeroProcesso");

		AnaliseTecnica analiseTecnica = AnaliseTecnica.find("analise.processo.numero = :numeroProcesso ORDER BY id DESC")
				.setParameter("numeroProcesso", numeroProcesso)
				.first();

		if(analiseTecnica == null || analiseTecnica.pareceresAnalistaTecnico == null ||  analiseTecnica.pareceresAnalistaTecnico.isEmpty()) {

			renderMensagem(Mensagem.PARECER_NAO_ENCONTRADO);

		} else if(!analiseTecnica.inconsistenciasTecnica.isEmpty()) {

			renderMensagem(Mensagem.CLONAR_PARECER_COM_INCONSISTENCIA);

		} else {

			renderJSON(ParecerAnalistaTecnico.getUltimoParecer(analiseTecnica.pareceresAnalistaTecnico), ParecerAnalistaTecnicoSerializer.findByIdNumeroProcesso);

		}
	}
	
	public static void findParecerByIdProcesso(Long idProcesso) {

		Processo processo = Processo.findById(idProcesso);

		renderJSON(ParecerAnalistaTecnico.findParecerByProcesso(processo), ParecerAnalistaTecnicoSerializer.findByIdProcesso);
	}

	public static void getUltimoParecerAnaliseTecnica(Long id) {

		AnaliseTecnica analiseTecnica = new AnaliseTecnica();

		if (id != null) {

			analiseTecnica = AnaliseTecnica.findById(id);

			renderJSON(analiseTecnica.pareceresAnalistaTecnico.stream().max(Comparator.comparing(ParecerAnalistaTecnico::getDataParecer)).orElseThrow(ValidationException::new), ParecerAnalistaTecnicoSerializer.findByIdHistoricoTramitacao);

		}

	}

}
