var ValidacaoAnaliseDiretorController = function($rootScope,
                                                 $route,      
                                                 analiseGeoService, 
                                                 analiseTecnicaService,
                                                 documentoAnaliseService,
                                                 documentoService) {

    var validacaoAnaliseDiretor = this;

    validacaoAnaliseDiretor.init = init;
    validacaoAnaliseDiretor.titulo = 'VALIDAÇÃO TÉCNICA';   
    validacaoAnaliseDiretor.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnaliseDiretor.tipoDocumento =  app.utils.TiposDocumentosAnalise;
    validacaoAnaliseDiretor.labelParecerGerente = null;
    validacaoAnaliseDiretor.labelParecerAnalistaGeo = null;
    validacaoAnaliseDiretor.labelParecerAnalistaTecnico = null;
    validacaoAnaliseDiretor.possuiAutoInfracao = false;

    var getUltimoParecerAnalista = function(pareceresAnalista) {

        var pareceresOrdenados = pareceresAnalista.sort(function(dataParecer1, dataParecer2){
            return dataParecer1 - dataParecer2;
        });

        return pareceresOrdenados[pareceresOrdenados.length - 1];

    };

    var getUltimoParecerGerente = function(pareceresGerente) {

        var pareceresOrdenados = pareceresGerente.sort(function(dataParecer1, dataParecer2){
            return dataParecer1 - dataParecer2;
        });

        return pareceresOrdenados[pareceresOrdenados.length - 1];

    };

    var setLabelsAnaliseGeo = function(){

        if(validacaoAnaliseDiretor.parecerGeo.tipoResultadoAnalise.id === validacaoAnaliseDiretor.tiposResultadoAnalise.DEFERIDO){

            validacaoAnaliseDiretor.labelParecerAnalistaGeo = 'Despacho';

        }else{

            validacaoAnaliseDiretor.labelParecerAnalistaGeo = 'Justificativa';

        }

        if(validacaoAnaliseDiretor.parecerGerenteGeo.tipoResultadoAnalise.id === validacaoAnaliseDiretor.tiposResultadoAnalise.PARECER_VALIDADO){

            validacaoAnaliseDiretor.labelParecerGerente = 'Despacho';

        }else{

            validacaoAnaliseDiretor.labelParecerGerente = 'Justificativa';

        }
    };

    var setLabelsAnaliseTecnica = function(){

        if(validacaoAnaliseDiretor.parecerGeo.tipoResultadoAnalise.id === validacaoAnaliseDiretor.tiposResultadoAnalise.DEFERIDO){

            validacaoAnaliseDiretor.labelParecerAnalistaTecnico = 'Despacho';

        }else{

            validacaoAnaliseDiretor.labelParecerAnalistaTecnico = 'Justificativa';

        }

        if(validacaoAnaliseDiretor.parecerGerenteGeo.tipoResultadoAnalise.id === validacaoAnaliseDiretor.tiposResultadoAnalise.PARECER_VALIDADO){

            validacaoAnaliseDiretor.labelParecerGerente = 'Despacho';

        }else{

            validacaoAnaliseDiretor.labelParecerGerente = 'Justificativa';

        }

    };

    function init() {

        analiseGeoService.getAnaliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseDiretor.analiseGeo = response.data;
                validacaoAnaliseDiretor.parecerGeo = getUltimoParecerAnalista(validacaoAnaliseDiretor.analiseGeo.pareceresAnalistaGeo);
                validacaoAnaliseDiretor.parecerGerenteGeo = getUltimoParecerGerente(validacaoAnaliseDiretor.analiseGeo.pareceresGerenteAnaliseGeo);

                setLabelsAnaliseGeo();

            });     

            analiseTecnicaService.getAnaliseTecnicaByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseDiretor.analiseTecnica = response.data;
                validacaoAnaliseDiretor.parecerTecnico = getUltimoParecerAnalista(validacaoAnaliseDiretor.analiseTecnica.pareceresAnalistaTecnico);
                validacaoAnaliseDiretor.parecerGerenteTecnico = getUltimoParecerGerente(validacaoAnaliseDiretor.analiseTecnica.pareceresGerenteAnaliseTecnica);

                setLabelsAnaliseTecnica();

                _.filter(validacaoAnaliseDiretor.parecerTecnico.documentos , function(documento){
                    if(documento.tipo.id === app.utils.TiposDocumentosAnalise.AUTO_INFRACAO){
                        validacaoAnaliseDiretor.possuiAutoInfracao = true;
                    }
                });

            });
        
        $rootScope.$broadcast('atualizarContagemProcessos');
    }


    validacaoAnaliseDiretor.visualizarAutoInfracao = function(documentosAnaliseTecnica) {

        _.filter(documentosAnaliseTecnica, function(documento){
            if(documento.tipo.id === app.utils.TiposDocumentosAnalise.AUTO_INFRACAO){
                documentoAnaliseService.download(documento.id);
            }
        });
    };

    validacaoAnaliseDiretor.baixarDocumento = function (analiseTecnica, tipoDocumento, documento ) {

        if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.PARECER_ANALISE_TECNICA ) {

            documentoService.downloadParecerByIdAnaliseTecnica(analiseTecnica.id);

        }else if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.DOCUMENTO_RELATORIO_TECNICO_VISTORIA ) {

            documentoService.downloadRTVByIdAnaliseTecnica(analiseTecnica.id);

        }else if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.DOCUMENTO_MINUTA ) {

            documentoService.downloadMinutaByIdAnaliseTecnica(analiseTecnica.id);

        }else if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.CARTA_IMAGEM ) {

            documentoService.downloadById(analiseTecnica.id);

        }else if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.PARECER_ANALISE_GEO ) {

            documentoService.downloadById(analiseTecnica.id);

        }
                    
    };

};

exports.controllers.ValidacaoAnaliseDiretorController = ValidacaoAnaliseDiretorController;
