var VisualizarJustificativasController = function ($uibModalInstance, idAcaoTramitacao,
                                                   analiseGeo, desvinculoService) {

var visualizarJustificativasCtlr = this;
visualizarJustificativasCtlr.enumAcaoTramitacao = app.utils.AcaoTramitacao;
visualizarJustificativasCtlr.analiseGeo = analiseGeo;
visualizarJustificativasCtlr.acaoTramitacao = idAcaoTramitacao;
visualizarJustificativasCtlr.labelParecer = '';

    if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.DEFERIR_ANALISE_GEO){

        visualizarJustificativasCtlr.parecer = _.find(analiseGeo.pareceresAnalistaGeo, function(parecer) {
            if(parecer.tipoResultadoAnalise.id === app.utils.TiposResultadoAnalise.DEFERIDO){
                return parecer.parecer;
            }
        });
        visualizarJustificativasCtlr.labelParecer = 'Despacho';

    }else if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.INDEFERIR_ANALISE_GEO){

        visualizarJustificativasCtlr.parecer = _.find(analiseGeo.pareceresAnalistaGeo, function(parecer) {
            if(parecer.tipoResultadoAnalise.id === app.utils.TiposResultadoAnalise.INDEFERIDO){
                return parecer.parecer;
            }
        });
        visualizarJustificativasCtlr.labelParecer = 'Justificativa';

    }else if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.EMITIR_NOTIFICACAO){

        visualizarJustificativasCtlr.parecer = _.find(analiseGeo.pareceresAnalistaGeo, function(parecer) {
            if(parecer.tipoResultadoAnalise.id === app.utils.TiposResultadoAnalise.EMITIR_NOTIFICACAO){
                return parecer.parecer;
            }
        });
        visualizarJustificativasCtlr.labelParecer = 'Descrição da solicitação';

    }else if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.SOLICITAR_DESVINCULO){

        desvinculoService.buscarDesvinculoPeloProcesso(analiseGeo.analise.processo.id)
            .then(function(response){
                visualizarJustificativasCtlr.parecer = _.find(analiseGeo.pareceresAnalistaGeo, function(parecer) {
                    if(parecer.tipoResultadoAnalise.id === app.utils.TiposResultadoAnalise.SOLICITAR_DESVINCULO){
                        return parecer.parecer;
                    }
                });
                visualizarJustificativasCtlr.labelParecer = 'Justificativa';
        });

    }else if (visualizarJustificativasCtlr.acaoTramitacao === visualizarJustificativasCtlr.enumAcaoTramitacao.SOLICITAR_AJUSTES){
        visualizarJustificativasCtlr.parecer = _.find(analiseGeo.pareceresGerenteAnaliseGeo, function(parecer) {
            if(parecer.tipoResultadoAnalise.id === app.utils.TiposResultadoAnalise.SOLICITAR_AJUSTES){
                return parecer.parecer;
            }
        });
        visualizarJustificativasCtlr.labelParecer = 'Justificativa';
    }

    visualizarJustificativasCtlr.fechar = function () {
        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.VisualizarJustificativasController = VisualizarJustificativasController;
