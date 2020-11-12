var ValidacaoAnaliseGeoGerenteController = function($rootScope,
                                                    analiseGeoService, 
                                                    $timeout,
                                                    $route, 
                                                    $scope,
                                                    mensagem, 
                                                    $location,
                                                    documentoAnaliseService, 
                                                    $anchorScroll,
                                                    processoService, 
                                                    $uibModal, 
                                                    empreendimentoService, 
                                                    documentoService,
                                                    validacaoAnaliseGerenteService, 
                                                    analistaService,
                                                    parecerAnalistaGeoService) {


    var validacaoAnaliseGeoGerente = this;

    validacaoAnaliseGeoGerente.analiseGeoValidacao = {};
    validacaoAnaliseGeoGerente.camadasDadosEmpreendimento = {};
    validacaoAnaliseGeoGerente.dadosProjeto = {};
    validacaoAnaliseGeoGerente.acaoTramitacao = app.utils.AcaoTramitacao;

    validacaoAnaliseGeoGerente.init = init;
    validacaoAnaliseGeoGerente.controleVisualizacao = null;
    validacaoAnaliseGeoGerente.exibirDadosProcesso = exibirDadosProcesso;
    validacaoAnaliseGeoGerente.concluir = concluir;
    validacaoAnaliseGeoGerente.baixarDocumento = baixarDocumento;
    validacaoAnaliseGeoGerente.verificarTamanhoInconsistencias = verificarTamanhoInconsistencias;
    validacaoAnaliseGeoGerente.openModalOficio = openModalOficio;
    validacaoAnaliseGeoGerente.openModalNotificacao = openModalNotificacao;
    validacaoAnaliseGeoGerente.analistasGeo = null;
    validacaoAnaliseGeoGerente.analistaGeoDestino = {};
    validacaoAnaliseGeoGerente.dadosRestricoesProjeto = [];
    validacaoAnaliseGeoGerente.titulo = 'VALIDAÇÃO GEO';
    validacaoAnaliseGeoGerente.orgaos = app.utils.Orgao;
    validacaoAnaliseGeoGerente.enumCategoria = app.utils.Inconsistencia;
    validacaoAnaliseGeoGerente.parecerGeo = {};
    validacaoAnaliseGeoGerente.labelDadosProjeto = '';
    validacaoAnaliseGeoGerente.enumCategoria = app.utils.Inconsistencia;
    validacaoAnaliseGeoGerente.enumDocumentos = app.utils.TiposDocumentosAnalise;
    validacaoAnaliseGeoGerente.possuiAnaliseTemporal = false;
    validacaoAnaliseGeoGerente.possuiDocumentos = false;
    validacaoAnaliseGeoGerente.listaAnalisesGeo = [];
    validacaoAnaliseGeoGerente.processo = null;
    validacaoAnaliseGeoGerente.inconsistencias = [];
    validacaoAnaliseGeoGerente.getItemRestricao = getItemRestricao;
    validacaoAnaliseGeoGerente.getDescricaoRestricao= getDescricaoRestricao;

    validacaoAnaliseGeoGerente.errors = {
		despacho: false,
        resultadoAnalise: false,
        analistas: false		
    };

    validacaoAnaliseGeoGerente.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    var getUltimoParecerAnalistaGeo = function(analiseGeo) {

        parecerAnalistaGeoService.getUltimoParecerAnaliseGeo(analiseGeo.id)
            .then(function(response){

                validacaoAnaliseGeoGerente.parecerGeo = response.data;
                verificaDocumentos();

        });
    };

    var findAnalisesGeoByNumeroProcesso = function(processo) {

        analiseGeoService.findAnalisesGeoByNumeroProcesso(btoa(processo.numero))
            .then(function(response){

                validacaoAnaliseGeoGerente.listaAnalisesGeo = response.data;
                setInconsistencias();

            });
    
    };

    var setInconsistencias = function() {

        _.forEach(validacaoAnaliseGeoGerente.listaAnalisesGeo, function(analise){

            if (analise.inconsistencias.length !== 0 ) {

                validacaoAnaliseGeoGerente.inconsistencias = analise.inconsistencias;

            }

        });
    
    };

    function init() {

        validacaoAnaliseGeoGerente.controleVisualizacao = "ETAPA_ANALISE_GEO";

        analiseGeoService.getAnaliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseGeoGerente.analiseGeo = response.data;
                getUltimoParecerAnalistaGeo(validacaoAnaliseGeoGerente.analiseGeo);
                findAnalisesGeoByNumeroProcesso(validacaoAnaliseGeoGerente.analiseGeo.analise.processo);
                
                processoService.getInfoProcesso(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id).then(function(response){
                    validacaoAnaliseGeoGerente.processo = response.data;
                });
                
                getUltimoParecerAnalistaGeo(validacaoAnaliseGeoGerente.analiseGeo);

                analiseGeoService.getDadosRestricoesProjeto(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id)
                .then(function(response) {
        
                    validacaoAnaliseGeoGerente.dadosRestricoesProjeto = response.data;
        
                });

                if(validacaoAnaliseGeoGerente.analiseGeo.analistasTecnicos) {
                    validacaoAnaliseGeoGerente.analiseGeoValidacao.idAnalistaTecnico =
                        validacaoAnaliseGeoGerente.analiseGeo.analistasTecnicos[0].usuario.id;
                }

                if (validacaoAnaliseGeoGerente.analiseGeo.tipoResultadoValidacaoGerente) {

                    validacaoAnaliseGeoGerente.analiseGeoValidacao.idTipoResultadoValidacaoGerente =
                        validacaoAnaliseGeoGerente.analiseGeo.tipoResultadoValidacaoGerente.id;
                }
                
                validacaoAnaliseGeoGerente.analiseGeoValidacao.parecerValidacaoGerente =
                    validacaoAnaliseGeoGerente.analiseGeo.parecerValidacaoGerente;

                analistaService.getAnalistasTecnicosByProcesso(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id)
                    .then(function(response){
                        validacaoAnaliseGeoGerente.analistas = response.data;
                });            
            
                getDadosVisualizar(validacaoAnaliseGeoGerente.analiseGeo.analise.processo);
                
            });
        
        $rootScope.$broadcast('atualizarContagemProcessos');
    }

    validacaoAnaliseGeoGerente.buscarAnalistasGeoByIdProcesso = function() {
		analistaService.buscarAnalistasGeoByIdProcesso(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id)
			.then(function(response) {
				validacaoAnaliseGeoGerente.analistasGeo = response.data;
			});
    };
    
    function scrollTop() {
		$anchorScroll();
	}

    function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id,
            numero: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.cpfCnpj.length > 11) {

            processo.cnpjEmpreendimento = validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.cpfCnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.cpfCnpj;
        }		

        processoService.visualizarProcesso(processo);
    }

    validacaoAnaliseGeoGerente.downloadPDFparecer = function (analiseGeo) {

		var params = {
			id: analiseGeo.id
		};

		documentoAnaliseService.generatePDFParecerGeo(params)
			.then(function(data, status, headers){

				var url = URL.createObjectURL(data.data.response.blob);
                window.open(url, '_blank');

			},function(error){
				mensagem.error(error.data.texto);
			});
	};

	validacaoAnaliseGeoGerente.downloadPDFCartaImagem = function (analiseGeo) {

		var params = {
			id: analiseGeo.id
		};

		documentoAnaliseService.generatePDFCartaImagemGeo(params)
			.then(function(data, status, headers){

				var url = URL.createObjectURL(data.data.response.blob);
                window.open(url, '_blank');

			},function(error){
				mensagem.error(error.data.texto);
			});
    };
    
    function analiseValida(analiseGeo) {

        if(analiseGeo.tipoResultadoValidacaoGerente === null || analiseGeo.tipoResultadoValidacaoGerente === undefined) {
            
            validacaoAnaliseGeoGerente.errors.resultadoAnalise = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseGeoGerente.errors.resultadoAnalise = false;

        }
        
        if(analiseGeo.parecerValidacaoGerente === "" || analiseGeo.parecerValidacaoGerente === null || analiseGeo.parecerValidacaoGerente === undefined) {
            
            validacaoAnaliseGeoGerente.errors.despacho = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseGeoGerente.errors.despacho = false;

        }

        if(analiseGeo.tipoResultadoValidacaoGerente.id === validacaoAnaliseGeoGerente.TiposResultadoAnalise.PARECER_NAO_VALIDADO.toString() && (validacaoAnaliseGeoGerente.analistaGeoDestino.id === null || validacaoAnaliseGeoGerente.analistaGeoDestino.id === undefined)) {
            
            validacaoAnaliseGeoGerente.errors.analistas = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseGeoGerente.errors.analistas = false;

        }

        if(validacaoAnaliseGeoGerente.errors.resultadoAnalise === true || validacaoAnaliseGeoGerente.errors.despacho === true || validacaoAnaliseGeoGerente.errors.analistas === true){
            
            return false;
            
        }
        
        return true;

    }

    function concluir() {

        if(!analiseValida(validacaoAnaliseGeoGerente.analiseGeo)){
            return;
        }

        var params = {
            analiseGeo: {
                id: validacaoAnaliseGeoGerente.analiseGeo.id,
                idAnalistaDestino: validacaoAnaliseGeoGerente.analistaGeoDestino.id
            },
            parecer: validacaoAnaliseGeoGerente.analiseGeo.parecerValidacaoGerente,
            tipoResultadoAnalise: {id: validacaoAnaliseGeoGerente.analiseGeo.tipoResultadoValidacaoGerente.id}
        };

        validacaoAnaliseGerenteService.concluir(params)
			.then(function(response){
                $location.path("analise-gerente");
                $timeout(function() {
                    mensagem.success("Validação finalizada!", {referenceId: 5});
                }, 0);
            },function(error){
				mensagem.error(error.data.texto);
			});
    }

    validacaoAnaliseGeoGerente.cancelar =function() {

        $location.path("/analise-gerente");
    };

    function baixarDocumento(documento) {
        if(!documento.id){
			documentoService.download(documento.key, documento.nomeDoArquivo);
		}else{
			analiseGeoService.download(documento.id);
		}
    }

    function getItemRestricao(inconsistencia) {

		var sobreposicaoInconsistencia = inconsistencia.sobreposicaoCaracterizacaoAtividade ? inconsistencia.sobreposicaoCaracterizacaoAtividade : inconsistencia.sobreposicaoCaracterizacaoEmpreendimento ? inconsistencia.sobreposicaoCaracterizacaoEmpreendimento : inconsistencia.sobreposicaoCaracterizacaoComplexo;

		if(inconsistencia.categoria.toUpperCase() !== 'PROPRIEDADE') {

			restricao = this.dadosRestricoesProjeto.find(function(restricao) {

				var sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

				return sobreposicaoInconsistencia.id === sobreposicaoRestricao.id;

			});

			return restricao && restricao.item ? restricao.item : '';

		}

		return 'Propriedade';
	
	}

	function getDescricaoRestricao(inconsistencia) {

		var sobreposicaoInconsistencia = inconsistencia.sobreposicaoCaracterizacaoAtividade ? inconsistencia.sobreposicaoCaracterizacaoAtividade : inconsistencia.sobreposicaoCaracterizacaoEmpreendimento ? inconsistencia.sobreposicaoCaracterizacaoEmpreendimento : inconsistencia.sobreposicaoCaracterizacaoComplexo;

		if(inconsistencia.categoria.toUpperCase() !== 'PROPRIEDADE') {

			restricao = validacaoAnaliseGeoGerente.dadosRestricoesProjeto.find(function(restricao) {

				var sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

				return sobreposicaoInconsistencia.id === sobreposicaoRestricao.id;

			});

			return restricao && restricao.descricao ? restricao.descricao : '';

		}

		return '-';
	
    }
    
    $scope.getOrgaos = function(restricao){
        
        var orgaos = [];
        var sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;
        _.forEach(sobreposicaoRestricao.tipoSobreposicao.orgaosResponsaveis, function(orgao){
            //verifica se o orgão da restrição é IPHAN ou IBAMA
            orgaos.push(orgao);
        });
        return orgaos;
    };

    function getDadosVisualizar(processo) {
        
        var cpfCnpjEmpreendimento = processo.empreendimento.cpfCnpj;

        var idProcesso = processo.id;

        empreendimentoService.getDadosGeoEmpreendimento(cpfCnpjEmpreendimento)
			.then(function(response) {

                validacaoAnaliseGeoGerente.camadasDadosEmpreendimento = response.data;
            });
        
        analiseGeoService.getDadosProjeto(idProcesso)
            .then(function (response) {

                validacaoAnaliseGeoGerente.dadosProjeto = response.data;

                if(validacaoAnaliseGeoGerente.dadosProjeto.categoria === validacaoAnaliseGeoGerente.enumCategoria.COMPLEXO || validacaoAnaliseGeoGerente.dadosProjeto.complexo) {

                    validacaoAnaliseGeoGerente.labelDadosProjeto = 'Dados da área do complexo';

                } else if(validacaoAnaliseGeoGerente.dadosProjeto.categoria === validacaoAnaliseGeoGerente.enumCategoria.PROPRIEDADE) {

                    validacaoAnaliseGeoGerente.labelDadosProjeto = 'Dados da área do empreendimento';

                } else {

                    validacaoAnaliseGeoGerente.labelDadosProjeto = 'Dados da(s) área(s) da(s) atividade(s)';

                }

            });
    }

    $scope.getRestricoes = function() {

        if(validacaoAnaliseGeoGerente.dadosProjeto.restricoes) {

            return validacaoAnaliseGeoGerente.dadosProjeto.restricoes.filter(function(restricao) {

                var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

                return sobreposicao.tipoSobreposicao.orgaosResponsaveis.every(function(orgao) {

                    return orgao.sigla.toUpperCase() !== validacaoAnaliseGeoGerente.orgaos.IPHAN && orgao.sigla.toUpperCase() !== validacaoAnaliseGeoGerente.orgaos.IBAMA;

                });

            });
        }

        return [];

    };

    function verificarTamanhoInconsistencias () {

        if(validacaoAnaliseGeoGerente.analiseGeo) {

            var inconsistencias = angular.copy(validacaoAnaliseGeoGerente.inconsistencias);

            return _.remove(inconsistencias, function(i){

                return(i.categoria !== 'ATIVIDADE');

            }).length > 0;

        } 

        return false;

    }
    
    function openModalOficio(restricao) {
        var modalInstance = $uibModal.open({

            component: 'modalOficioRestricao',
            backdrop: 'static',
            size: 'lg',
            resolve: {

                restricao: function() {

                    return restricao;

                },

                idAnaliseGeo: function() {

                    return validacaoAnaliseGeoGerente.analiseGeo.id;
                }

            }    
        });
    }

	validacaoAnaliseGeoGerente.validacaoAbaVoltar = function() {
		
		validacaoAnaliseGeoGerente.controleVisualizacao = "ETAPA_ANALISE_GEO";
		
		scrollTop();
    };

    function verificaDocumentos() {

        var qtdDocumentosAnaliseTemporal = 0;

        if (validacaoAnaliseGeoGerente.parecerGeo.documentos.length > 0) {

            _.forEach(validacaoAnaliseGeoGerente.parecerGeo.documentos, function(documento) {

                if (documento.tipo.id === validacaoAnaliseGeoGerente.enumDocumentos.DOCUMENTO_ANALISE_TEMPORAL) {

                    qtdDocumentosAnaliseTemporal++;

                }
            });

            if (validacaoAnaliseGeoGerente.parecerGeo.documentos.length === qtdDocumentosAnaliseTemporal) {

                validacaoAnaliseGeoGerente.possuiDocumentos = false;
    
            } else {

                validacaoAnaliseGeoGerente.possuiDocumentos = true;

            }
            
            if (qtdDocumentosAnaliseTemporal > 0) {

                validacaoAnaliseGeoGerente.possuiAnaliseTemporal = true;

            } 
            
        }

    }

    validacaoAnaliseGeoGerente.voltarEtapaAnterior = function(){
		$timeout(function() {
			$('.nav-tabs > .active').prev('li').find('a').trigger('click');
			scrollTop();
			validacaoAnaliseGeoGerente.controleVisualizacao = "ETAPA_ANALISE_GEO";
		}, 0);
	};
    
    validacaoAnaliseGeoGerente.avancarProximaEtapa = function() {
		$timeout(function() {
			$('.nav-tabs > .active').next('li').find('a').trigger('click');
			validacaoAnaliseGeoGerente.controleVisualizacao = "ETAPA_VALIDACAO_ANALISE_GEO";
			scrollTop();
        }, 0);
    };

    validacaoAnaliseGeoGerente.validacaoAbaAvancar = function() {
		
        validacaoAnaliseGeoGerente.controleVisualizacao = "ETAPA_VALIDACAO_ANALISE_GEO";
        
        scrollTop();
    
    };

    function openModalNotificacao(inconsistencia) {

        var sobreposicaoInconsistencia = inconsistencia.sobreposicaoCaracterizacaoAtividade ? inconsistencia.sobreposicaoCaracterizacaoAtividade : inconsistencia.sobreposicaoCaracterizacaoEmpreendimento ? inconsistencia.sobreposicaoCaracterizacaoEmpreendimento : inconsistencia.sobreposicaoCaracterizacaoComplexo;

        if(inconsistencia.categoria !== validacaoAnaliseGeoGerente.enumCategoria.PROPRIEDADE){

            var restricao = this.dadosRestricoesProjeto.find(function(restricao) {

                if (restricao.sobreposicaoCaracterizacaoEmpreendimento !== null) {

                    return restricao.sobreposicaoCaracterizacaoEmpreendimento.id === sobreposicaoInconsistencia.id;

                } else if (restricao.sobreposicaoCaracterizacaoAtividade !== null) {

                    return restricao.sobreposicaoCaracterizacaoAtividade.id === sobreposicaoInconsistencia.id;

                } else if (restricao.sobreposicaoCaracterizacaoComplexo !== null) {

                    return restricao.sobreposicaoCaracterizacaoComplexo.id === sobreposicaoInconsistencia.id;

                }

            });

        }

        $uibModal.open({

            component: 'modalNotificacaoRestricao',
            backdrop: 'static',
            size: 'lg',
            resolve: {

                inconsistencia: function() {

                    return inconsistencia;
                },

                restricao: function() {
                    return restricao;
                }
            }    
        });

    }

    var abrirModal = function(parecer, analiseGeo, processo) {

		$uibModal.open({
			controller: 'historicoAnaliseGeoCtrl',
			controllerAs: 'historicoAnaliseGeoCtrl',
			templateUrl: 'features/analiseEmAndamento/validacao/gerenteTecnico/historicoAnalises/modalHistoricoAnaliseGeo.html',
			size: 'lg',
			resolve: {

				parecer: function() {
					return parecer;
				},
				
				analiseGeo: function() {
					return analiseGeo;
                },
                
                processo: function(){
                    return processo;
                }

			}
		});

	};

	validacaoAnaliseGeoGerente.visualizarJustificativas = function(parecer, analiseGeo, processo){

		abrirModal(parecer, analiseGeo, processo);

	};

};

exports.controllers.ValidacaoAnaliseGeoGerenteController = ValidacaoAnaliseGeoGerenteController;
