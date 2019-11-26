var VisualizarJustificativasController = function ($uibModalInstance, idAcaoTramitacao,
                                                   analiseGeo, desvinculoService) {

var visualizarJustificativasCtlr = this;
visualizarJustificativasCtlr.enumAcaoTramitacao = app.utils.AcaoTramitacao;
visualizarJustificativasCtlr.analiseGeo = analiseGeo;
visualizarJustificativasCtlr.acaoTramitacao = idAcaoTramitacao;

    if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.DEFERIR_ANALISE_GEO){
        visualizarJustificativasCtlr.despacho = visualizarJustificativasCtlr.analiseGeo.despacho;

    }else if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.INDEFERIR_ANALISE_GEO){
        visualizarJustificativasCtlr.justificativa = visualizarJustificativasCtlr.analiseGeo.despacho;

    }else if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.EMITIR_NOTIFICACAO){
        visualizarJustificativasCtlr.descricaoSolicitacao = visualizarJustificativasCtlr.analiseGeo.despacho;

    }else if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.SOLICITAR_DESVINCULO){

        desvinculoService.buscarDesvinculoPeloProcesso(analiseGeo.analise.processo.id)
            .then(function(response){
                visualizarJustificativasCtlr.justificativaDesvinculo = response.data.justificativa;
        });

    }else if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.SOLICITAR_AJUSTES){
        visualizarJustificativasCtlr.observacoes = visualizarJustificativasCtlr.analiseGeo.parecerValidacaoGerente;
    }

    visualizarJustificativasCtlr.fechar = function () {
        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.VisualizarJustificativasController = VisualizarJustificativasController;
