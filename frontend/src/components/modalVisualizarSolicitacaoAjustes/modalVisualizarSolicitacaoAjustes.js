var VisualizarAjustesController = function (analiseGeo, $uibModalInstance) {

var visualizarAjustesController = this;

    visualizarAjustesController.justificativa = _.find(analiseGeo.pareceresGerenteAnaliseGeo, function(parecer) {
        if(parecer.tipoResultadoAnalise.id === app.utils.TiposResultadoAnalise.SOLICITAR_AJUSTES){
            return parecer.parecer;
        }
    });

visualizarAjustesController.fechar = function() {
    $uibModalInstance.dismiss('cancel');
};

};

exports.controllers.VisualizarAjustesController= VisualizarAjustesController;
