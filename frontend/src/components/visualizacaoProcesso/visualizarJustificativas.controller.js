var VisualizarJustificativasController = function ($uibModalInstance, parecer, idProcesso, desvinculoService) {

    var visualizarJustificativasCtlr = this;

    visualizarJustificativasCtlr.parecer = parecer;
    visualizarJustificativasCtlr.labelParecer = '';
    visualizarJustificativasCtlr.resultadoAnalise = app.utils.TiposResultadoAnalise;

    if (visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.DEFERIDO ||
        visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.PARECER_VALIDADO){
        
        visualizarJustificativasCtlr.labelParecer = 'Despacho';

    } else if (visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.INDEFERIDO ||
        visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.PARECER_NAO_VALIDADO){

        visualizarJustificativasCtlr.labelParecer = 'Justificativa';

    }else if (visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.EMITIR_NOTIFICACAO){

        visualizarJustificativasCtlr.labelParecer = 'Descrição da solicitação';

    } else if (visualizarJustificativasCtlr.parecer.tipoResultadoAnalise.id === visualizarJustificativasCtlr.resultadoAnalise.SOLICITAR_AJUSTES){

        visualizarJustificativasCtlr.labelParecer = 'Observações';

    } else {

        desvinculoService.buscarDesvinculoPeloProcesso(idProcesso)
            .then(function(response){

                var desvinculo = response.data;
                visualizarJustificativasCtlr.parecer.parecer = desvinculo.justificativa;
                visualizarJustificativasCtlr.labelParecer = 'Justificativa';

        });
    }

    visualizarJustificativasCtlr.fechar = function () {
        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.VisualizarJustificativasController = VisualizarJustificativasController;
