var AnaliseJuridicaController = function($rootScope, $scope, $routeParams, Upload, processo) {

    var analiseJuridica = this;

    analiseJuridica.processo = processo;

    analiseJuridica.upload = function(file) {

        console.log(file);
    };
    

};

exports.controllers.AnaliseJuridicaController = AnaliseJuridicaController;