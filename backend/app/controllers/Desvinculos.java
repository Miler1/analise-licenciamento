package controllers;


import exceptions.ValidacaoException;
import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import security.Acao;
import utils.Mensagem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
            desvinculo.gerente = Gerente.distribuicaoAutomaticaGerente(siglaSetor);

            desvinculo.save();

            AnaliseGeo analiseGeoBanco = AnaliseGeo.findById(desvinculo.analiseGeo.id);
            desvinculo.analiseGeo.analise.processo.tramitacao.tramitar(analiseGeoBanco.analise.processo, AcaoTramitacao.SOLICITAR_DESVINCULO, getUsuarioSessao(), desvinculo.gerente);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id), getUsuarioSessao());

            renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }

}
