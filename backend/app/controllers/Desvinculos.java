package controllers;

import models.*;
import security.Acao;
import serializers.DesvinculoSerializar;
import utils.Mensagem;
import static controllers.InternalController.getUsuarioSessao;
import static controllers.InternalController.verificarPermissao;

public class Desvinculos extends GenericController {

    public static void solicitarDesvinculoAnaliseGeo(DesvinculoAnaliseGeo desvinculoAnaliseGeo) {

        verificarPermissao(Acao.SOLICITAR_DESVINCULO_GEO);

        returnIfNull(desvinculoAnaliseGeo, "DesvinculoAnaliseGeo");

        desvinculoAnaliseGeo.solicitaDesvinculoAnaliseGeo( getUsuarioSessao() );

        renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }

    public static void buscarDesvinculoPeloProcessoGeo(Long idProcesso) {

        Processo processo = Processo.findById(idProcesso);

        renderJSON(processo.buscaDesvinculoPeloProcessoGeo(), DesvinculoSerializar.list);
    }

    public static void responderSolicitacaoDesvinculoAnaliseGeo(DesvinculoAnaliseGeo desvinculoAnaliseGeo) {

        verificarPermissao(Acao.RESPONDER_SOLICITACAO_DESVINCULO);

        returnIfNull(desvinculoAnaliseGeo, "DesvinculoAnaliseGeo");

        desvinculoAnaliseGeo.respondeSolicitacaoDesvinculoAnaliseGeo( getUsuarioSessao() );

        renderText(Mensagem.DESVINCULO_RESPONDIDO_COM_SUCESSO.getTexto());

    }

    public static void solicitarDesvinculoAnaliseTecnica(DesvinculoAnaliseTecnica desvinculoAnaliseTecnica) {

        verificarPermissao(Acao.SOLICITAR_DESVINCULO_TECNICO);

        returnIfNull(desvinculoAnaliseTecnica, "DesvinculoAnaliseTecnica");

        desvinculoAnaliseTecnica.solicitaDesvinculoSAnaliseTecnica( getUsuarioSessao() );

        renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }

    public static void buscarDesvinculoPeloProcessoTecnico(Long idProcesso) {

        Processo processo = Processo.findById(idProcesso);

        renderJSON(processo.buscaDesvinculoPeloProcessoTecnico(), DesvinculoSerializar.list);
    }

    public static void responderSolicitacaoDesvinculoAnaliseTecnica(DesvinculoAnaliseTecnica desvinculoAnaliseTecnica) {

        verificarPermissao(Acao.RESPONDER_SOLICITACAO_DESVINCULO);

        returnIfNull(desvinculoAnaliseTecnica, "DesvinculoAnaliseTecnica");

        desvinculoAnaliseTecnica.respondeSolicitacaoDesvinculoAnaliseTecnica( getUsuarioSessao() );

        renderText(Mensagem.DESVINCULO_RESPONDIDO_COM_SUCESSO.getTexto());

    }

}
