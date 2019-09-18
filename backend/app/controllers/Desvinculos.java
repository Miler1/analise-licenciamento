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
//        Gerente gerente = UsuarioAnalise.findByGerente(Gerente.distribuicaoAutomaticaGerente(siglaSetor, desvinculo.analiseGeo));
        Gerente gerente = Gerente.distribuicaoAutomaticaGerente(siglaSetor, desvinculo.analiseGeo);

        gerente.save();

        desvinculo.gerente = UsuarioAnalise.findByGerente(gerente);

        desvinculo.analistaGeo =  getUsuarioSessao();

        desvinculo.save();

        desvinculo.analiseGeo = AnaliseGeo.findById(desvinculo.analiseGeo.id);
        desvinculo.analiseGeo.analise.processo.tramitacao.tramitar(desvinculo.analiseGeo.analise.processo, AcaoTramitacao.SOLICITAR_DESVINCULO, getUsuarioSessao(), desvinculo.gerente);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculo.analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

        renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }

    public static void buscarDesvinculoPeloProcesso(Long idProcesso) {

        Processo processo = Processo.findById(idProcesso);

        Desvinculo desvinculo = Desvinculo.find("id_analise_geo = :id and id_usuario = :idUsuario")
                .setParameter("id", processo.analises.get(0).analisesGeo.get(0).id)
                .setParameter("idUsuario",processo.analises.get(0).analisesGeo.get(0).analistasGeo.get(0).usuario.id)
                .first();

        renderJSON(desvinculo, DesvinculoSerializar.list);
    }

    public static void buscarAnalistasGeo(Long idProcesso) {

        Processo processo = Processo.findById(idProcesso);

        String siglaSetor = processo.getCaracterizacao().atividadesCaracterizacao.get(0).atividade.siglaSetor;

        List<UsuarioAnalise> analistasGeo = AnalistaGeo.buscarAnalistasGeo(siglaSetor);

        renderJSON(analistasGeo);
    }

    public static void respondersolicitacaoDesvinculo(Desvinculo desvinculo) {

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
        } else {
            desvinculo.analiseGeo.analise.processo.tramitacao.tramitar(desvinculo.analiseGeo.analise.processo, AcaoTramitacao.NEGAR_SOLICITACAO_DESVINCULO, getUsuarioSessao(), desvinculo.analistaGeo);
        }
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculo.analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

        renderText(Mensagem.DESVINCULO_RESPONDIDO_COM_SUCESSO.getTexto());

    }

}
