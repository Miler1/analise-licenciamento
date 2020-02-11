var ValidacaoAnaliseDiretorController = function($rootScope,
                                                 $route,      
                                                 analiseGeoService,
                                                 parecerDiretorTecnicoService,
                                                 $anchorScroll,
                                                 $location,
                                                 $timeout,
                                                 analistaService) {

    var validacaoAnaliseDiretor = this;

    validacaoAnaliseDiretor.dadosProjeto = {};
    validacaoAnaliseDiretor.init = init;
    validacaoAnaliseDiretor.controleVisualizacao = null;
    validacaoAnaliseDiretor.concluir = concluir;
    validacaoAnaliseDiretor.titulo = 'VALIDAÇÃO TÉCNICA';   
    validacaoAnaliseDiretor.parecerTecnico = {};
    validacaoAnaliseDiretor.labelDadosProjeto = '';
    validacaoAnaliseDiretor.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnaliseDiretor.idTipoResultadoAnalise = null; 
    validacaoAnaliseDiretor.parecerDiretorTecnico = '';

    validacaoAnaliseDiretor.errors = {

		despacho: false,
        resultadoAnalise: false,
        	
    };

    function init() {

        validacaoAnaliseDiretor.controleVisualizacao = "ETAPA_VALIDACAO_GEO";

        analiseGeoService.getAnaliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseDiretor.analiseGeo = response.data;

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

        }else{

            validacaoAnaliseDiretor.errors.resultadoAnalise = false;

        }
        
        if(validacaoAnaliseDiretor.parecerDiretorTecnico === "" || validacaoAnaliseDiretor.parecerDiretorTecnico === null || validacaoAnaliseDiretor.parecerDiretorTecnico === undefined) {

            validacaoAnaliseDiretor.errors.despacho = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

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
                $location.path("analise-diretor");
                $timeout(function() {
                    mensagem.success("Validacao diretor finalizada!", {referenceId: 5});
                }, 0);
            },function(error){
				mensagem.error(error.data.texto);
			});
    }

};

exports.controllers.ValidacaoAnaliseDiretorController = ValidacaoAnaliseDiretorController;
