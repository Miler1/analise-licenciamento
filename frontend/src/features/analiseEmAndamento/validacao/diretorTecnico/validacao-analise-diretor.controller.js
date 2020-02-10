var ValidacaoAnaliseDiretorController = function($rootScope,
                                                 $route,      
                                                 analiseGeoService, 
                                                 analistaService) {

    var validacaoAnaliseDiretor = this;

    validacaoAnaliseDiretor.dadosProjeto = {};
    validacaoAnaliseDiretor.init = init;
    validacaoAnaliseDiretor.controleVisualizacao = null;
    validacaoAnaliseDiretor.titulo = 'VALIDAÇÃO TÉCNICA';   
    validacaoAnaliseDiretor.parecerTecnico = {};
    validacaoAnaliseDiretor.labelDadosProjeto = '';
    validacaoAnaliseDiretor.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    function init() {

        analiseGeoService.getAnaliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseDiretor.analiseGeo = response.data;
                validacaoAnaliseDiretor.parecerGeo = getUltimoParecerGeo(validacaoAnaliseDiretor.analiseGeo.pareceresAnalistaGeo);

                if(validacaoAnaliseDiretor.analiseGeo.analistasTecnicos) {

                    validacaoAnaliseDiretor.analiseGeoValidacao.idAnalistaTecnico =
                        validacaoAnaliseDiretor.analiseGeo.analistasTecnicos[0].usuario.id;
                        
                }

                if (validacaoAnaliseDiretor.analiseGeo.tipoResultadoValidacaoGerente) {

                    validacaoAnaliseDiretor.analiseGeoValidacao.idTipoResultadoValidacaoGerente =
                        validacaoAnaliseDiretor.analiseGeo.tipoResultadoValidacaoGerente.id;
                }
                
                validacaoAnaliseDiretor.analiseGeoValidacao.parecerValidacaoGerente =
                    validacaoAnaliseDiretor.analiseGeo.parecerValidacaoGerente;

                analistaService.getAnalistasTecnicosByProcesso(validacaoAnaliseDiretor.analiseGeo.analise.processo.id)
                    .then(function(response){

                        validacaoAnaliseDiretor.analistas = response.data;

                });            
            
                getDadosVisualizar(validacaoAnaliseDiretor.analiseGeo.analise.processo);

            });
        
        $rootScope.$broadcast('atualizarContagemProcessos');
    }

};

exports.controllers.ValidacaoAnaliseDiretorController = ValidacaoAnaliseDiretorController;
