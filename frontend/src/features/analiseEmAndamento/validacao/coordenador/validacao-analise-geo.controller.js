var ValidacaoAnaliseGeoCoordenadorController = function($rootScope,
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
                                                    validacaoAnaliseCoordenadorService, 
                                                    analistaService,
                                                    parecerAnalistaGeoService) {


    var validacaoAnaliseGeoCoordenador = this;

    validacaoAnaliseGeoCoordenador.analiseGeoValidacao = {};
    validacaoAnaliseGeoCoordenador.camadasDadosEmpreendimento = {};
    validacaoAnaliseGeoCoordenador.dadosProjeto = {};
    validacaoAnaliseGeoCoordenador.acaoTramitacao = app.utils.AcaoTramitacao;

    validacaoAnaliseGeoCoordenador.init = init;
    validacaoAnaliseGeoCoordenador.controleVisualizacao = null;
    validacaoAnaliseGeoCoordenador.exibirDadosProcesso = exibirDadosProcesso;
    validacaoAnaliseGeoCoordenador.concluir = concluir;
    validacaoAnaliseGeoCoordenador.baixarDocumento = baixarDocumento;
    validacaoAnaliseGeoCoordenador.verificarTamanhoInconsistencias = verificarTamanhoInconsistencias;
    validacaoAnaliseGeoCoordenador.openModalOficio = openModalOficio;
    validacaoAnaliseGeoCoordenador.openModalNotificacao = openModalNotificacao;
    validacaoAnaliseGeoCoordenador.analistasGeo = null;
    validacaoAnaliseGeoCoordenador.analistaGeoDestino = {};
    validacaoAnaliseGeoCoordenador.dadosRestricoesProjeto = [];
    validacaoAnaliseGeoCoordenador.titulo = 'VALIDAÇÃO GEO';
    validacaoAnaliseGeoCoordenador.orgaos = app.utils.Orgao;
    validacaoAnaliseGeoCoordenador.enumCategoria = app.utils.Inconsistencia;
    validacaoAnaliseGeoCoordenador.parecerGeo = {};
    validacaoAnaliseGeoCoordenador.labelDadosProjeto = '';
    validacaoAnaliseGeoCoordenador.enumCategoria = app.utils.Inconsistencia;
    validacaoAnaliseGeoCoordenador.enumDocumentos = app.utils.TiposDocumentosAnalise;
    validacaoAnaliseGeoCoordenador.possuiAnaliseTemporal = false;
    validacaoAnaliseGeoCoordenador.possuiDocumentos = false;
    validacaoAnaliseGeoCoordenador.listaAnalisesGeo = [];
    validacaoAnaliseGeoCoordenador.processo = null;
    validacaoAnaliseGeoCoordenador.inconsistencias = [];
    validacaoAnaliseGeoCoordenador.getItemRestricao = getItemRestricao;
    validacaoAnaliseGeoCoordenador.getDescricaoRestricao= getDescricaoRestricao;

    validacaoAnaliseGeoCoordenador.errors = {
		despacho: false,
        resultadoAnalise: false,
        analistas: false		
    };

    validacaoAnaliseGeoCoordenador.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    var getUltimoParecerAnalistaGeo = function(analiseGeo) {

        parecerAnalistaGeoService.getUltimoParecerAnaliseGeo(analiseGeo.id)
            .then(function(response){

                validacaoAnaliseGeoCoordenador.parecerGeo = response.data;
                verificaDocumentos();

        });
    };

    var findAnalisesGeoByNumeroProcesso = function(processo) {

        analiseGeoService.findAnalisesGeoByNumeroProcesso(btoa(processo.numero))
            .then(function(response){

                validacaoAnaliseGeoCoordenador.listaAnalisesGeo = response.data;
                setInconsistencias();

            });
    
    };

    var setInconsistencias = function() {

        _.forEach(validacaoAnaliseGeoCoordenador.listaAnalisesGeo, function(analise){

            if (analise.inconsistencias.length !== 0 ) {

                validacaoAnaliseGeoCoordenador.inconsistencias = analise.inconsistencias;

            }

        });
    
    };

    function init() {

        validacaoAnaliseGeoCoordenador.controleVisualizacao = "ETAPA_ANALISE_GEO";

        analiseGeoService.getAnaliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseGeoCoordenador.analiseGeo = response.data;
                getUltimoParecerAnalistaGeo(validacaoAnaliseGeoCoordenador.analiseGeo);
                findAnalisesGeoByNumeroProcesso(validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo);
                
                processoService.getInfoProcesso(validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.id).then(function(response){
                    validacaoAnaliseGeoCoordenador.processo = response.data;
                });
                
                getUltimoParecerAnalistaGeo(validacaoAnaliseGeoCoordenador.analiseGeo);

                analiseGeoService.getDadosRestricoesProjeto(validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.id)
                .then(function(response) {
        
                    validacaoAnaliseGeoCoordenador.dadosRestricoesProjeto = response.data;
        
                });

                if(validacaoAnaliseGeoCoordenador.analiseGeo.analistasTecnicos) {
                    validacaoAnaliseGeoCoordenador.analiseGeoValidacao.idAnalistaTecnico =
                        validacaoAnaliseGeoCoordenador.analiseGeo.analistasTecnicos[0].usuario.id;
                }

                if (validacaoAnaliseGeoCoordenador.analiseGeo.tipoResultadoValidacaoCoordenador) {

                    validacaoAnaliseGeoCoordenador.analiseGeoValidacao.idTipoResultadoValidacaoCoordenador =
                        validacaoAnaliseGeoCoordenador.analiseGeo.tipoResultadoValidacaoCoordenador.id;
                }
                
                validacaoAnaliseGeoCoordenador.analiseGeoValidacao.parecerValidacaoCoordenador =
                    validacaoAnaliseGeoCoordenador.analiseGeo.parecerValidacaoCoordenador;

                analistaService.getAnalistasTecnicosByProcesso(validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.id)
                    .then(function(response){
                        validacaoAnaliseGeoCoordenador.analistas = response.data;
                });            
            
                getDadosVisualizar(validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo);
                
            });
        
        $rootScope.$broadcast('atualizarContagemProcessos');
    }

    validacaoAnaliseGeoCoordenador.buscarAnalistasGeoByIdProcesso = function() {
		analistaService.buscarAnalistasGeoByIdProcesso(validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.id)
			.then(function(response) {
				validacaoAnaliseGeoCoordenador.analistasGeo = response.data;
			});
    };
    
    function scrollTop() {
		$anchorScroll();
	}

    function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.id,
            numero: validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.empreendimento.cpfCnpj.length > 11) {

            processo.cnpjEmpreendimento = validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.empreendimento.cpfCnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseGeoCoordenador.analiseGeo.analise.processo.empreendimento.cpfCnpj;
        }		

        processoService.visualizarProcesso(processo);
    }

    validacaoAnaliseGeoCoordenador.downloadPDFparecer = function (analiseGeo) {

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

	validacaoAnaliseGeoCoordenador.downloadPDFCartaImagem = function (analiseGeo) {

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

        if(analiseGeo.tipoResultadoValidacaoCoordenador === null || analiseGeo.tipoResultadoValidacaoCoordenador === undefined) {
            
            validacaoAnaliseGeoCoordenador.errors.resultadoAnalise = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseGeoCoordenador.errors.resultadoAnalise = false;

        }
        
        if(analiseGeo.parecerValidacaoCoordenador === "" || analiseGeo.parecerValidacaoCoordenador === null || analiseGeo.parecerValidacaoCoordenador === undefined) {
            
            validacaoAnaliseGeoCoordenador.errors.despacho = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseGeoCoordenador.errors.despacho = false;

        }

        if(analiseGeo.tipoResultadoValidacaoCoordenador.id === validacaoAnaliseGeoCoordenador.TiposResultadoAnalise.PARECER_NAO_VALIDADO.toString() && (validacaoAnaliseGeoCoordenador.analistaGeoDestino.id === null || validacaoAnaliseGeoCoordenador.analistaGeoDestino.id === undefined)) {
            
            validacaoAnaliseGeoCoordenador.errors.analistas = true;
            mensagem.error("Preencha os campos obrigatórios para prosseguir com a análise.");

        }else{

            validacaoAnaliseGeoCoordenador.errors.analistas = false;

        }

        return !(validacaoAnaliseGeoCoordenador.errors.resultadoAnalise === true || validacaoAnaliseGeoCoordenador.errors.despacho === true || validacaoAnaliseGeoCoordenador.errors.analistas === true);

    }

    function concluir() {

        if(!analiseValida(validacaoAnaliseGeoCoordenador.analiseGeo)){
            return;
        }

        var params = {
            analiseGeo: {
                id: validacaoAnaliseGeoCoordenador.analiseGeo.id,
                idAnalistaDestino: validacaoAnaliseGeoCoordenador.analistaGeoDestino.id
            },
            parecer: validacaoAnaliseGeoCoordenador.analiseGeo.parecerValidacaoCoordenador,
            tipoResultadoAnalise: {id: validacaoAnaliseGeoCoordenador.analiseGeo.tipoResultadoValidacaoCoordenador.id}
        };

        validacaoAnaliseCoordenadorService.concluir(params)
			.then(function(response){
                $location.path("analise-coordenador");
                $timeout(function() {
                    mensagem.success("Validação finalizada!", {referenceId: 5});
                }, 0);
            },function(error){
				mensagem.error(error.data.texto);
			});
    }

    validacaoAnaliseGeoCoordenador.cancelar =function() {

        $location.path("/analise-coordenador");
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

			var restricao = this.dadosRestricoesProjeto.find(function(restricao) {

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

			var restricao = validacaoAnaliseGeoCoordenador.dadosRestricoesProjeto.find(function(restricao) {

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

                validacaoAnaliseGeoCoordenador.camadasDadosEmpreendimento = response.data;
            });
        
        analiseGeoService.getDadosProjeto(idProcesso)
            .then(function (response) {

                validacaoAnaliseGeoCoordenador.dadosProjeto = response.data;

                if(validacaoAnaliseGeoCoordenador.dadosProjeto.categoria === validacaoAnaliseGeoCoordenador.enumCategoria.COMPLEXO || validacaoAnaliseGeoCoordenador.dadosProjeto.complexo) {

                    validacaoAnaliseGeoCoordenador.labelDadosProjeto = 'Dados da área do complexo';

                } else if(validacaoAnaliseGeoCoordenador.dadosProjeto.categoria === validacaoAnaliseGeoCoordenador.enumCategoria.PROPRIEDADE) {

                    validacaoAnaliseGeoCoordenador.labelDadosProjeto = 'Dados da área do empreendimento';

                } else {

                    validacaoAnaliseGeoCoordenador.labelDadosProjeto = 'Dados da(s) área(s) da(s) atividade(s)';

                }

            });
    }

    $scope.getRestricoes = function() {

        if(validacaoAnaliseGeoCoordenador.dadosProjeto.restricoes) {

            return validacaoAnaliseGeoCoordenador.dadosProjeto.restricoes.filter(function(restricao) {

                var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

                return sobreposicao.tipoSobreposicao.orgaosResponsaveis.every(function(orgao) {

                    return orgao.sigla.toUpperCase() !== validacaoAnaliseGeoCoordenador.orgaos.IPHAN && orgao.sigla.toUpperCase() !== validacaoAnaliseGeoCoordenador.orgaos.IBAMA;

                });

            });
        }

        return [];

    };

    function verificarTamanhoInconsistencias () {

        if(validacaoAnaliseGeoCoordenador.analiseGeo) {

            var inconsistencias = angular.copy(validacaoAnaliseGeoCoordenador.inconsistencias);

            return _.remove(inconsistencias, function(i){

                return(i.categoria !== 'ATIVIDADE');

            }).length > 0;

        } 

        return false;

    }
    
    function openModalOficio(restricao) {

        $uibModal.open({

            component: 'modalOficioRestricao',
            backdrop: 'static',
            size: 'lg',
            resolve: {

                restricao: function() {

                    return restricao;

                },

                idAnaliseGeo: function() {

                    return validacaoAnaliseGeoCoordenador.analiseGeo.id;
                }

            }    
        });
    }

	validacaoAnaliseGeoCoordenador.validacaoAbaVoltar = function() {
		
		validacaoAnaliseGeoCoordenador.controleVisualizacao = "ETAPA_ANALISE_GEO";
		
		scrollTop();
    };

    function verificaDocumentos() {

        var qtdDocumentosAnaliseTemporal = 0;

        if (validacaoAnaliseGeoCoordenador.parecerGeo.documentos.length > 0) {

            _.forEach(validacaoAnaliseGeoCoordenador.parecerGeo.documentos, function(documento) {

                if (documento.tipo.id === validacaoAnaliseGeoCoordenador.enumDocumentos.DOCUMENTO_ANALISE_TEMPORAL) {

                    qtdDocumentosAnaliseTemporal++;

                }
            });

            validacaoAnaliseGeoCoordenador.possuiDocumentos = validacaoAnaliseGeoCoordenador.parecerGeo.documentos.length !== qtdDocumentosAnaliseTemporal;
            
            if (qtdDocumentosAnaliseTemporal > 0) {

                validacaoAnaliseGeoCoordenador.possuiAnaliseTemporal = true;

            } 
            
        }

    }

    validacaoAnaliseGeoCoordenador.voltarEtapaAnterior = function(){
		$timeout(function() {
			$('.nav-tabs > .active').prev('li').find('a').trigger('click');
			scrollTop();
			validacaoAnaliseGeoCoordenador.controleVisualizacao = "ETAPA_ANALISE_GEO";
		}, 0);
	};
    
    validacaoAnaliseGeoCoordenador.avancarProximaEtapa = function() {
		$timeout(function() {
			$('.nav-tabs > .active').next('li').find('a').trigger('click');
			validacaoAnaliseGeoCoordenador.controleVisualizacao = "ETAPA_VALIDACAO_ANALISE_GEO";
			scrollTop();
        }, 0);
    };

    validacaoAnaliseGeoCoordenador.validacaoAbaAvancar = function() {
		
        validacaoAnaliseGeoCoordenador.controleVisualizacao = "ETAPA_VALIDACAO_ANALISE_GEO";
        
        scrollTop();
    
    };

    function openModalNotificacao(inconsistencia) {

        var sobreposicaoInconsistencia = inconsistencia.sobreposicaoCaracterizacaoAtividade ? inconsistencia.sobreposicaoCaracterizacaoAtividade : inconsistencia.sobreposicaoCaracterizacaoEmpreendimento ? inconsistencia.sobreposicaoCaracterizacaoEmpreendimento : inconsistencia.sobreposicaoCaracterizacaoComplexo;

        if(inconsistencia.categoria !== validacaoAnaliseGeoCoordenador.enumCategoria.PROPRIEDADE){

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
			templateUrl: 'features/analiseEmAndamento/validacao/coordenador/historicoAnalises/modalHistoricoAnaliseGeo.html',
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

	validacaoAnaliseGeoCoordenador.visualizarJustificativas = function(parecer, analiseGeo, processo){

		abrirModal(parecer, analiseGeo, processo);

	};

};

exports.controllers.ValidacaoAnaliseGeoCoordenadorController = ValidacaoAnaliseGeoCoordenadorController;