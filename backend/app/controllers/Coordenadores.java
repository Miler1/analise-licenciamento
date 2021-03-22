package controllers;

import models.EntradaUnica.CodigoPerfil;
import models.Processo;
import models.UsuarioAnalise;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import security.Acao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

import java.util.List;

public class Coordenadores extends InternalController {

    public static void vincularAnaliseCoordenador(Long idUsuario, Long... idsProcesso) {

        verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);

        UsuarioAnalise usuarioAnalise = UsuarioAnalise.findById(idUsuario);

        UsuarioAnalise coordenador = UsuarioAnalise.getUsuarioEntradaUnicaByLogin(usuarioAnalise.login);

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        for (Long idProcesso : idsProcesso) {

            Processo processo = Processo.findById(idProcesso);

            processo.vincularCoordenador(coordenador, usuarioExecutor);

        }

        renderMensagem(Mensagem.COORDENADOR_VINCULADO_SUCESSO);

    }

    public static void getCoordenadoresByIdProcesso(Long idProcesso) {

        verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);

        Processo processo = Processo.findById(idProcesso);

        List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacao.atividadesCaracterizacao;

        TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao =
                TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);

        List<UsuarioAnalise> consultores = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.COORDENADOR, tipoAtividadeCaracterizacao.atividade.siglaSetor);

        renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasCoordenadores);

    }

    public static void getCoordenadores() {

        verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);

        List<UsuarioAnalise> coordenadores = UsuarioAnalise.getUsuariosByPerfil(CodigoPerfil.COORDENADOR);

        renderJSON(coordenadores, UsuarioSerializer.getConsultoresAnalistasCoordenadores);

    }

}