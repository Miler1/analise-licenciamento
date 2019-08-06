package controllers;


import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import security.Acao;
import utils.Mensagem;

import java.util.Calendar;
import java.util.Date;

import static controllers.InternalController.getUsuarioSessao;
import static controllers.InternalController.verificarPermissao;

public class Desvinculos extends GenericController {

    public static void solicitarDesvinculo(Long idProcesso, String justificativa) {


            Desvinculo desvinculo = new Desvinculo();
            desvinculo.idAnalistaGeo = getUsuarioSessao();
            desvinculo.justificativa = justificativa;
            desvinculo.idProcesso = Processo.findById(idProcesso);

            if(desvinculo.dataSolicitacao == null) {

                Calendar c = Calendar.getInstance();
                c.setTime(new Date());

                desvinculo.dataSolicitacao = c.getTime();
            }

            desvinculo.save();

            desvinculo.idProcesso.tramitacao.tramitar(desvinculo.idProcesso, AcaoTramitacao.SOLICITAR_DESVINCULO, desvinculo.idAnalistaGeo);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(desvinculo.idProcesso.objetoTramitavel.id), desvinculo.idAnalistaGeo);

            renderText(Mensagem.DESVINCULO_SOLICITADO_COM_SUCESSO.getTexto());

    }





}
