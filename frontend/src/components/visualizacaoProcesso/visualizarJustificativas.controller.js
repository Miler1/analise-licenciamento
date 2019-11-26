var VisualizarJustificativasController = function ($uibModalInstance, 
                                                   parecer, idProcesso, desvinculoService) {

    var visualizarJustificativasCtlr = this;

    visualizarJustificativasCtlr.resultadoAnalise = app.utils.TiposResultadoAnalise;
    visualizarJustificativasCtlr.parecer = parecer;
    visualizarJustificativasCtlr.labelParecer = '';

    if(visualizarJustificativasCtlr.parecer.parecer === null || visualizarJustificativasCtlr.parecer.parecer === undefined){

        desvinculoService.buscarDesvinculoPeloProcesso(idProcesso)
            .then(function(response){
                visualizarJustificativasCtlr.justificativaDesvinculo = response.data.justificativa;
        });
         
    } else if (visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.DEFERIDO){
        
        visualizarJustificativasCtlr.labelParecer = 'Despacho';

    } else if (visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.INDEFERIDO){

        visualizarJustificativasCtlr.labelParecer = 'Justificativa';

    } else if (visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.EMITIR_NOTIFICACAO){
        
        visualizarJustificativasCtlr.labelParecer = 'Descrição da solicitação';

    }

    visualizarJustificativasCtlr.fechar = function () {
        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.VisualizarJustificativasController = VisualizarJustificativasController;
