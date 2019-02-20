package controllers;

import models.EntradaUnica.CodigoPerfil;
import models.Processo;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.portalSeguranca.UsuarioLicenciamento;
import security.Acao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

import java.util.List;

public class Analistas extends InternalController {

	public static void vincularAnaliseAnalistaTecnico(Long idUsuario, String justificativaCoordenador, Long... idsProcesso) {

		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);

		UsuarioLicenciamento analista = UsuarioLicenciamento.findById(idUsuario);

		UsuarioLicenciamento usuarioExecutor = getUsuarioSessao();

		for(Long idProcesso : idsProcesso) {

			Processo processo = Processo.findById(idProcesso);

			processo.vincularAnalista(analista, usuarioExecutor, justificativaCoordenador);

		}

		renderMensagem(Mensagem.ANALISTA_VINCULADO_SUCESSO);

	}

	public static void getAnalistaTecnico(Long idProcesso) {

		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);

		Processo processo = Processo.findById(idProcesso);

		List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacoes.get(0).atividadesCaracterizacao;

		TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao =
				TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);

		List<UsuarioLicenciamento> consultores = UsuarioLicenciamento.getUsuariosByPerfilSetor(CodigoPerfil.ANALISTA_TECNICO, tipoAtividadeCaracterizacao.siglaSetor);

		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

	public static void getAnalistaTecnicoPerfil() {

		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO, Acao.CONSULTAR_PROCESSO);

		List<UsuarioLicenciamento> consultores = UsuarioLicenciamento.getUsuariosByPerfil(CodigoPerfil.ANALISTA_TECNICO);

		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);

	}

	public static void getAnalistaTecnicoPerfilSetores(boolean isGerente) {

		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO, Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);

		UsuarioLicenciamento usuarioSessao = getUsuarioSessao();

		List<Integer> idsSetoresFilhos = null;
		List<UsuarioLicenciamento> pessoas = null;
		switch (usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) {
		/**
		 * Nível 1 corresponde aos filhos e nível 2 aos netos e assim por diante na hieraquia.
		 * Neste caso, colocamos o nível 2, pois aqui o aprovador está pesquisando e o setor dele
		 * é diretoria, então a hierarquia é Diretoria/Coordenadoria(1)/Gerência(2)
		 */
		case CodigoPerfil.APROVADOR:
			//TODO REFATORAR
			//idsSetoresFilhos = usuarioSessao.setorSelecionado.getIdsSetoresByNivel(2);
			pessoas = UsuarioLicenciamento.getUsuariosByPerfilSetores(CodigoPerfil.ANALISTA_TECNICO, idsSetoresFilhos);
			break;
		/**
		 * Nível 1 corresponde aos filhos e nível 2 aos netos e assim por diante na hieraquia.
		 * Neste caso, colocamos o nível 1, pois aqui o coordenador está pesquisando e o setor dele
		 * é cordenadoria, então a hierarquia é Coordenadoria/Gerência(1)
		 */
		case CodigoPerfil.COORDENADOR_TECNICO:
			//TODO REFATORAR
			//idsSetoresFilhos = usuarioSessao.setorSelecionado.getIdsSetoresByNivel(1);
			pessoas = UsuarioLicenciamento.getUsuariosByPerfilSetores(CodigoPerfil.ANALISTA_TECNICO, idsSetoresFilhos);
			break;
		/**
		 * No caso aqui seria o Gerente ou outros que estão no mesmo setor que os Analistas
		 */
		default:
			pessoas = UsuarioLicenciamento.getUsuariosByPerfilSetor(CodigoPerfil.ANALISTA_TECNICO, usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
			break;
		}

		renderJSON(pessoas, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}


}
