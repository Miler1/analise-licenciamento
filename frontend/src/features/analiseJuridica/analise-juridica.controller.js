var AnaliseJuridicaController = function($rootScope, $scope, $routeParams, processo, 
        analiseJuridica, documentoLicenciamentoService, uploadService) {

    var ctrl = this;

    ctrl.processo = processo;
    ctrl.analiseJuridica = angular.copy(analiseJuridica);
    
    ctrl.documentosProcesso = angular.copy(ctrl.analiseJuridica
                                                .analise
                                                .processo
                                                .caracterizacoes[0]
                                                .documentosEnviados);

    ctrl.documentosParecer = angular.copy(ctrl.analiseJuridica.documentos || []);

    ctrl.upload = function(file) {

        console.log(file);
        uploadService.save(file)
            .then(function(response) {

                ctrl.documentosParecer.push({

                    key: response,
                    nome: file.name
                });

                console.log(response.data);
            });
    };

    ctrl.downloadDocumentoLicenciamento = function(idDocumento) {

        documentoLicenciamentoService.download(idDocumento);
    };
    

};

exports.controllers.AnaliseJuridicaController = AnaliseJuridicaController;