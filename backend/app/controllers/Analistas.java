package controllers;

import models.AnalistaGeo;
import models.AnalistaTecnico;
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

		verificarPermissao(Acao.VINCULAR_PROCESSO);

		UsuarioAnalise usuarioAnalise = UsuarioAnalise.findById(idUsuario);

		UsuarioAnalise analista = UsuarioAnalise.getUsuarioEntradaUnicaByLogin(usuarioAnalise.login);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		for(Long idProcesso : idsProcesso) {

			Processo processo = Processo.findById(idProcesso);

			processo.vincularAnalista(analista, usuarioExecutor, justificativaCoordenador);

		}

		renderMensagem(Mensagem.ANALISTA_VINCULADO_SUCESSO);

	}

	public static void findAllBySetor() {

		String setorUsuarioLogado = getUsuarioSessao().usuarioEntradaUnica.setorSelecionado.sigla;

		renderJSON(UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.ANALISTA_TECNICO, setorUsuarioLogado), UsuarioSerializer.getAnalistasTecnico);

	}

	public static void vincularAnaliseAnalistaGeo(Long idUsuario, String justificativaCoordenador, Long... idsProcesso) {

		verificarPermissao(Acao.VINCULAR_PROCESSO);

		UsuarioAnalise usuarioAnalise = UsuarioAnalise.findById(idUsuario);

		UsuarioAnalise analista = UsuarioAnalise.getUsuarioEntradaUnicaByLogin(usuarioAnalise.login);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		for(Long idProcesso : idsProcesso) {

			Processo processo = Processo.findById(idProcesso);

			processo.vincularAnalista(analista, usuarioExecutor, justificativaCoordenador);

		}

		renderMensagem(Mensagem.ANALISTA_VINCULADO_SUCESSO);

	}

	public static void getAnalistaTecnico(Long idProcesso) {

		verificarPermissao(Acao.VINCULAR_PROCESSO);

		Processo processo = Processo.findById(idProcesso);

		List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacao.atividadesCaracterizacao;

		TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao =
				TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);

		List<UsuarioAnalise> consultores = UsuarioAnalise.getUsuariosEntradaUnica(CodigoPerfil.ANALISTA_TECNICO, tipoAtividadeCaracterizacao.atividade.siglaSetor);

		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasCoordenadores);

	}

	public static void getAnalistaTecnicoPerfil() {

		verificarPermissao(Acao.VINCULAR_PROCESSO, Acao.CONSULTAR_PROCESSO);

		List<UsuarioAnalise> consultores = UsuarioAnalise.getUsuariosByPerfil(CodigoPerfil.ANALISTA_TECNICO);

		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasCoordenadores);

	}

	public static void getAnalistaGeo(Long idProcesso) {

		verificarPermissao(Acao.VINCULAR_PROCESSO);

		Processo processo = Processo.findById(idProcesso);

		List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacao.atividadesCaracterizacao;

		TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao =
				TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);

		List<UsuarioAnalise> consultores = UsuarioAnalise.getUsuariosEntradaUnica(CodigoPerfil.ANALISTA_GEO, tipoAtividadeCaracterizacao.atividade.siglaSetor);

		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasCoordenadores);

	}

	public static void getAnalistaGeoPerfil() {

		verificarPermissao(Acao.VINCULAR_PROCESSO, Acao.CONSULTAR_PROCESSO);

		List<UsuarioAnalise> consultores = UsuarioAnalise.getUsuariosByPerfil(CodigoPerfil.ANALISTA_GEO);

		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasCoordenadores);

	}

	public static void buscarAnalistasGeoByIdProcesso(Long idProcesso) {

		Processo processo = Processo.findById(idProcesso);

		String siglaSetor = processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor;

		List<UsuarioAnalise> analistasGeo = AnalistaGeo.buscarAnalistasGeoParaDesvinculo(siglaSetor, processo.analise.getAnaliseGeo().getAnalistaGeo().usuario.id);

		renderJSON(analistasGeo, UsuarioSerializer.getConsultoresAnalistasGeo);

	}

	public static void buscarAnalistasTecnicoByIdProcesso(Long idProcesso) {

		Processo processo = Processo.findById(idProcesso);

		String siglaSetor = processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor;

		List<UsuarioAnalise> analistasTecnico = AnalistaTecnico.buscarAnalistasTecnicoParaDesvinculo(siglaSetor, processo.analise.getAnaliseTecnica().analistaTecnico.usuario.id);

		renderJSON(analistasTecnico, UsuarioSerializer.getAnalistasTecnico);

	}

	public static void getAnalistaTecnicoPerfilSetores(boolean isCoordenador) {

//		verificarPermissao(Acao.VINCULAR_PROCESSO, Acao.VALIDAR_PARECERES);
//
//		UsuarioAnalise usuarioSessao = getUsuarioSessao();
//
//		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
//
//		List<String> siglasSetoresFilhos;
//		List<UsuarioAnalise> pessoas;
//
//		switch (usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) {
//
//			case CodigoPerfil.APROVADOR:
//
//				siglasSetoresFilhos = integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla,2);
//				pessoas = integracaoEntradaUnica.findUsuariosByPerfilAndSetores(CodigoPerfil.ANALISTA_TECNICO, siglasSetoresFilhos);
//
//				break;
//
//			case CodigoPerfil.COORDENADOR_TECNICO:
//
//				siglasSetoresFilhos = integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla,1);
//				pessoas = UsuarioAnalise.getUsuariosByPerfilSetores(CodigoPerfil.ANALISTA_TECNICO, siglasSetoresFilhos);
//				break;
//			/**
//			 * No caso aqui seria o Coordenador ou outros que estão no mesmo setor que os Analistas
//			 */
//			default:
//				pessoas = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.ANALISTA_TECNICO, usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
//				break;
//
//		}

		List<UsuarioAnalise> pessoas = UsuarioAnalise.findUsuariosByPerfil(CodigoPerfil.ANALISTA_TECNICO);

		renderJSON(pessoas, UsuarioSerializer.getConsultoresAnalistasCoordenadores);

	}

	public static void getAnalistaGeoPerfilSetores(boolean isCoordenador) {

		List<UsuarioAnalise> pessoas = UsuarioAnalise.findUsuariosByPerfil(CodigoPerfil.ANALISTA_GEO);

		renderJSON(pessoas, UsuarioSerializer.getConsultoresAnalistasCoordenadores);

	}

	public void getSetorByNivel(int nivel){

		String sigla = getUsuarioSessao().usuarioEntradaUnica.setorSelecionado.sigla;

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		renderJSON(integracaoEntradaUnica.getSiglasSetoresByNivel( sigla, 1));

	}

}
