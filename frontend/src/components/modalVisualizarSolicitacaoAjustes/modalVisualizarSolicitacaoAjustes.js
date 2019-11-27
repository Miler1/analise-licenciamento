var VisualizarAjustesController = function (analiseGeo, $uibModalInstance) {

    var visualizarAjustesController = this;

    visualizarAjustesController.justificativa = _.findLast(analiseGeo.pareceresGerenteAnaliseGeo, function(parecer) {
        return parecer.tipoResultadoAnalise.id === app.utils.TiposResultadoAnalise.SOLICITAR_AJUSTES;
    });

    visualizarAjustesController.fechar = function() {
        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.VisualizarAjustesController= VisualizarAjustesController;
