package controllers;


import exceptions.ValidacaoException;
import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import serializers.DesvinculoSerializar;
import utils.Configuracoes;
import utils.DateUtil;
import utils.Mensagem;

import java.util.Calendar;
import java.util.Date;

import static controllers.InternalController.getUsuarioSessao;
import static models.tramitacao.AcaoTramitacao.SOLICITAR_DESVINCULO;

public class Desvinculos extends GenericController {

    public static void solicitarDesvinculo(Desvinculo desvinculo) {

        returnIfNull(desvinculo, "Desvinculo");

        if(desvinculo.justificativa == null || desvinculo.justificativa.equals("")){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }

        if(desvinculo.dataSolicitacao == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculo.dataSolicitacao = c.getTime();
        }
        String siglaSetor = getUsuarioSessao().usuarioEntradaUnica.setorSelecionado.sigla;
        
        Gerente gerente = Gerente.distribuicaoAutomaticaGerente(siglaSetor, desvinculo.analiseGeo);

        gerente.save();

        desvinculo.gerente = UsuarioAnalise.findByGerente(gerente);

        desvinculo.analistaGeo =  getUsuarioSessao();

        desvinculo.save();

        desvinculo.analiseGeo = AnaliseGeo.findById(desvinculo.analiseGeo.id);
        desvinculo.analiseGeo.analise.processo.tramitacao.tramitar(desvinculo.analiseGeo.analise.processo, SOLICITAR_DESVINCULO, getUsuarioSessao(), desvinculo.gerente);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculo.analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

        renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }

    public static void buscarDesvinculoPeloProcesso(Long idProcesso) {

        Processo processo = Processo.findById(idProcesso);

        Desvinculo desvinculo = Desvinculo.find("id_analise_geo = :id and id_usuario = :idUsuario")
                .setParameter("id", processo.analise.analisesGeo.get(0).id)
                .setParameter("idUsuario",processo.analise.analisesGeo.get(0).analistasGeo.get(0).usuario.id)
                .first();

        renderJSON(desvinculo, DesvinculoSerializar.list);
    }

    public static void responderSolicitacaoDesvinculo(Desvinculo desvinculo) {

        returnIfNull(desvinculo, "Desvinculo");

        if(desvinculo.justificativa == null ||
                desvinculo.justificativa.equals("") ||
                desvinculo.respostaGerente== null  ||
                desvinculo.respostaGerente.equals("") ||
                desvinculo.aprovada == null){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }

        if(desvinculo.dataSolicitacao == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculo.dataSolicitacao = c.getTime();
        }

        if(desvinculo.dataResposta == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculo.dataResposta = c.getTime();
        }

        if(desvinculo.aprovada) {
            desvinculo.analistaGeoDestino = UsuarioAnalise.findById(desvinculo.analistaGeoDestino.id);
            AnalistaGeo analistaGeo = AnalistaGeo.find("id_analise_geo = :id_analise_geo")
                    .setParameter("id_analise_geo", desvinculo.analiseGeo.id).first();
            analistaGeo.usuario = desvinculo.analistaGeoDestino;
            analistaGeo._save();
        }

        Desvinculo desvinculoAlterar = Desvinculo.findById(desvinculo.id);
        desvinculoAlterar.update(desvinculo);

        desvinculo.analiseGeo = AnaliseGeo.findById(desvinculo.analiseGeo.id);

        if(desvinculo.aprovada) {

            desvinculo.analiseGeo.analise.processo.tramitacao.tramitar(desvinculo.analiseGeo.analise.processo, AcaoTramitacao.APROVAR_SOLICITACAO_DESVINCULO, getUsuarioSessao(), desvinculo.analistaGeoDestino);

            desvinculo.analiseGeo.dataVencimentoPrazo = DateUtil.somaDiasEmData(new Date(), Configuracoes.PRAZO_ANALISE_GEO);
            desvinculo.analiseGeo.analise.diasAnalise.preencheDiasAnaliseGeo();
            desvinculo.analiseGeo.analise.diasAnalise._save();
            desvinculo.analiseGeo._save();

        } else {

            desvinculo.analiseGeo.analise.processo.tramitacao.tramitar(desvinculo.analiseGeo.analise.processo, AcaoTramitacao.NEGAR_SOLICITACAO_DESVINCULO, getUsuarioSessao(), desvinculo.analistaGeo);

            desvinculo.analiseGeo.dataVencimentoPrazo = DateUtil.somaDiasEmData(DateUtil.somaDiasEmData(desvinculo.analiseGeo.dataCadastro, Configuracoes.PRAZO_ANALISE_GEO) , DiasAnalise.intervalosTramitacoesAnaliseGeo(desvinculo.analiseGeo.analise.processo.getHistoricoTramitacao()));
            desvinculo.analiseGeo.analise.diasAnalise.preencheDiasAnaliseGeo();
            desvinculo.analiseGeo.analise.diasAnalise._save();
            desvinculo.analiseGeo._save();

        }

        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculo.analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

        renderText(Mensagem.DESVINCULO_RESPONDIDO_COM_SUCESSO.getTexto());

    }

}
