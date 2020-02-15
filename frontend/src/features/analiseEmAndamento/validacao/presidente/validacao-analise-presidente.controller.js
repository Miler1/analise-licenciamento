var ValidacaoAnalisePresidenteController = function($rootScope,
                                                 $route,      
                                                 analiseGeoService, 
                                                 analiseTecnicaService,
                                                 $location) {

    var validacaoAnalisePresidente = this;

    validacaoAnalisePresidente.init = init;
    validacaoAnalisePresidente.titulo = 'VALIDAÇÃO TÉCNICA';   
    validacaoAnalisePresidente.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnalisePresidente.tipoDocumento =  app.utils.TiposDocumentosAnalise;

    function init() {

        validacaoAnalisePresidente.controleVisualizacao = "ETAPA_VALIDACAO_GEO";

        analiseGeoService.getAnaliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnalisePresidente.analiseGeo = response.data;

            });     

            analiseTecnicaService.getAnaliseTecnicaByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnalisePresidente.analiseTecnica = response.data;

            });
        
        $rootScope.$broadcast('atualizarContagemProcessos');
    }

    validacaoAnalisePresidente.cancelar = function() {

        $location.path("/analise-presidente");
    };

};

exports.controllers.ValidacaoAnalisePresidenteController = ValidacaoAnalisePresidenteController;
