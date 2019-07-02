package controllers;

import models.EntradaUnica.CodigoPerfil;
import models.Processo;
import models.UsuarioAnalise;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import security.Acao;
import serializers.UsuarioSerializer;
import services.IntegracaoEntradaUnicaService;
import utils.Mensagem;

import java.util.List;

public class Analistas extends InternalController {

	public static void vincularAnaliseAnalistaTecnico(Long idUsuario, String justificativaCoordenador, Long... idsProcesso) {

		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);

		//TODO ANALISAR - PH

		UsuarioAnalise usuarioAnalise = UsuarioAnalise.findById(idUsuario);

		UsuarioAnalise analista = UsuarioAnalise.getUsuarioByLogin(usuarioAnalise.login);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

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

		List<UsuarioAnalise> consultores = UsuarioAnalise.getUsuariosByPerfilSetor(CodigoPerfil.ANALISTA_TECNICO, tipoAtividadeCaracterizacao.atividade.siglaSetor);

		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

	public static void getAnalistaTecnicoPerfil() {

		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO, Acao.CONSULTAR_PROCESSO);

		List<UsuarioAnalise> consultores = UsuarioAnalise.getUsuariosByPerfil(CodigoPerfil.ANALISTA_TECNICO);

		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);

	}

	public static void getAnalistaTecnicoPerfilSetores(boolean isGerente) {

		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO, Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);

		UsuarioAnalise usuarioSessao = getUsuarioSessao();

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		List<String> siglasSetoresFilhos = null;
		List<UsuarioAnalise> pessoas = null;
		switch (usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) {

		case CodigoPerfil.APROVADOR:

			siglasSetoresFilhos = integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla,2);
			pessoas = integracaoEntradaUnica.findUsuariosByPerfilAndSetores(CodigoPerfil.ANALISTA_TECNICO, siglasSetoresFilhos);

			break;

		case CodigoPerfil.COORDENADOR_TECNICO:

			siglasSetoresFilhos = integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla,1);
			pessoas = UsuarioAnalise.getUsuariosByPerfilSetores(CodigoPerfil.ANALISTA_TECNICO, siglasSetoresFilhos);
			break;
		/**
		 * No caso aqui seria o Gerente ou outros que estão no mesmo setor que os Analistas
		 */
		default:
			pessoas = UsuarioAnalise.getUsuariosByPerfilSetor(CodigoPerfil.ANALISTA_TECNICO, usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
			break;
		}

		renderJSON(pessoas, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

	public void getSetorByNivel(int nivel){

		String sigla = getUsuarioSessao().usuarioEntradaUnica.setorSelecionado.sigla;

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		renderJSON(integracaoEntradaUnica.getSiglasSetoresByNivel( sigla, 1));
	}


}
