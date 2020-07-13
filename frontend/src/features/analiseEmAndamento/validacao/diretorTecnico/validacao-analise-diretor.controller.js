var ValidacaoAnaliseDiretorController = function($rootScope,
                                                 $route,      
                                                 analiseGeoService, 
                                                 analiseTecnicaService,
                                                 documentoAnaliseService,
                                                 documentoService,
                                                 parecerDiretorTecnicoService,
                                                 $anchorScroll,
                                                 mensagem,
                                                 $location,
                                                 $timeout,
                                                 processoService,
                                                 parecerGerenteService,
                                                 parecerAnalistaTecnicoService,
                                                 parecerAnalistaGeoService) {

    var validacaoAnaliseDiretor = this;

    validacaoAnaliseDiretor.init = init;
    validacaoAnaliseDiretor.titulo = 'VALIDAÇÃO TÉCNICA';   
    validacaoAnaliseDiretor.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnaliseDiretor.tipoDocumento =  app.utils.TiposDocumentosAnalise;
    validacaoAnaliseDiretor.labelParecerGerente = null;
    validacaoAnaliseDiretor.labelParecerAnalistaGeo = null;
    validacaoAnaliseDiretor.labelParecerAnalistaTecnico = null;
    validacaoAnaliseDiretor.possuiAutoInfracao = false;
    validacaoAnaliseDiretor.controleVisualizacao = null;
    validacaoAnaliseDiretor.concluir = concluir;
    validacaoAnaliseDiretor.parecerTecnico = {};
    validacaoAnaliseDiretor.idTipoResultadoAnalise = null; 
    validacaoAnaliseDiretor.parecerDiretorTecnico = '';
    validacaoAnaliseDiretor.exibirDadosProcesso = exibirDadosProcesso;
    validacaoAnaliseDiretor.documentosAnaliseTecnica = [];

    validacaoAnaliseDiretor.errors = {

		despacho: false,
        resultadoAnalise: false,
        	
    };

    var getUltimoParecerAnalistaGeo = function(analiseGeo) {

        parecerAnalistaGeoService.getUltimoParecerAnaliseGeo(analiseGeo.id)
            .then(function(response){

                validacaoAnaliseDiretor.parecerGeo = response.data;
                setLabelsAnaliseGeo();
        });
    };

    var getUltimoParecerAnalistaTecnico = function(analiseTecnica) {

        parecerAnalistaTecnicoService.getUltimoParecerAnaliseTecnica(analiseTecnica.id)
            .then(function(response){

                validacaoAnaliseDiretor.parecerTecnico = response.data;
                setLabelsAnaliseTecnica();

                _.filter(validacaoAnaliseDiretor.parecerTecnico.documentos , function(documento){
                    if(documento.tipo.id === app.utils.TiposDocumentosAnalise.AUTO_INFRACAO){

                        validacaoAnaliseDiretor.possuiAutoInfracao = true;

                    } else {
                        validacaoAnaliseDiretor.documentosAnaliseTecnica.push(documento);
                    }
                });
        });

    };

    validacaoAnaliseDiretor.downloadDocumentoAnalise = function (idDocumento) {

        documentoAnaliseService.download(idDocumento);
        
	};

    var getUltimoParecerGerenteTecnico = function(analiseTecnica) {

        parecerGerenteService.getUltimoParecerGerenteAnaliseTecnica(analiseTecnica.id)
            .then(function(response){

                validacaoAnaliseDiretor.parecerGerenteTecnico = response.data;
                setLabelsGerenteTecnico();

        });

    };

    var getUltimoParecerGerenteGeo = function(analiseGeo) {

        parecerGerenteService.getUltimoParecerGerenteAnaliseGeo(analiseGeo.id)
            .then(function(response){

                validacaoAnaliseDiretor.parecerGerenteGeo = response.data;
                setLabelsGerenteGeo();

        });

    };

    var setLabelsAnaliseGeo = function(){

        if(validacaoAnaliseDiretor.parecerGeo.tipoResultadoAnalise.id === validacaoAnaliseDiretor.tiposResultadoAnalise.DEFERIDO){

            validacaoAnaliseDiretor.labelParecerAnalistaGeo = 'Despacho';

        }else{

            validacaoAnaliseDiretor.labelParecerAnalistaGeo = 'Justificativa';

        }

    };

    var setLabelsGerenteGeo = function(){

        if(validacaoAnaliseDiretor.parecerGerenteGeo.tipoResultadoAnalise.id === validacaoAnaliseDiretor.tiposResultadoAnalise.PARECER_VALIDADO){

            validacaoAnaliseDiretor.labelParecerGerente = 'Despacho';

        }else{

            validacaoAnaliseDiretor.labelParecerGerente = 'Justificativa';

        }

    };

    var setLabelsAnaliseTecnica = function(){

        if(validacaoAnaliseDiretor.parecerTecnico.tipoResultadoAnalise.id === validacaoAnaliseDiretor.tiposResultadoAnalise.DEFERIDO){

            validacaoAnaliseDiretor.labelParecerAnalistaTecnico = 'Despacho';

        }else{

            validacaoAnaliseDiretor.labelParecerAnalistaTecnico = 'Justificativa';

        }

    };

    var setLabelsGerenteTecnico = function(){

        if(validacaoAnaliseDiretor.parecerGerenteTecnico.tipoResultadoAnalise.id === validacaoAnaliseDiretor.tiposResultadoAnalise.PARECER_VALIDADO){

            validacaoAnaliseDiretor.labelParecerGerente = 'Despacho';

        }else{

            validacaoAnaliseDiretor.labelParecerGerente = 'Justificativa';

        }
        
    };

    function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseDiretor.analiseGeo.analise.processo.id,
            numero: validacaoAnaliseDiretor.analiseGeo.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseDiretor.analiseGeo.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseDiretor.analiseGeo.analise.processo.empreendimento.cpfCnpj.length > 11) {

            processo.cnpjEmpreendimento = validacaoAnaliseDiretor.analiseGeo.analise.processo.empreendimento.cpfCnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseDiretor.analiseGeo.analise.processo.empreendimento.cpfCnpj;
        }		

        processoService.visualizarProcesso(processo);
    }

    function init() {

        validacaoAnaliseDiretor.controleVisualizacao = "ETAPA_VALIDACAO_GEO";

        analiseGeoService.getAnaliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseDiretor.analiseGeo = response.data;
                getUltimoParecerAnalistaGeo(validacaoAnaliseDiretor.analiseGeo);
                getUltimoParecerGerenteGeo(validacaoAnaliseDiretor.analiseGeo);


        });     

        analiseTecnicaService.getAnaliseTecnicaByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseDiretor.analiseTecnica = response.data;
                getUltimoParecerAnalistaTecnico(validacaoAnaliseDiretor.analiseTecnica);
                getUltimoParecerGerenteTecnico(validacaoAnaliseDiretor.analiseTecnica);

        });
        
        $rootScope.$broadcast('atualizarContagemProcessos');

    }

    function scrollTop() {

        $anchorScroll();
    }

    function analiseValida() {

        if(validacaoAnaliseDiretor.idTipoResultadoAnalise === null || validacaoAnaliseDiretor.idTipoResultadoAnalise === undefined) {

            validacaoAnaliseDiretor.errors.resultadoAnalise = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        } else{

            validacaoAnaliseDiretor.errors.resultadoAnalise = false;
        }
        
        if(validacaoAnaliseDiretor.parecerDiretorTecnico === "" || validacaoAnaliseDiretor.parecerDiretorTecnico === null || validacaoAnaliseDiretor.parecerDiretorTecnico === undefined) {

            validacaoAnaliseDiretor.errors.despacho = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        } else{

            validacaoAnaliseDiretor.errors.despacho = false;

        }

        if(validacaoAnaliseDiretor.errors.resultadoAnalise === true || validacaoAnaliseDiretor.errors.despacho === true){

            return false;

        }
        
        return true;

    }

    validacaoAnaliseDiretor.validacaoAbaVoltar = function() {
		
        validacaoAnaliseDiretor.controleVisualizacao = "ETAPA_VALIDACAO_GEO";
		
		scrollTop();
	};

    validacaoAnaliseDiretor.voltarEtapaAnterior = function(){
		$timeout(function() {
			$('.nav-tabs > .active').prev('li').find('a').trigger('click');
            scrollTop();
			
		}, 0);
	};
    
    validacaoAnaliseDiretor.avancarProximaEtapa = function() {
		$timeout(function() {
            $('.nav-tabs > .active').next('li').find('a').trigger('click');

			scrollTop();
        }, 0);
    };

    validacaoAnaliseDiretor.validacaoAbaAvancarDiretor = function() {

        validacaoAnaliseDiretor.controleVisualizacao = "ETAPA_VALIDACAO_DIRETOR";
        
        scrollTop();
    
    };

    validacaoAnaliseDiretor.validacaoAbaAvancarTecnico = function() {

        validacaoAnaliseDiretor.controleVisualizacao = "ETAPA_VALIDACAO_TECNICO";
        
        scrollTop();
    
    };

    validacaoAnaliseDiretor.cancelar = function() {

        $location.path("/analise-diretor");
    };

    function concluir() {

        if(!analiseValida()){
            return;
        }

        var params = {
            analise: {
                id: $route.current.params.idAnalise,
            },
            parecer: validacaoAnaliseDiretor.parecerDiretorTecnico,
            tipoResultadoAnalise: {id: validacaoAnaliseDiretor.idTipoResultadoAnalise}
        };

        parecerDiretorTecnicoService.concluirParecerDiretorTecnico(params)
			.then(function(response){
                $location.path('/analise-diretor');
                $timeout(function() {
                    mensagem.success("Validação finalizada!", {referenceId: 5});
                }, 0);
            },function(error){
				mensagem.error(error.data.texto);
			});
    }


    validacaoAnaliseDiretor.visualizarAutoInfracao = function(documentosAnaliseTecnica) {

        _.filter(documentosAnaliseTecnica, function(documento){
            if(documento.tipo.id === app.utils.TiposDocumentosAnalise.AUTO_INFRACAO){

                documentoAnaliseService.download(documento.id);
                
            }
        });
    };

    validacaoAnaliseDiretor.baixarDocumento = function (analiseTecnica, tipoDocumento ) {

        if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.PARECER_ANALISE_TECNICA ) {

            documentoService.downloadParecerByIdAnaliseTecnica(analiseTecnica.id);

        }else if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.DOCUMENTO_RELATORIO_TECNICO_VISTORIA ) {

            documentoService.downloadRTVByIdAnaliseTecnica(analiseTecnica.id);

        }else if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.DOCUMENTO_MINUTA ) {

            documentoService.downloadMinutaByIdAnaliseTecnica(analiseTecnica.id);

        }
                    
    };

    validacaoAnaliseDiretor.baixarDocumentoGeo = function (parecerGeo, tipoDocumento ) {

        if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.PARECER_ANALISE_GEO ) {

            analiseGeoService.download(parecerGeo.documentoParecer.id);

        }else if ( tipoDocumento === validacaoAnaliseDiretor.tipoDocumento.CARTA_IMAGEM ) {

            analiseGeoService.download(parecerGeo.cartaImagem.id);

        }                    
    };

};

exports.controllers.ValidacaoAnaliseDiretorController = ValidacaoAnaliseDiretorController;
