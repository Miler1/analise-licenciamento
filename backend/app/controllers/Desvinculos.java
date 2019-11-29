package controllers;


import exceptions.ValidacaoException;
import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import security.Acao;
import serializers.DesvinculoSerializar;
import utils.Mensagem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static controllers.InternalController.getUsuarioSessao;
import static controllers.InternalController.verificarPermissao;

public class Desvinculos extends GenericController {

    public static void solicitarDesvinculoAnaliseGeo(DesvinculoAnaliseGeo desvinculoAnaliseGeo) {

        returnIfNull(desvinculoAnaliseGeo, "Desvinculo");

        if(desvinculoAnaliseGeo.justificativa == null || desvinculoAnaliseGeo.justificativa.equals("")){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }

        if(desvinculoAnaliseGeo.dataSolicitacao == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculoAnaliseGeo.dataSolicitacao = c.getTime();
        }
        String siglaSetor = getUsuarioSessao().usuarioEntradaUnica.setorSelecionado.sigla;
        
        Gerente gerente = Gerente.distribuicaoAutomaticaGerente(siglaSetor, desvinculoAnaliseGeo.analiseGeo);

        gerente.save();

        desvinculoAnaliseGeo.gerente = UsuarioAnalise.findByGerente(gerente);

        desvinculoAnaliseGeo.analistaGeo =  getUsuarioSessao();

        desvinculoAnaliseGeo.save();

        desvinculoAnaliseGeo.analiseGeo = AnaliseGeo.findById(desvinculoAnaliseGeo.analiseGeo.id);
        desvinculoAnaliseGeo.analiseGeo.analise.processo.tramitacao.tramitar(desvinculoAnaliseGeo.analiseGeo.analise.processo, AcaoTramitacao.SOLICITAR_DESVINCULO, getUsuarioSessao(), desvinculoAnaliseGeo.gerente);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculoAnaliseGeo.analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

        renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }

    public static void buscarDesvinculoPeloProcessoGeo(Long idProcesso) {

        Processo processo = Processo.findById(idProcesso);

        DesvinculoAnaliseGeo desvinculoAnaliseGeo = DesvinculoAnaliseGeo.find("id_analise_geo = :id and id_usuario = :idUsuario")
                .setParameter("id", processo.analise.analisesGeo.get(0).id)
                .setParameter("idUsuario",processo.analise.analisesGeo.get(0).analistasGeo.get(0).usuario.id)
                .first();

        renderJSON(desvinculoAnaliseGeo, DesvinculoSerializar.list);
    }

    public static void responderSolicitacaoDesvinculoAnaliseGeo(DesvinculoAnaliseGeo desvinculoAnaliseGeo) {

        returnIfNull(desvinculoAnaliseGeo, "DesvinculoAnaliseGeo");

        if(desvinculoAnaliseGeo.justificativa == null ||
                desvinculoAnaliseGeo.justificativa.equals("") ||
                desvinculoAnaliseGeo.respostaGerente== null  ||
                desvinculoAnaliseGeo.respostaGerente.equals("") ||
                desvinculoAnaliseGeo.aprovada == null){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }

        if(desvinculoAnaliseGeo.dataSolicitacao == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculoAnaliseGeo.dataSolicitacao = c.getTime();
        }

        if(desvinculoAnaliseGeo.dataResposta == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculoAnaliseGeo.dataResposta = c.getTime();
        }

        if(desvinculoAnaliseGeo.aprovada) {

            desvinculoAnaliseGeo.analistaGeoDestino = UsuarioAnalise.findById(desvinculoAnaliseGeo.analistaGeoDestino.id);
            AnalistaGeo analistaGeo = AnalistaGeo.find("id_analise_geo = :id_analise_geo")
                    .setParameter("id_analise_geo", desvinculoAnaliseGeo.analiseGeo.id).first();
            analistaGeo.usuario = desvinculoAnaliseGeo.analistaGeoDestino;
            analistaGeo._save();

        }else {

            desvinculoAnaliseGeo.analistaGeoDestino = desvinculoAnaliseGeo.analistaGeo;
        }

        DesvinculoAnaliseGeo desvinculoAlterar = DesvinculoAnaliseGeo.findById(desvinculoAnaliseGeo.id);
        desvinculoAlterar.update(desvinculoAnaliseGeo);

        desvinculoAnaliseGeo.analiseGeo = AnaliseGeo.findById(desvinculoAnaliseGeo.analiseGeo.id);
        if(desvinculoAnaliseGeo.aprovada) {
            desvinculoAnaliseGeo.analiseGeo.analise.processo.tramitacao.tramitar(desvinculoAnaliseGeo.analiseGeo.analise.processo, AcaoTramitacao.APROVAR_SOLICITACAO_DESVINCULO, getUsuarioSessao(), desvinculoAnaliseGeo.analistaGeoDestino);
        } else {
            desvinculoAnaliseGeo.analiseGeo.analise.processo.tramitacao.tramitar(desvinculoAnaliseGeo.analiseGeo.analise.processo, AcaoTramitacao.NEGAR_SOLICITACAO_DESVINCULO, getUsuarioSessao(), desvinculoAnaliseGeo.analistaGeo);
        }
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculoAnaliseGeo.analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

        renderText(Mensagem.DESVINCULO_RESPONDIDO_COM_SUCESSO.getTexto());

    }

    public static void solicitarDesvinculoAnaliseTecnica(DesvinculoAnaliseTecnica desvinculoAnaliseTecnica) {

        returnIfNull(desvinculoAnaliseTecnica, "Desvinculo");

        if(desvinculoAnaliseTecnica.justificativa == null || desvinculoAnaliseTecnica.justificativa.equals("")){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }

        if(desvinculoAnaliseTecnica.dataSolicitacao == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculoAnaliseTecnica.dataSolicitacao = c.getTime();
        }
        String siglaSetor = getUsuarioSessao().usuarioEntradaUnica.setorSelecionado.sigla;

        Gerente gerente = Gerente.distribuicaoAutomaticaGerenteAnaliseTecnica(siglaSetor, desvinculoAnaliseTecnica.analiseTecnica);

        gerente.save();

        desvinculoAnaliseTecnica.gerente = UsuarioAnalise.findByGerente(gerente);

        desvinculoAnaliseTecnica.analistaTecnico =  getUsuarioSessao();

        desvinculoAnaliseTecnica.save();

        desvinculoAnaliseTecnica.analiseTecnica = AnaliseTecnica.findById(desvinculoAnaliseTecnica.analiseTecnica.id);
        desvinculoAnaliseTecnica.analiseTecnica.analise.processo.tramitacao.tramitar(desvinculoAnaliseTecnica.analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_DESVINCULO, getUsuarioSessao(), desvinculoAnaliseTecnica.gerente);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculoAnaliseTecnica.analiseTecnica.analise.processo.objetoTramitavel.id), getUsuarioSessao());

        renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }

    public static void buscarDesvinculoPeloProcessoTecnico(Long idProcesso) {

        Processo processo = Processo.findById(idProcesso);

        DesvinculoAnaliseTecnica desvinculoAnaliseTecnica = DesvinculoAnaliseTecnica.find("id_analise_tecnica = :id and id_usuario = :idUsuario")
                .setParameter("id", processo.analise.analisesTecnicas.get(0).id)
                .setParameter("idUsuario",processo.analise.analisesTecnicas.get(0).analistaTecnico.usuario.id)
                .first();

        renderJSON(desvinculoAnaliseTecnica, DesvinculoSerializar.list);
    }

    public static void responderSolicitacaoDesvinculoAnaliseTecnica(DesvinculoAnaliseTecnica desvinculoAnaliseTecnica) {

        returnIfNull(desvinculoAnaliseTecnica, "Desvinculo");

        if(desvinculoAnaliseTecnica.justificativa == null ||
                desvinculoAnaliseTecnica.justificativa.equals("") ||
                desvinculoAnaliseTecnica.respostaGerente== null  ||
                desvinculoAnaliseTecnica.respostaGerente.equals("") ||
                desvinculoAnaliseTecnica.aprovada == null){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }

        if(desvinculoAnaliseTecnica.dataSolicitacao == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculoAnaliseTecnica.dataSolicitacao = c.getTime();
        }

        if(desvinculoAnaliseTecnica.dataResposta == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            desvinculoAnaliseTecnica.dataResposta = c.getTime();
        }

        if(desvinculoAnaliseTecnica.aprovada) {
            desvinculoAnaliseTecnica.analistaTecnicoDestino = UsuarioAnalise.findById(desvinculoAnaliseTecnica.analistaTecnicoDestino.id);
            AnalistaGeo analistaGeo = AnalistaGeo.find("id_analise_geo = :id_analise_geo")
                    .setParameter("id_analise_geo", desvinculoAnaliseTecnica.analiseTecnica.id).first();
            analistaGeo.usuario = desvinculoAnaliseTecnica.analistaTecnicoDestino;
            analistaGeo._save();
        }

        DesvinculoAnaliseTecnica desvinculoAlterar = DesvinculoAnaliseTecnica.findById(desvinculoAnaliseTecnica.id);

        desvinculoAlterar.update(desvinculoAnaliseTecnica);

        desvinculoAnaliseTecnica.analiseTecnica = AnaliseGeo.findById(desvinculoAnaliseTecnica.analiseTecnica.id);

        if(desvinculoAnaliseTecnica.aprovada) {

            desvinculoAnaliseTecnica.analiseTecnica.analise.processo.tramitacao.tramitar(desvinculoAnaliseTecnica.analiseTecnica.analise.processo, AcaoTramitacao.APROVAR_SOLICITACAO_DESVINCULO, getUsuarioSessao(), desvinculoAnaliseTecnica.analistaTecnicoDestino);
        } else {

            desvinculoAnaliseTecnica.analiseTecnica.analise.processo.tramitacao.tramitar(desvinculoAnaliseTecnica.analiseTecnica.analise.processo, AcaoTramitacao.NEGAR_SOLICITACAO_DESVINCULO, getUsuarioSessao(), desvinculoAnaliseTecnica.analistaTecnico);

        }
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculoAnaliseTecnica.analiseTecnica.analise.processo.objetoTramitavel.id), getUsuarioSessao());

        renderText(Mensagem.DESVINCULO_RESPONDIDO_COM_SUCESSO.getTexto());

    }

}
