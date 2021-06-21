package controllers;

import models.EntradaUnica.CodigoPerfil;
import models.UsuarioAnalise;
import models.tramitacao.Condicao;
import security.Acao;
import security.Auth;
import serializers.CondicaoSerializer;
import utils.Configuracoes;

import java.util.Arrays;
import java.util.List;

public class Condicoes extends InternalController {

    public static void list() {

        verificarPermissao(Acao.CONSULTAR_PROCESSO);
        UsuarioAnalise user = Auth.getUsuarioSessao();
        List<Condicao> condicoesVisiveis = null;

        switch (user.usuarioEntradaUnica.perfilSelecionado.codigo) {
            case CodigoPerfil.COORDENADOR:

                condicoesVisiveis = Condicao.find("idCondicao in (:idsCondicoes)")
                        .setParameter("idsCondicoes", Arrays.asList(Condicao.NOTIFICADO_PELO_ANALISTA_GEO,
                                Condicao.AGUARDANDO_ANALISE_GEO,
                                Condicao.AGUARDANDO_ANALISE_TECNICA,
                                Condicao.EM_ANALISE_GEO,
                                Condicao.EM_ANALISE_TECNICA,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO,
                                Condicao.AGUARDANDO_RESPOSTA_COMUNICADO,
                                Condicao.AGUARDANDO_VALIDACAO_GEO_PELO_COORDENADOR,
                                Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR,
                                Condicao.ARQUIVADO,
                                Condicao.AGUARDANDO_RESPOSTA_JURIDICA,
                                Condicao.NOTIFICADO_PELO_ANALISTA_TECNICO,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA
                        ))
                        .fetch();
                break;
            case CodigoPerfil.COORDENADOR_GEO:

                condicoesVisiveis = Condicao.find("idCondicao in (:idsCondicoes)")
                        .setParameter("idsCondicoes", Arrays.asList(Condicao.NOTIFICADO_PELO_ANALISTA_GEO,
                                Condicao.AGUARDANDO_ANALISE_GEO,
                                Condicao.AGUARDANDO_ANALISE_TECNICA,
                                Condicao.EM_ANALISE_GEO,
                                Condicao.EM_ANALISE_TECNICA,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO,
                                Condicao.AGUARDANDO_RESPOSTA_COMUNICADO,
                                Condicao.AGUARDANDO_VALIDACAO_GEO_PELO_COORDENADOR,
                                Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR,
                                Condicao.ARQUIVADO,
                                Condicao.AGUARDANDO_RESPOSTA_JURIDICA,
                                Condicao.NOTIFICADO_PELO_ANALISTA_TECNICO,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA
                        ))
                        .fetch();
                break;
            case CodigoPerfil.ANALISTA_GEO:

                condicoesVisiveis = Condicao.find("idCondicao in (:idsCondicoes)")
                        .setParameter("idsCondicoes", Arrays.asList(Condicao.AGUARDANDO_ANALISE_GEO,
                                Condicao.EM_ANALISE_GEO,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO,
                                Condicao.NOTIFICADO_PELO_ANALISTA_GEO,
                                Condicao.AGUARDANDO_RESPOSTA_COMUNICADO,
                                Condicao.ARQUIVADO
                        ))
                        .fetch();
                break;
            case CodigoPerfil.ANALISTA_TECNICO:

                condicoesVisiveis = Condicao.find("idCondicao in (:idsCondicoes)")
                        .setParameter("idsCondicoes", Arrays.asList(Condicao.AGUARDANDO_ANALISE_TECNICA,
                                Condicao.EM_ANALISE_TECNICA,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA,
                                Condicao.NOTIFICADO_PELO_ANALISTA_TECNICO,
                                Condicao.AGUARDANDO_RESPOSTA_JURIDICA,
                                Condicao.ARQUIVADO
                        ))
                        .fetch();
                break;
            case CodigoPerfil.DIRETOR:

                condicoesVisiveis = Condicao.find("idCondicao in (:idsCondicoes)")
                        .setParameter("idsCondicoes", Arrays.asList(Condicao.NOTIFICADO_PELO_ANALISTA_GEO,
                                Condicao.AGUARDANDO_ANALISE_GEO,
                                Condicao.AGUARDANDO_ANALISE_TECNICA,
                                Condicao.EM_ANALISE_GEO,
                                Condicao.EM_ANALISE_TECNICA,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO,
                                Condicao.AGUARDANDO_RESPOSTA_COMUNICADO,
                                Condicao.AGUARDANDO_VALIDACAO_GEO_PELO_COORDENADOR,
                                Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR,
                                Condicao.ARQUIVADO,
                                Condicao.NOTIFICADO_PELO_ANALISTA_TECNICO,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA
                        ))
                        .fetch();
                break;
            case CodigoPerfil.SECRETARIO:

                condicoesVisiveis = Condicao.find("idCondicao in (:idsCondicoes)")
                        .setParameter("idsCondicoes", Arrays.asList(Condicao.NOTIFICADO_PELO_ANALISTA_GEO,
                                Condicao.AGUARDANDO_ANALISE_GEO,
                                Condicao.AGUARDANDO_ANALISE_TECNICA,
                                Condicao.EM_ANALISE_GEO,
                                Condicao.EM_ANALISE_TECNICA,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO,
                                Condicao.AGUARDANDO_RESPOSTA_COMUNICADO,
                                Condicao.AGUARDANDO_VALIDACAO_GEO_PELO_COORDENADOR,
                                Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR,
                                Condicao.AGUARDANDO_VALIDACAO_DIRETORIA,
                                Condicao.EM_ANALISE_DIRETOR,
                                Condicao.ARQUIVADO,
                                Condicao.NOTIFICADO_PELO_ANALISTA_TECNICO,
                                Condicao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA
                        ))
                        .fetch();
                break;
        }

        renderJSON(condicoesVisiveis, CondicaoSerializer.list);

    }

    public static void listManejo() {

        verificarPermissao(Acao.LISTAR_PROCESSO_MANEJO);

        List<Condicao> condicoes = Condicao.find("idEtapa", Configuracoes.TRAMITACAO_ETAPA_MANEJO).fetch();

        renderJSON(condicoes, CondicaoSerializer.listManejo);

    }

}