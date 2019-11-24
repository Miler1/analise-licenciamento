var VisualizarAjustesController = function (analiseGeo, $uibModalInstance) {

var visualizarAjustesController = this;
visualizarAjustesController.analiseGeo = analiseGeo;
visualizarAjustesController.justificativa = analiseGeo.parecerValidacaoGerente;

visualizarAjustesController.fechar = function() {
    $uibModalInstance.dismiss('cancel');
};

};

exports.controllers.VisualizarAjustesController= VisualizarAjustesController;
