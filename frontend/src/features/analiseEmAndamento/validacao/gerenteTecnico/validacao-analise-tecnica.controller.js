var ValidacaoAnaliseTecnicaGerenteController = function($rootScope,
                                                    analiseTecnicaService, 
                                                    $timeout,
                                                    $route, 
                                                    $scope,
                                                    mensagem, 
                                                    $location,
                                                    documentoAnaliseService, 
                                                    $anchorScroll, 
                                                    $uibModal,      
                                                    documentoService,
                                                    validacaoAnaliseGerenteService, 
                                                    analistaService) {

    var validacaoAnaliseTecnicaGerente = this;

    validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao = {};
    validacaoAnaliseTecnicaGerente.dadosProjeto = {};
    validacaoAnaliseTecnicaGerente.init = init;
    validacaoAnaliseTecnicaGerente.controleVisualizacao = null;
    validacaoAnaliseTecnicaGerente.baixarDocumento = baixarDocumento;
    validacaoAnaliseTecnicaGerente.titulo = 'VALIDAÇÃO TÉCNICA';   
    validacaoAnaliseTecnicaGerente.parecerTecnico = {};
    validacaoAnaliseTecnicaGerente.labelDadosProjeto = '';
    validacaoAnaliseTecnicaGerente.enumDocumentos = app.utils.TiposDocumentosAnalise;

    validacaoAnaliseTecnicaGerente.errors = {
		despacho: false,
        resultadoAnalise: false,
        analistas: false		
};

validacaoAnaliseTecnicaGerente.disable = {
    tipoLicenca: true,
    atividade: [],
    questionario: true,
    documentoAdministrativo: [],
    documentoTecnicoAmbiental: []
};

    validacaoAnaliseTecnicaGerente.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    function init() {
        validacaoAnaliseTecnicaGerente.controleVisualizacao = "ETAPA_ANALISE_TECNICA";

        analiseTecnicaService.getAnliseTecnicaByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseTecnicaGerente.analiseTecnica = response.data;
                validacaoAnaliseTecnicaGerente.parecerTecnico = getUltimoParecerTecnico(validacaoAnaliseTecnicaGerente.analiseTecnica.pareceresAnalistaTecnico);

                _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, index){
                    
                    validacaoAnaliseTecnicaGerente.disable.atividade[index] = {
                        atividadeValida: true,
                        parametros: []
                    };

                    _.forEach(atividade.atividade.parametros, function(parametro, indexParametro) {
                        validacaoAnaliseTecnicaGerente.disable.atividade[index].parametros[indexParametro] = true;
                    });  
                });

                _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.caracterizacao.solicitacoesDocumento, function(documentoAdministrativo, index){
                        validacaoAnaliseTecnicaGerente.disable.documentoAdministrativo[index] = true;
                });

                _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.caracterizacao.documentosSolicitacaoGrupo, function(documentoTecnicoAmbiental, index){
                    validacaoAnaliseTecnicaGerente.disable.documentoTecnicoAmbiental[index] = true;
                });

                _.forEach( validacaoAnaliseTecnicaGerente.analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
    
                    if(inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca !== null){
                        validacaoAnaliseTecnicaGerente.disable.tipoLicenca = false;
                    }

                    if(inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null){

                        _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, index){

                            if(atividade.id === inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id){
                                
                                validacaoAnaliseTecnicaGerente.disable.atividade[index].atividadeValida = false;
                            }
                        });
                    }

                    if(inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null){

                        _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, indexAtividade){

                            _.forEach(atividade.atividade.parametros, function(parametro, indexParametro) {

                                if(inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id === parametro.id &&
                                    inconsistenciaTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.id === atividade.id){

                                        validacaoAnaliseTecnicaGerente.disable.atividade[indexAtividade].parametros[indexParametro] = false;
                                    }        
                            });
                        });
                    }
                    
                    if(inconsistenciaTecnica.inconsistenciaTecnicaQuestionario !== null){
                        validacaoAnaliseTecnicaGerente.disable.questionario = false;
                    }
                    
                    if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo !== null){

                        _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.caracterizacao.solicitacoesDocumento, function(documentoAdministrativo, index){
                            if(documentoAdministrativo.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id){

                                validacaoAnaliseTecnicaGerente.disable.documentoAdministrativo[index] = false;
                            }
                        });                                               
                    }
                    
                    if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental !== null){

                        _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.caracterizacao.documentosSolicitacaoGrupo, function(documentoTecnicoAmbiental, index){
                            validacaoAnaliseTecnicaGerente.disable.documentoTecnicoAmbiental[index] = false;
                        });
                    }
                });           
            });
        
        $rootScope.$broadcast('atualizarContagemProcessos');
    }

    var getUltimoParecerTecnico = function(pareceresAnalistaTecnico) {

        var pareceresOrdenados = pareceresAnalistaTecnico.sort(function(dataParecer1, dataParecer2){
            return dataParecer1 - dataParecer2;
        });

        return pareceresOrdenados[pareceresOrdenados.length - 1];

    };

    validacaoAnaliseTecnicaGerente.buscarAnalistasTecnicoByIdProcesso = function() {
		analistaService.buscarAnalistasTecnicoByIdProcesso(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.id)
			.then(function(response) {
				validacaoAnaliseTecnicaGerente.analistaTecnicos = response.data;
			});
    };
    
    function scrollTop() {
		$anchorScroll();
	}

    validacaoAnaliseTecnicaGerente.cancelar = function() {

        $location.path("/analise-gerente");
    };

    function baixarDocumento(documento) {
        if(!documento.id){
			documentoService.download(documento.key, documento.nomeDoArquivo);
		}else{
			analiseTecnicaService.download(documento.id);
		}
    }

	validacaoAnaliseTecnicaGerente.validacaoAbaVoltar = function() {
		
		validacaoAnaliseTecnicaGerente.controleVisualizacao = "ETAPA_ANALISE_TECNICA";
		
		scrollTop();
	};

    validacaoAnaliseTecnicaGerente.voltarEtapaAnterior = function(){
		$timeout(function() {
			$('.nav-tabs > .active').prev('li').find('a').trigger('click');
			scrollTop();
			validacaoAnaliseTecnicaGerente.controleVisualizacao = "ETAPA_ANALISE_TECNICA";
		}, 0);
	};
    
    validacaoAnaliseTecnicaGerente.avancarProximaEtapa = function() {
		$timeout(function() {
			$('.nav-tabs > .active').next('li').find('a').trigger('click');
			validacaoAnaliseTecnicaGerente.controleVisualizacao = "ETAPA_VALIDACAO_ANALISE_TECNICA";
			scrollTop();
        }, 0);
    };

    validacaoAnaliseTecnicaGerente.validacaoAbaAvancar = function() {
		
        validacaoAnaliseTecnicaGerente.controleVisualizacao = "ETAPA_VALIDACAO_ANALISE_TECNICA";
        
        scrollTop();
    
};

    function openModal(analiseTecnicaModal, tipoDeInconsistenciaTecnicaModal, inconsistenciaTecnicaModal, 
                       atividadeCaracterizacaoModal, parametroAtividadeModal, questionarioModal, 
                       documentoAdministrativoModal, documentoTecnicoAmbientalModal, indexModal, indexParametroModal) {
            
        $uibModal.open({
            animation: true,
            templateUrl: './features/analiseTecnica/modalInconsistenciaTecnica.html',
            controller: 'inconsistenciaTecnicaController',
            controllerAs: 'modalCtrl',
            size: 'lg',
            resolve: {
                analiseTecnica: function () {
                    return analiseTecnicaModal;
                },
                tipoDeInconsistenciaTecnica: function(){
                    return tipoDeInconsistenciaTecnicaModal;
                },
                inconsistenciaTecnica: function(){
                    return inconsistenciaTecnicaModal;
                },
                atividadeCaracterizacao: function(){
                    return atividadeCaracterizacaoModal;
                },
                parametroAtividade: function(){
                    return parametroAtividadeModal;
                },
                questionario: function(){
                    return questionarioModal;
                },
                documentoAdministrativo: function(){
                    return documentoAdministrativoModal;
                },
                documentoTecnicoAmbiental: function(){
                    return documentoTecnicoAmbientalModal;
                },
                index: function(){
                    return indexModal;
                },
                indexParametro: function(){
                    return indexParametroModal;
                }
            }
        });
    }


    validacaoAnaliseTecnicaGerente.visualizarInconsistenciaTecnicaTipoLicenca = function (analiseTecnica, tipoDeInconsistenciaTecnica) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
            .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){
        
                    return inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca;
                });

            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, null, null, null, null, null);
        });
    };

    validacaoAnaliseTecnicaGerente.visualizarInconsistenciaTecnicaAtividade = function (analiseTecnica, tipoDeInconsistenciaTecnica, atividadeCaracterizacao, index) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
        .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){
                return inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null &&
                    inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id === atividadeCaracterizacao.id;
            });
    
            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, atividadeCaracterizacao, null, null, null, null, index, null);
        });

    };

    validacaoAnaliseTecnicaGerente.visualizarInconsistenciaTecnicaParametro = function (analiseTecnica, tipoDeInconsistenciaTecnica, parametroAtividade, index, indexParametro, atividade) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
        .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){
                return inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null &&
                    inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id === parametroAtividade.id && 
                    inconsistenciaTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.id === atividade.id;
            });
       
            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, atividade, parametroAtividade, null, null, null, index, indexParametro);
        });
    };

    validacaoAnaliseTecnicaGerente.visualizarInconsistenciaTecnicaQuestionario = function (analiseTecnica,tipoDeInconsistenciaTecnica) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
        .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){
    
                return inconsistenciaTecnica.inconsistenciaTecnicaQuestionario;
            });
       
            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, analiseTecnica.analise.processo.caracterizacao.questionario3, null, null, null, null);
        });
    };

    validacaoAnaliseTecnicaGerente.visualizarInconsistenciaTecnicaDocumentoAdministrativo = function (analiseTecnica, tipoDeInconsistenciaTecnica, documentoAdministrativo, index) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
        .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){

                if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo !== null){
                    if (documentoAdministrativo.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id){
                        return inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo;
                    }
                }
            });
       
            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, null, documentoAdministrativo, null, index, null);    
        });
    };

    validacaoAnaliseTecnicaGerente.visualizarInconsistenciaTecnicaDocumentoTecnicoAmbiental = function (analiseTecnica,tipoDeInconsistenciaTecnica, documentoTecnicoAmbiental, index) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
        .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){

                if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental !== null){
                    if (documentoTecnicoAmbiental.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.id){
                        return inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental;
                    }
                }   
            });
       
            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, null, null, documentoTecnicoAmbiental, index, null);
        });
    };

};

exports.controllers.ValidacaoAnaliseTecnicaGerenteController = ValidacaoAnaliseTecnicaGerenteController;
