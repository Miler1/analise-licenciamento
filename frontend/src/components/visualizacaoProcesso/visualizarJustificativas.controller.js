var VisualizarJustificativasController = function ($uibModalInstance, 
                                                   analiseGeo, desvinculoService) {

var visualizarJustificativasCtlr = this;

visualizarJustificativasCtlr.resultadoAnalise = app.utils.TiposResultadoAnalise;
visualizarJustificativasCtlr.analiseGeo = analiseGeo;

    if(visualizarJustificativasCtlr.analiseGeo.despacho === null || visualizarJustificativasCtlr.analiseGeo.despacho === undefined){

        desvinculoService.buscarDesvinculoPeloProcesso(analiseGeo.analise.processo.id)
            .then(function(response){
                visualizarJustificativasCtlr.justificativaDesvinculo = response.data.justificativa;
        });
         
    }else if (visualizarJustificativasCtlr.analiseGeo.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.DEFERIDO){
        visualizarJustificativasCtlr.despacho = visualizarJustificativasCtlr.analiseGeo.despacho;

    }else if (visualizarJustificativasCtlr.analiseGeo.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.INDEFERIDO){
        visualizarJustificativasCtlr.justificativa = visualizarJustificativasCtlr.analiseGeo.despacho;

    }else if (visualizarJustificativasCtlr.analiseGeo.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.EMITIR_NOTIFICACAO){
        visualizarJustificativasCtlr.descricaoSolicitacao = visualizarJustificativasCtlr.analiseGeo.despacho;

    }

    visualizarJustificativasCtlr.fechar = function () {
        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.VisualizarJustificativasController = VisualizarJustificativasController;
