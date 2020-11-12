var ValidacaoAnaliseTecnicaGerenteController = function($rootScope,
                                                    analiseTecnicaService,
                                                    $timeout,
                                                    $route,
                                                    mensagem,
                                                    $location,
                                                    documentoAnaliseService,
                                                    $anchorScroll,
                                                    $uibModal,
                                                    documentoService,
                                                    validacaoAnaliseGerenteService,
                                                    analistaService,
                                                    processoService,
                                                    parecerAnalistaTecnicoService) {

    var validacaoAnaliseTecnicaGerente = this;

    validacaoAnaliseTecnicaGerente.analistasTecnicos = null;
    validacaoAnaliseTecnicaGerente.analistaTecnicoDestino = {};
    validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao = {};
    validacaoAnaliseTecnicaGerente.dadosProjeto = {};
    validacaoAnaliseTecnicaGerente.init = init;
    validacaoAnaliseTecnicaGerente.controleVisualizacao = null;
    validacaoAnaliseTecnicaGerente.baixarDocumento = baixarDocumento;
    validacaoAnaliseTecnicaGerente.titulo = 'VALIDAÇÃO TÉCNICA';
    validacaoAnaliseTecnicaGerente.parecerTecnico = {};
    validacaoAnaliseTecnicaGerente.labelDadosProjeto = '';
    validacaoAnaliseTecnicaGerente.enumDocumentos = app.utils.TiposDocumentosAnalise;
    validacaoAnaliseTecnicaGerente.concluir = concluir;
    validacaoAnaliseTecnicaGerente.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnaliseTecnicaGerente.listaAnalisesTecnicas = [];
    validacaoAnaliseTecnicaGerente.processo = null;
    validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga = null;
    validacaoAnaliseTecnicaGerente.documentos = [];

    validacaoAnaliseTecnicaGerente.possuiAutoInfracao = false;

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

    var findAnalisesTecnicaByNumeroProcesso = function(processo) {

        analiseTecnicaService.findAnalisesTecnicaByNumeroProcesso(btoa(processo.numero))
            .then(function(response){

                validacaoAnaliseTecnicaGerente.listaAnalisesTecnicas = response.data;

            });
    };

    function getAnaliseTecnica(idAnaliseTecnica){

        analiseTecnicaService.getAnaliseTecnica(idAnaliseTecnica)
        .then(function(response){

            validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga = response.data;
            findAnalisesTecnicaByNumeroProcesso(validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.analise.processo);

            _.filter(validacaoAnaliseTecnicaGerente.parecerTecnico.documentos , function(documento){
                
                if(documento.tipo.id === validacaoAnaliseTecnicaGerente.enumDocumentos.AUTO_INFRACAO){
                    validacaoAnaliseTecnicaGerente.possuiAutoInfracao = true;
                }else {
                    validacaoAnaliseTecnicaGerente.documentos.push(documento);
                }
            });

            _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, index){

                validacaoAnaliseTecnicaGerente.disable.atividade[index] = {
                    atividadeValida: true,
                    parametros: []
                };

                _.forEach(atividade.atividade.parametros, function(parametro, indexParametro) {
                    validacaoAnaliseTecnicaGerente.disable.atividade[index].parametros[indexParametro] = true;
                });
            });

            _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.analise.processo.caracterizacao.solicitacoesDocumento, function(documentoAdministrativo, index){
                    validacaoAnaliseTecnicaGerente.disable.documentoAdministrativo[index] = true;
            });

            _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.analise.processo.caracterizacao.documentosSolicitacaoGrupo, function(documentoTecnicoAmbiental, index){
                validacaoAnaliseTecnicaGerente.disable.documentoTecnicoAmbiental[index] = true;
            });

            _.forEach( validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.inconsistenciasTecnica, function(inconsistenciaTecnica){

                if(inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca !== null){
                    validacaoAnaliseTecnicaGerente.disable.tipoLicenca = false;
                }

                if(inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null){

                    _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, index){

                        if(atividade.id === inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id){

                            validacaoAnaliseTecnicaGerente.disable.atividade[index].atividadeValida = false;
                        }
                    });
                }

                if(inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null){

                    _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, indexAtividade){

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

                    _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.analise.processo.caracterizacao.solicitacoesDocumento, function(documentoAdministrativo, index){
                        if(documentoAdministrativo.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id){

                            validacaoAnaliseTecnicaGerente.disable.documentoAdministrativo[index] = false;
                        }
                    });
                }

                if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental !== null){

                    _.forEach(validacaoAnaliseTecnicaGerente.analiseTecnicaAntiga.analise.processo.caracterizacao.documentosSolicitacaoGrupo, function(documentoTecnicoAmbiental, index){
                        validacaoAnaliseTecnicaGerente.disable.documentoTecnicoAmbiental[index] = false;
                    });
                }
            });
            
        });
    }

    function init() {

        validacaoAnaliseTecnicaGerente.controleVisualizacao = "ETAPA_ANALISE_TECNICA";
        analiseTecnicaService.getAnaliseTecnicaByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseTecnicaGerente.analiseTecnica = response.data;

                processoService.getInfoProcesso(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.id).then(function(response){

                    validacaoAnaliseTecnicaGerente.processo = response.data;

                    if(validacaoAnaliseTecnicaGerente.processo.processoAnterior === undefined || validacaoAnaliseTecnicaGerente.processo.processoAnterior === null  ){

                        getAnaliseTecnica(validacaoAnaliseTecnicaGerente.analiseTecnica.id);
                        
                    } else{

                        if(validacaoAnaliseTecnicaGerente.processo.processoAnterior.analise.analisesTecnicas.length === 0) {
                            
                            getAnaliseTecnica(validacaoAnaliseTecnicaGerente.analiseTecnica.id);
                       
                        } else {

                            getAnaliseTecnica(validacaoAnaliseTecnicaGerente.processo.processoAnterior.analise.analisesTecnicas[0].id);
                        }

                    }
                   
                    getUltimoParecerAnalistaTecnico(validacaoAnaliseTecnicaGerente.analiseTecnica);

                });
            });

        $rootScope.$broadcast('atualizarContagemProcessos');
    }

    var getUltimoParecerAnalistaTecnico = function(analiseTecnica) {

        parecerAnalistaTecnicoService.getUltimoParecerAnaliseTecnica(analiseTecnica.id)
            .then(function(response){

                validacaoAnaliseTecnicaGerente.parecerTecnico = response.data;

        });

    };

    validacaoAnaliseTecnicaGerente.downloadPDFParecer = function(analiseTecnica) {

        documentoService.downloadParecerByIdAnaliseTecnica(analiseTecnica.id);

    };

    validacaoAnaliseTecnicaGerente.downloadPDFMinuta = function(analiseTecnica) {

        documentoService.downloadMinutaByIdAnaliseTecnica(analiseTecnica.id);

    };

    validacaoAnaliseTecnicaGerente.downloadRTVByIdAnaliseTecnica = function(analiseTecnica) {

        documentoService.downloadRTVByIdAnaliseTecnica(analiseTecnica.id);

    };

    validacaoAnaliseTecnicaGerente.visualizarAutoInfracao = function(documentosAnaliseTecnica) {

        _.filter(documentosAnaliseTecnica, function(documento){
            if(documento.tipo.id === validacaoAnaliseTecnicaGerente.enumDocumentos.AUTO_INFRACAO){
                documentoAnaliseService.download(documento.id);
            }
        });
    };

    validacaoAnaliseTecnicaGerente.exibirDadosProcesso = function () {

        var processo = {

            idProcesso: validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.id,
            numero: validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.empreendimento.cpfCnpj.length > 11) {

            processo.cnpjEmpreendimento = validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.empreendimento.cpfCnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.empreendimento.cpfCnpj;
        }		


        processoService.visualizarProcesso(processo);
    };

    validacaoAnaliseTecnicaGerente.buscarAnalistasTecnicoByIdProcesso = function() {
		analistaService.buscarAnalistasTecnicoByIdProcesso(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.id)
			.then(function(response) {
				validacaoAnaliseTecnicaGerente.analistasTecnicos = response.data;
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
            		documentoService.downloadById(documento.id);
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
                },
                isGerente: function(){
                    return true;
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

    function analiseValida(analiseTecnica) {

        if(analiseTecnica.tipoResultadoValidacaoGerente === null || analiseTecnica.tipoResultadoValidacaoGerente === undefined) {

            validacaoAnaliseTecnicaGerente.errors.resultadoAnalise = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseTecnicaGerente.errors.resultadoAnalise = false;

        }

        if(analiseTecnica.parecerValidacaoGerente === "" || analiseTecnica.parecerValidacaoGerente === null || analiseTecnica.parecerValidacaoGerente === undefined) {

            validacaoAnaliseTecnicaGerente.errors.despacho = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseTecnicaGerente.errors.despacho = false;
        }
        if (analiseTecnica.tipoResultadoValidacaoGerente !== null) {

            if(analiseTecnica.tipoResultadoValidacaoGerente.id === validacaoAnaliseTecnicaGerente.TiposResultadoAnalise.PARECER_NAO_VALIDADO.toString() && (validacaoAnaliseTecnicaGerente.analistaTecnicoDestino.id === null || validacaoAnaliseTecnicaGerente.analistaTecnicoDestino.id === undefined)) {

                validacaoAnaliseTecnicaGerente.errors.analistas = true;
                mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");
    
            }else{
    
                validacaoAnaliseTecnicaGerente.errors.analistas = false;
    
            }

        }

        if(validacaoAnaliseTecnicaGerente.errors.resultadoAnalise === true || validacaoAnaliseTecnicaGerente.errors.despacho === true || validacaoAnaliseTecnicaGerente.errors.analistas === true){
            return false;
        }

        return true;

    }

    function concluir() {

        if(!analiseValida(validacaoAnaliseTecnicaGerente.analiseTecnica)){
            return;
        }

        var params = {
            analiseTecnica: {
                id: validacaoAnaliseTecnicaGerente.analiseTecnica.id,
                idAnalistaDestino: validacaoAnaliseTecnicaGerente.analistaTecnicoDestino.id
            },
            parecer: validacaoAnaliseTecnicaGerente.analiseTecnica.parecerValidacaoGerente,
            tipoResultadoAnalise: {id: validacaoAnaliseTecnicaGerente.analiseTecnica.tipoResultadoValidacaoGerente.id}
        };

        validacaoAnaliseGerenteService.concluirParecerTecnico(params)
			.then(function(response){
                $location.path("analise-gerente");
                $timeout(function() {
                    mensagem.success("Validação finalizada!", {referenceId: 5});
                }, 0);
            },function(error){
				mensagem.error(error.data.texto);
			});
    }

    var abrirModal = function(parecer, analiseTecnica, processo) {

		$uibModal.open({
			controller: 'historicoAnaliseTecnicaCtrl',
			controllerAs: 'historicoAnaliseTecnicaCtrl',
			templateUrl: 'features/analiseEmAndamento/validacao/gerenteTecnico/historicoAnalises/modalHistoricoAnaliseTecnica.html',
			size: 'lg',
			resolve: {

				parecer: function() {
					return parecer;
				},

				analiseTecnica: function() {
					return analiseTecnica;
                },

                processo: function(){
                    return processo;
                }

			}
		});

	};

	validacaoAnaliseTecnicaGerente.visualizarJustificativas = function(parecer, analiseTecnica, processo){

		abrirModal(parecer, analiseTecnica, processo);

	};
};

exports.controllers.ValidacaoAnaliseTecnicaGerenteController = ValidacaoAnaliseTecnicaGerenteController;
