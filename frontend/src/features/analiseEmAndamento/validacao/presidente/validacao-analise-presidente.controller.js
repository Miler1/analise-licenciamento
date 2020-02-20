var ValidacaoAnalisePresidenteController = function($uibModal,
												 $route,
												 mensagem,
                                                 desvinculoService,      
                                                 analiseTecnicaService,
												 parecerDiretorTecnicoService,
												 parecerPresidenteService,
                                                 parecerGerenteService,
                                                 parecerAnalistaTecnicoService,
                                                 parecerAnalistaGeoService,
												 processoService,
												 documentoService,
                                                 $location) {

    var validacaoAnalisePresidente = this;

    validacaoAnalisePresidente.init = init;
    validacaoAnalisePresidente.titulo = 'RESUMO DO PROTOCOLO';   
    validacaoAnalisePresidente.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnalisePresidente.tipoDocumento =  app.utils.TiposDocumentosAnalise;
    validacaoAnalisePresidente.dadosProcesso = null;
    validacaoAnalisePresidente.dateUtil = app.utils.DateUtil;
    validacaoAnalisePresidente.analiseTecnica = null;
	validacaoAnalisePresidente.acaoTramitacao = app.utils.AcaoTramitacao;
	validacaoAnalisePresidente.idTipoResultadoAnalise = null;

	validacaoAnalisePresidente.errors = {
		despacho: false
	};

    function init() {

		analiseTecnicaService.getAnaliseTecnicaByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnalisePresidente.analiseTecnica = response.data;
				validacaoAnalisePresidente.parecerTecnico = getUltimoParecerAnalista(validacaoAnalisePresidente.analiseTecnica.pareceresAnalistaTecnico);
				
				processoService.getInfoProcessoByNumero(validacaoAnalisePresidente.analiseTecnica.analise.processo.numero)
				.then(function(response){

					validacaoAnalisePresidente.dadosProcesso = response.data;

				});

            });	
			 
        parecerDiretorTecnicoService.findParecerByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnalisePresidente.parecerDiretor = response.data;
                
            });

	}

	validacaoAnalisePresidente.baixarDocumento = function (analiseTecnica, tipoDocumento ) {

		if ( tipoDocumento === validacaoAnalisePresidente.tipoDocumento.DOCUMENTO_MINUTA ) {

            documentoService.downloadMinutaByIdAnaliseTecnica(analiseTecnica.id);

        }
                    
    };
	
	var getUltimoParecerAnalista = function(pareceresAnalista) {

        var pareceresOrdenados = pareceresAnalista.sort(function(dataParecer1, dataParecer2){
            return dataParecer1 - dataParecer2;
        });

        return pareceresOrdenados[pareceresOrdenados.length - 1];

    };

    validacaoAnalisePresidente.cancelar = function() {

        $location.path("/analise-presidente");
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

	validacaoAnalisePresidente.getDiasAnaliseGeo = function() {

		if (validacaoAnalisePresidente.dadosProcesso) {

			return validacaoAnalisePresidente.dateUtil.calcularDias(validacaoAnalisePresidente.dadosProcesso.analise.dataCadastro, validacaoAnalisePresidente.dadosProcesso.analise.analiseGeo.dataFim);
		}
	};

	validacaoAnalisePresidente.getDiasAnaliseTecnica = function() {

		if (validacaoAnalisePresidente.dadosProcesso) {

			return validacaoAnalisePresidente.dadosProcesso.analise.analiseTecnica ? validacaoAnalisePresidente.dateUtil.calcularDias(validacaoAnalisePresidente.dadosProcesso.analise.analiseTecnica.dataCadastro, validacaoAnalisePresidente.dadosProcesso.analise.analiseTecnica.dataFim) : '-';
		}
    };

	this.getAnaliseTotal = function() {

		if (validacaoAnalisePresidente.dadosProcesso) {

			var diasAnaliseTecnica = validacaoAnalisePresidente.getDiasAnaliseTecnica(validacaoAnalisePresidente.dadosProcesso.analise.analiseTecnica);
			diasAnaliseTecnica = diasAnaliseTecnica === '-' ? 0 : diasAnaliseTecnica;

			return validacaoAnalisePresidente.getDiasAnaliseGeo() + diasAnaliseTecnica;
		}
	};

    this.visualizarJustificativas = function(idProcesso, historico){

		if(historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_GEO){

			desvinculoService.buscarDesvinculoPeloProcessoGeo(idProcesso)
            	.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		} else if(historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_TECNICA){

			desvinculoService.buscarDesvinculoPeloProcessoTecnico(idProcesso)
            	.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		}else if(historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.VALIDAR_PARECER_GEO_GERENTE ||
                  historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE ||
				  historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO) {

				parecerGerenteService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
					.then(function(response){
						abrirModal(response.data, idProcesso);
					});

		}else if(historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE ||
				historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_GERENTE ||
				historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_TECNICO){

			parecerAnalistaTecnicoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

        } else if(historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_GERENTE ||
                historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_GERENTE ||
                historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.VALIDAR_PARECER_TECNICO_GERENTE){
			
			parecerGerenteService.findParecerTecnicoByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
                });
                
        } else if(historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.VALIDAR_ANALISE_PELO_DIRETOR ||
                historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_ANALISE_PELO_DIRETOR ){

				abrirModal(validacaoAnalisePresidente.parecerDiretor, idProcesso);	

		}else {

			parecerAnalistaGeoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		}

	};

	this.validaJustificativas = function (tramitacao){

		return tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.DEFERIR_ANALISE_GEO || 
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.INDEFERIR_ANALISE_GEO ||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_GEO ||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_TECNICO ||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_GEO ||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_TECNICA || 
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.VALIDAR_PARECER_GEO_GERENTE ||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE ||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_GERENTE ||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO || 
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.AGUARDAR_RESPOSTA_COMUNICADO||
           tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_GERENTE||
           tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.VALIDAR_ANALISE_PELO_DIRETOR||
           tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_ANALISE_PELO_DIRETOR|| 
           tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.VALIDAR_PARECER_TECNICO_GERENTE|| 
           tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_GERENTE||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE;
	};

	validacaoAnalisePresidente.openModalLicenca = function (parecerTecnico, dadosProcesso, analiseTecnica) {

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
                }

            }    
        });

	};

	validacaoAnalisePresidente.concluir = function () {

		var parecerValido = !validacaoAnalisePresidente.parecerPresidente ? validacaoAnalisePresidente.errors.despacho = true : validacaoAnalisePresidente.errors.despacho = false;

		if(parecerValido){
			mensagem.error('Não foi possível concluir a análise. Verifique os campos obrigatórios!', { ttl: 5000 });
			return;
		}
		
		var params = {

			analise: {
                id: $route.current.params.idAnalise,
            },
			tipoResultadoAnalise: {id: validacaoAnalisePresidente.idTipoResultadoAnalise},
			parecer: validacaoAnalisePresidente.parecerPresidente

		};

		parecerPresidenteService.concluirParecerPresidente(params)
			.then(function(response){
				
				mensagem.success(response.data);

		}, function(){

			mensagem.error("Não foi possível concluir a análise");

		});

    };

};

exports.controllers.ValidacaoAnalisePresidenteController = ValidacaoAnalisePresidenteController;
