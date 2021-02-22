var ValidacaoAnaliseSecretarioController = function($uibModal,
												 $route,
												 $timeout,
												 mensagem,
                                                 desvinculoService,      
                                                 analiseTecnicaService,
												 parecerDiretorTecnicoService,
												 parecerSecretarioService,
                                                 parecerCoordenadorService,
                                                 parecerAnalistaTecnicoService,
                                                 parecerAnalistaGeoService,
												 processoService,
												 documentoService,
												 $rootScope,
                                                 $location) {

    var validacaoAnaliseSecretario = this;

    validacaoAnaliseSecretario.init = init;
    validacaoAnaliseSecretario.titulo = 'RESUMO DO PROTOCOLO';   
    validacaoAnaliseSecretario.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnaliseSecretario.tipoDocumento =  app.utils.TiposDocumentosAnalise;
    validacaoAnaliseSecretario.dadosProcesso = null;
    validacaoAnaliseSecretario.dateUtil = app.utils.DateUtil;
    validacaoAnaliseSecretario.analiseTecnica = null;
	validacaoAnaliseSecretario.acaoTramitacao = app.utils.AcaoTramitacao;
	validacaoAnaliseSecretario.idTipoResultadoAnalise = null;
	validacaoAnaliseSecretario.exibirDadosProcesso = exibirDadosProcesso;
	validacaoAnaliseSecretario.possuiValidade = null;
	validacaoAnaliseSecretario.tipologias = app.utils.Tipologia;

	validacaoAnaliseSecretario.errors = {
		despacho: false
	};

    function init() {

		analiseTecnicaService.getAnaliseTecnicaByAnalise($route.current.params.idAnalise)
            .then(function(response){

				validacaoAnaliseSecretario.analiseTecnica = response.data;
				validacaoAnaliseSecretario.possuiValidade = (validacaoAnaliseSecretario.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao[0].atividade.tipologia.codigo === validacaoAnaliseSecretario.tipologias.ID_AQUICULTURA) ? false : true;
				getUltimoParecerAnalistaTecnico(validacaoAnaliseSecretario.analiseTecnica);
				
				processoService.getInfoProcessoByNumero(validacaoAnaliseSecretario.analiseTecnica.analise.processo.numero)
				.then(function(response){

					validacaoAnaliseSecretario.dadosProcesso = response.data;

				});

            });	
			 
        parecerDiretorTecnicoService.findParecerByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnaliseSecretario.parecerDiretor = response.data;
                
			});
			
		$rootScope.$broadcast('atualizarContagemProcessos');

	}

	function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseSecretario.analiseTecnica.analise.processo.id,
            numero: validacaoAnaliseSecretario.analiseTecnica.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseSecretario.analiseTecnica.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseSecretario.analiseTecnica.analise.processo.empreendimento.cpfCnpj.length) {

            processo.cnpjEmpreendimento = validacaoAnaliseSecretario.analiseTecnica.analise.processo.empreendimento.cpfCnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseSecretario.analiseTecnica.analise.processo.empreendimento.cpfCnpj;
        }		

        processoService.visualizarProcesso(processo);
    }



	validacaoAnaliseSecretario.baixarDocumento = function (analiseTecnica, tipoDocumento ) {

		if ( tipoDocumento === validacaoAnaliseSecretario.tipoDocumento.DOCUMENTO_MINUTA ) {

            documentoService.downloadMinutaByIdAnaliseTecnica(analiseTecnica.id);

        }
                    
    };
	
    var getUltimoParecerAnalistaTecnico = function(analiseTecnica) {

        parecerAnalistaTecnicoService.getUltimoParecerAnaliseTecnica(analiseTecnica.id)
            .then(function(response){

				validacaoAnaliseSecretario.parecerTecnico = response.data;
				
        });

    };


    validacaoAnaliseSecretario.cancelar = function() {

        $location.path("/analise-secretario");
    };

    var abrirModal = function(parecer, idProcesso) {

		$uibModal.open({
			controller: 'visualizarJustificativasController',
			controllerAs: 'visualizarJustificativasCtlr',
			templateUrl: 'components/visualizacaoProcesso/modalVisualizarObservacao.html',
			size: 'lg',
			resolve: {

				parecer: function() {
					return parecer;
				},
				
				idProcesso: function() {
					return idProcesso;
				}

			}
		});

	};

	validacaoAnaliseSecretario.getDiasAnaliseGeo = function() {

		if (validacaoAnaliseSecretario.dadosProcesso) {

			return validacaoAnaliseSecretario.dateUtil.calcularDias(validacaoAnaliseSecretario.dadosProcesso.analise.dataCadastro, validacaoAnaliseSecretario.dadosProcesso.analise.analiseGeo.dataFim);
		}
	};

	validacaoAnaliseSecretario.getDiasAnaliseTecnica = function() {

		if (validacaoAnaliseSecretario.dadosProcesso) {

			return validacaoAnaliseSecretario.dadosProcesso.analise.analiseTecnica ? validacaoAnaliseSecretario.dateUtil.calcularDias(validacaoAnaliseSecretario.dadosProcesso.analise.analiseTecnica.dataCadastro, validacaoAnaliseSecretario.dadosProcesso.analise.analiseTecnica.dataFim) : '-';
		}
    };

	this.getAnaliseTotal = function() {

		if (validacaoAnaliseSecretario.dadosProcesso) {

			var diasAnaliseTecnica = validacaoAnaliseSecretario.getDiasAnaliseTecnica(validacaoAnaliseSecretario.dadosProcesso.analise.analiseTecnica);
			diasAnaliseTecnica = diasAnaliseTecnica === '-' ? 0 : diasAnaliseTecnica;

			return validacaoAnaliseSecretario.getDiasAnaliseGeo() + diasAnaliseTecnica;
		}
	};

    this.visualizarJustificativas = function(idProcesso, historico){

		if(historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_GEO){

			desvinculoService.buscarDesvinculoPeloProcessoGeo(idProcesso)
            	.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		} else if(historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_TECNICA){

			desvinculoService.buscarDesvinculoPeloProcessoTecnico(idProcesso)
            	.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		}else if(historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.VALIDAR_PARECER_GEO_COORDENADOR ||
                  historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_COORDENADOR ||
				  historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO) {

				parecerCoordenadorService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
					.then(function(response){
						abrirModal(response.data, idProcesso);
					});

		}else if(historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_COORDENADOR ||
				historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_COORDENADOR ||
				historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_TECNICO){

			parecerAnalistaTecnicoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

        } else if(historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR ||
				// historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_COORDENADOR ||
				historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO||
                historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.VALIDAR_PARECER_TECNICO_COORDENADOR){
			
			parecerCoordenadorService.findParecerTecnicoByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
                });
                
        } else if(historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.VALIDAR_ANALISE_PELO_DIRETOR ||
                historico.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INVALIDAR_ANALISE_PELO_DIRETOR ){

				abrirModal(validacaoAnaliseSecretario.parecerDiretor, idProcesso);	

		}else {

			parecerAnalistaGeoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		}

	};

	this.validaJustificativas = function (tramitacao){

		return tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.DEFERIR_ANALISE_GEO || 
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INDEFERIR_ANALISE_GEO ||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_GEO ||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_TECNICO ||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_GEO ||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_TECNICA || 
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.VALIDAR_PARECER_GEO_COORDENADOR ||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_COORDENADOR ||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR ||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO || 
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.AGUARDAR_RESPOSTA_COMUNICADO||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_COORDENADOR||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO||
           tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.VALIDAR_ANALISE_PELO_DIRETOR||
           tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INVALIDAR_ANALISE_PELO_DIRETOR|| 
           tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.VALIDAR_PARECER_TECNICO_COORDENADOR|| 
           tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_COORDENADOR||
		   tramitacao.idAcao === validacaoAnaliseSecretario.acaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_COORDENADOR;
	};

	validacaoAnaliseSecretario.openModalLicenca = function (parecerTecnico, dadosProcesso, analiseTecnica) {

		$uibModal.open({

            component: 'modalVisualizarSolicitacaoLicenca',
            backdrop: 'static',
            size: 'lg',
            resolve: {

                parecerTecnico: function() {

                    return parecerTecnico;

				},
				
				analiseTecnica: function() {

                    return analiseTecnica;

                },

                dadosProcesso: function() {
					
                    return dadosProcesso;
				},
				
				processo: function(){
					return validacaoAnaliseSecretario.analiseTecnica.analise.processo;
				}

            }    
        });

	};

	validacaoAnaliseSecretario.concluir = function () {

		var parecerValido = !validacaoAnaliseSecretario.parecerSecretario ? validacaoAnaliseSecretario.errors.despacho = true : validacaoAnaliseSecretario.errors.despacho = false;

		if(parecerValido){
			mensagem.error('Não foi possível concluir a análise. Verifique os campos obrigatórios!', { ttl: 5000 });
			return;
		}
		
		var params = {

			analise: {
                id: $route.current.params.idAnalise,
            },
			tipoResultadoAnalise: {id: validacaoAnaliseSecretario.idTipoResultadoAnalise},
			parecer: validacaoAnaliseSecretario.parecerSecretario

		};

		parecerSecretarioService.concluirParecerSecretario(params)
			.then(function(response){
				
				$location.path('/analise-secretario');
				$timeout(function() {
                    mensagem.success("Validação finalizada!", {referenceId: 5});
                }, 0);

		}).catch(function(){

			mensagem.error("Não foi possível concluir a análise");

		});

    };

};

exports.controllers.ValidacaoAnaliseSecretarioController = ValidacaoAnaliseSecretarioController;
