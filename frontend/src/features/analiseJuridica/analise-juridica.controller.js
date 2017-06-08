var AnaliseJuridicaController = function($rootScope, $scope, $routeParams, Upload, processo, analiseJuridica, documentoLicenciamentoService) {

    var ctrl = this;

    ctrl.processo = processo;
    ctrl.analiseJuridica = angular.copy(analiseJuridica);
    
    ctrl.documentosProcesso = angular.copy(ctrl.analiseJuridica
                                                .analise
                                                .processo
                                                .caracterizacoes[0]
                                                .documentosEnviados);

    ctrl.documentosParacer = angular.copy(ctrl.analiseJuridica.documentos);

    ctrl.upload = function(file) {

        console.log(file);
    };

    ctrl.downloadDocumentoLicenciamento = function(idDocumento) {

        documentoLicenciamentoService.download(idDocumento);
    };
    

};

exports.controllers.AnaliseJuridicaController = AnaliseJuridicaController;