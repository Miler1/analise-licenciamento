var ValidacaoAnaliseTecnicaCoordenadorController = function($rootScope,
                                                    analiseTecnicaService,
                                                    $timeout,
                                                    $route,
                                                    mensagem,
                                                    $location,
                                                    documentoAnaliseService,
                                                    $anchorScroll,
                                                    $uibModal,
                                                    documentoService,
                                                    validacaoAnaliseCoordenadorService,
                                                    analistaService,
                                                    processoService,
                                                    parecerAnalistaTecnicoService) {

    var validacaoAnaliseTecnicaCoordenador = this;

    validacaoAnaliseTecnicaCoordenador.analistasTecnicos = null;
    validacaoAnaliseTecnicaCoordenador.analistaTecnicoDestino = {};
    validacaoAnaliseTecnicaCoordenador.analiseTecnicaValidacao = {};
    validacaoAnaliseTecnicaCoordenador.dadosProjeto = {};
    validacaoAnaliseTecnicaCoordenador.init = init;
    validacaoAnaliseTecnicaCoordenador.controleVisualizacao = null;
    validacaoAnaliseTecnicaCoordenador.baixarDocumento = baixarDocumento;
    validacaoAnaliseTecnicaCoordenador.titulo = 'VALIDAÇÃO TÉCNICA';
    validacaoAnaliseTecnicaCoordenador.parecerTecnico = {};
    validacaoAnaliseTecnicaCoordenador.labelDadosProjeto = '';
    validacaoAnaliseTecnicaCoordenador.enumDocumentos = app.utils.TiposDocumentosAnalise;
    validacaoAnaliseTecnicaCoordenador.concluir = concluir;
    validacaoAnaliseTecnicaCoordenador.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnaliseTecnicaCoordenador.listaAnalisesTecnicas = [];
    validacaoAnaliseTecnicaCoordenador.processo = null;
    validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga = null;
    validacaoAnaliseTecnicaCoordenador.documentos = [];

    validacaoAnaliseTecnicaCoordenador.possuiAutoInfracao = false;

    validacaoAnaliseTecnicaCoordenador.errors = {

	despacho: false,
        resultadoAnalise: false,
        analistas: false

    };

    validacaoAnaliseTecnicaCoordenador.disable = {

        tipoLicenca: true,
        atividade: [],
        questionario: true,
        documentoAdministrativo: [],
        documentoTecnicoAmbiental: []

    };

    validacaoAnaliseTecnicaCoordenador.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    var findAnalisesTecnicaByNumeroProcesso = function(processo) {

        analiseTecnicaService.findAnalisesTecnicaByNumeroProcesso(btoa(processo.numero))
            .then(function(response){

                validacaoAnaliseTecnicaCoordenador.listaAnalisesTecnicas = response.data;

            });
    };

    function getAnaliseTecnica(idAnaliseTecnica){

        analiseTecnicaService.getAnaliseTecnica(idAnaliseTecnica)
        .then(function(response){

            validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga = response.data;
            findAnalisesTecnicaByNumeroProcesso(validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.analise.processo);

            _.filter(validacaoAnaliseTecnicaCoordenador.parecerTecnico.documentos , function(documento){

                if(documento.tipo.id === validacaoAnaliseTecnicaCoordenador.enumDocumentos.AUTO_INFRACAO){
                    validacaoAnaliseTecnicaCoordenador.possuiAutoInfracao = true;
                }else {
                    validacaoAnaliseTecnicaCoordenador.documentos.push(documento);
                }
            });

            _.forEach(validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, index){

                validacaoAnaliseTecnicaCoordenador.disable.atividade[index] = {
                    atividadeValida: true,
                    parametros: []
                };

                _.forEach(atividade.atividade.parametros, function(parametro, indexParametro) {
                    validacaoAnaliseTecnicaCoordenador.disable.atividade[index].parametros[indexParametro] = true;
                });
            });

            _.forEach(validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.analise.processo.caracterizacao.solicitacoesDocumento, function(documentoAdministrativo, index){
                    validacaoAnaliseTecnicaCoordenador.disable.documentoAdministrativo[index] = true;
            });

            _.forEach(validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.analise.processo.caracterizacao.documentosSolicitacaoGrupo, function(documentoTecnicoAmbiental, index){
                validacaoAnaliseTecnicaCoordenador.disable.documentoTecnicoAmbiental[index] = true;
            });

            _.forEach( validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.inconsistenciasTecnica, function(inconsistenciaTecnica){

                if(inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca !== null){
                    validacaoAnaliseTecnicaCoordenador.disable.tipoLicenca = false;
                }

                if(inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null){

                    _.forEach(validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, index){

                        if(atividade.id === inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id){

                            validacaoAnaliseTecnicaCoordenador.disable.atividade[index].atividadeValida = false;
                        }
                    });
                }

                if(inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null){

                    _.forEach(validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, indexAtividade){

                        _.forEach(atividade.atividade.parametros, function(parametro, indexParametro) {

                            if(inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id === parametro.id &&
                                inconsistenciaTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.id === atividade.id){

                                    validacaoAnaliseTecnicaCoordenador.disable.atividade[indexAtividade].parametros[indexParametro] = false;
                                }
                        });
                    });
                }

                if(inconsistenciaTecnica.inconsistenciaTecnicaQuestionario !== null){
                    validacaoAnaliseTecnicaCoordenador.disable.questionario = false;
                }

                if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo !== null){

                    _.forEach(validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.analise.processo.caracterizacao.solicitacoesDocumento, function(documentoAdministrativo, index){
                        if(documentoAdministrativo.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id){

                            validacaoAnaliseTecnicaCoordenador.disable.documentoAdministrativo[index] = false;
                        }
                    });
                }

                if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental !== null){

                    _.forEach(validacaoAnaliseTecnicaCoordenador.analiseTecnicaAntiga.analise.processo.caracterizacao.documentosSolicitacaoGrupo, function(documentoTecnicoAmbiental, index){
                        validacaoAnaliseTecnicaCoordenador.disable.documentoTecnicoAmbiental[index] = false;
                    });
                }
            });

        });
    }

    function init() {

        validacaoAnaliseTecnicaCoordenador.controleVisualizacao = "ETAPA_ANALISE_TECNICA";
        analiseTecnicaService.getAnaliseTecnicaByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseTecnicaCoordenador.analiseTecnica = response.data;

                processoService.getInfoProcesso(validacaoAnaliseTecnicaCoordenador.analiseTecnica.analise.processo.id).then(function(response){

                    validacaoAnaliseTecnicaCoordenador.processo = response.data;

                    if(validacaoAnaliseTecnicaCoordenador.processo.processoAnterior === undefined || validacaoAnaliseTecnicaCoordenador.processo.processoAnterior === null  ){

                        getAnaliseTecnica(validacaoAnaliseTecnicaCoordenador.analiseTecnica.id);

                    } else{

                        if(validacaoAnaliseTecnicaCoordenador.processo.processoAnterior.analise.analisesTecnicas.length === 0) {

                            getAnaliseTecnica(validacaoAnaliseTecnicaCoordenador.analiseTecnica.id);

                        } else {

                            getAnaliseTecnica(validacaoAnaliseTecnicaCoordenador.processo.processoAnterior.analise.analisesTecnicas[0].id);
                        }

                    }

                    getUltimoParecerAnalistaTecnico(validacaoAnaliseTecnicaCoordenador.analiseTecnica);

                });
            });

        $rootScope.$broadcast('atualizarContagemProcessos');
    }

    var getUltimoParecerAnalistaTecnico = function(analiseTecnica) {

        parecerAnalistaTecnicoService.getUltimoParecerAnaliseTecnica(analiseTecnica.id)
            .then(function(response){

                validacaoAnaliseTecnicaCoordenador.parecerTecnico = response.data;

        });

    };

    validacaoAnaliseTecnicaCoordenador.downloadPDFParecer = function(analiseTecnica) {

        documentoService.downloadParecerByIdAnaliseTecnica(analiseTecnica.id);

    };

    validacaoAnaliseTecnicaCoordenador.downloadPDFMinuta = function(analiseTecnica) {

        documentoService.downloadMinutaByIdAnaliseTecnica(analiseTecnica.id);

    };

    validacaoAnaliseTecnicaCoordenador.downloadRTVByIdAnaliseTecnica = function(analiseTecnica) {

        documentoService.downloadRTVByIdAnaliseTecnica(analiseTecnica.id);

    };

    validacaoAnaliseTecnicaCoordenador.visualizarAutoInfracao = function(documentosAnaliseTecnica) {

        _.filter(documentosAnaliseTecnica, function(documento){
            if(documento.tipo.id === validacaoAnaliseTecnicaCoordenador.enumDocumentos.AUTO_INFRACAO){
                documentoAnaliseService.download(documento.id);
            }
        });
    };

    validacaoAnaliseTecnicaCoordenador.exibirDadosProcesso = function () {

        var processo = {

            idProcesso: validacaoAnaliseTecnicaCoordenador.analiseTecnica.analise.processo.id,
            numero: validacaoAnaliseTecnicaCoordenador.analiseTecnica.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseTecnicaCoordenador.analiseTecnica.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseTecnicaCoordenador.analiseTecnica.analise.processo.empreendimento.cpfCnpj.length > 11) {

            processo.cnpjEmpreendimento = validacaoAnaliseTecnicaCoordenador.analiseTecnica.analise.processo.empreendimento.cpfCnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseTecnicaCoordenador.analiseTecnica.analise.processo.empreendimento.cpfCnpj;
        }


        processoService.visualizarProcesso(processo);
    };

    validacaoAnaliseTecnicaCoordenador.buscarAnalistasTecnicoByIdProcesso = function() {
		analistaService.buscarAnalistasTecnicoByIdProcesso(validacaoAnaliseTecnicaCoordenador.analiseTecnica.analise.processo.id)
			.then(function(response) {
				validacaoAnaliseTecnicaCoordenador.analistasTecnicos = response.data;
			});
    };

    function scrollTop() {
		$anchorScroll();
	}

    validacaoAnaliseTecnicaCoordenador.cancelar = function() {

        $location.path("/analise-coordenador");
    };

    function baixarDocumento(documento) {
        if(!documento.id){
			documentoService.download(documento.key, documento.nomeDoArquivo);
		}else{
            		documentoService.downloadById(documento.id);
		}
    }

	validacaoAnaliseTecnicaCoordenador.validacaoAbaVoltar = function() {

		validacaoAnaliseTecnicaCoordenador.controleVisualizacao = "ETAPA_ANALISE_TECNICA";

		scrollTop();
	};

    validacaoAnaliseTecnicaCoordenador.voltarEtapaAnterior = function(){
		$timeout(function() {
			$('.nav-tabs > .active').prev('li').find('a').trigger('click');
			scrollTop();
			validacaoAnaliseTecnicaCoordenador.controleVisualizacao = "ETAPA_ANALISE_TECNICA";
		}, 0);
	};

    validacaoAnaliseTecnicaCoordenador.avancarProximaEtapa = function() {
		$timeout(function() {
			$('.nav-tabs > .active').next('li').find('a').trigger('click');
			validacaoAnaliseTecnicaCoordenador.controleVisualizacao = "ETAPA_VALIDACAO_ANALISE_TECNICA";
			scrollTop();
        }, 0);
    };

    validacaoAnaliseTecnicaCoordenador.validacaoAbaAvancar = function() {

        validacaoAnaliseTecnicaCoordenador.controleVisualizacao = "ETAPA_VALIDACAO_ANALISE_TECNICA";

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
                isCoordenador: function(){
                    return true;
                }
            }
        });
    }


    validacaoAnaliseTecnicaCoordenador.visualizarInconsistenciaTecnicaTipoLicenca = function (analiseTecnica, tipoDeInconsistenciaTecnica) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
            .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){

                    return inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca;
                });

            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, null, null, null, null, null);
        });
    };

    validacaoAnaliseTecnicaCoordenador.visualizarInconsistenciaTecnicaAtividade = function (analiseTecnica, tipoDeInconsistenciaTecnica, atividadeCaracterizacao, index) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
        .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){
                return inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null &&
                    inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id === atividadeCaracterizacao.id;
            });

            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, atividadeCaracterizacao, null, null, null, null, index, null);
        });

    };

    validacaoAnaliseTecnicaCoordenador.visualizarInconsistenciaTecnicaParametro = function (analiseTecnica, tipoDeInconsistenciaTecnica, parametroAtividade, index, indexParametro, atividade) {

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

    validacaoAnaliseTecnicaCoordenador.visualizarInconsistenciaTecnicaQuestionario = function (analiseTecnica,tipoDeInconsistenciaTecnica) {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
        .then(function(response){

            var inconsistenciaTecnica = _.find( response.data.inconsistenciasTecnica, function(inconsistenciaTecnica){

                return inconsistenciaTecnica.inconsistenciaTecnicaQuestionario;
            });

            openModal(response.data, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, analiseTecnica.analise.processo.caracterizacao.questionario3, null, null, null, null);
        });
    };

    validacaoAnaliseTecnicaCoordenador.visualizarInconsistenciaTecnicaDocumentoAdministrativo = function (analiseTecnica, tipoDeInconsistenciaTecnica, documentoAdministrativo, index) {

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

    validacaoAnaliseTecnicaCoordenador.visualizarInconsistenciaTecnicaDocumentoTecnicoAmbiental = function (analiseTecnica,tipoDeInconsistenciaTecnica, documentoTecnicoAmbiental, index) {

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

        if(analiseTecnica.tipoResultadoValidacaoCoordenador === null || analiseTecnica.tipoResultadoValidacaoCoordenador === undefined) {

            validacaoAnaliseTecnicaCoordenador.errors.resultadoAnalise = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseTecnicaCoordenador.errors.resultadoAnalise = false;

        }

        if(analiseTecnica.parecerValidacaoCoordenador === "" || analiseTecnica.parecerValidacaoCoordenador === null || analiseTecnica.parecerValidacaoCoordenador === undefined) {

            validacaoAnaliseTecnicaCoordenador.errors.despacho = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseTecnicaCoordenador.errors.despacho = false;
        }
        if (analiseTecnica.tipoResultadoValidacaoCoordenador !== null) {

            if(analiseTecnica.tipoResultadoValidacaoCoordenador.id === validacaoAnaliseTecnicaCoordenador.TiposResultadoAnalise.PARECER_NAO_VALIDADO.toString() && (validacaoAnaliseTecnicaCoordenador.analistaTecnicoDestino.id === null || validacaoAnaliseTecnicaCoordenador.analistaTecnicoDestino.id === undefined)) {

                validacaoAnaliseTecnicaCoordenador.errors.analistas = true;
                mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

            }else{

                validacaoAnaliseTecnicaCoordenador.errors.analistas = false;

            }

        }

        return !(validacaoAnaliseTecnicaCoordenador.errors.resultadoAnalise === true || validacaoAnaliseTecnicaCoordenador.errors.despacho === true || validacaoAnaliseTecnicaCoordenador.errors.analistas === true);

    }

    function concluir() {

        if(!analiseValida(validacaoAnaliseTecnicaCoordenador.analiseTecnica)){
            return;
        }

        var params = {
            analiseTecnica: {
                id: validacaoAnaliseTecnicaCoordenador.analiseTecnica.id,
                idAnalistaDestino: validacaoAnaliseTecnicaCoordenador.analistaTecnicoDestino.id
            },
            parecer: validacaoAnaliseTecnicaCoordenador.analiseTecnica.parecerValidacaoCoordenador,
            tipoResultadoAnalise: {id: validacaoAnaliseTecnicaCoordenador.analiseTecnica.tipoResultadoValidacaoCoordenador.id}
        };

        validacaoAnaliseCoordenadorService.concluirParecerTecnico(params)
			.then(function(response){
                $location.path("analise-coordenador");
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
			templateUrl: 'features/analiseEmAndamento/validacao/coordenador/historicoAnalises/modalHistoricoAnaliseTecnica.html',
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

	validacaoAnaliseTecnicaCoordenador.visualizarJustificativas = function(parecer, analiseTecnica, processo){

		abrirModal(parecer, analiseTecnica, processo);

	};
};

exports.controllers.ValidacaoAnaliseTecnicaCoordenadorController = ValidacaoAnaliseTecnicaCoordenadorController;
