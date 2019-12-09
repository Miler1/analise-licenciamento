var VisualizarAjustesController = function (justificativa, $uibModalInstance) {

    var visualizarAjustesController = this;

    visualizarAjustesController.justificativa = justificativa;

    visualizarAjustesController.fechar = function() {
        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.VisualizarAjustesController= VisualizarAjustesController;
