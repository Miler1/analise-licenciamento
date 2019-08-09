package controllers;


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

            desvinculo.analista = getUsuarioSessao();

            if(desvinculo.dataSolicitacao == null) {

                Calendar c = Calendar.getInstance();
                c.setTime(new Date());

                desvinculo.dataSolicitacao = c.getTime();
            }
            String siglaSetor = desvinculo.analista.usuarioEntradaUnica.setorSelecionado.sigla;
            desvinculo.gerente = Gerente.distribuicaoSolicitacaoDesvinculo(siglaSetor, desvinculo);
            desvinculo.save();
            Processo processoBanco = Processo.findById(desvinculo.processo.id);
            desvinculo.processo.tramitacao.tramitar(processoBanco, AcaoTramitacao.SOLICITAR_DESVINCULO, desvinculo.analista, desvinculo.gerente);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(processoBanco.objetoTramitavel.id), desvinculo.analista);

            renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }

}
