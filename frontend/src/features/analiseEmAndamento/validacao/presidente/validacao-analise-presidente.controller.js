var ValidacaoAnalisePresidenteController = function($uibModal,
                                                 $route,
                                                 desvinculoService,      
                                                 analiseGeoService,
                                                 parecerDiretorTecnicoService,
                                                 parecerGerenteService,
                                                 parecerAnalistaTecnicoService,
                                                 parecerAnalistaGeoService,
                                                 processoService,
                                                 $location) {

    var validacaoAnalisePresidente = this;

    validacaoAnalisePresidente.init = init;
    validacaoAnalisePresidente.titulo = 'RESUMO DO PROTOCOLO';   
    validacaoAnalisePresidente.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    validacaoAnalisePresidente.tipoDocumento =  app.utils.TiposDocumentosAnalise;
    validacaoAnalisePresidente.dadosProcesso = null;
    validacaoAnalisePresidente.analiseGeo = null;
    validacaoAnalisePresidente.dateUtil = app.utils.DateUtil;
    validacaoAnalisePresidente.analiseTecnica = null;
    validacaoAnalisePresidente.acaoTramitacao = app.utils.AcaoTramitacao;

    function init() {

        analiseGeoService.getAnaliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){

                validacaoAnalisePresidente.analiseGeo = response.data;

                processoService.getInfoProcessoByNumero(validacaoAnalisePresidente.analiseGeo.analise.processo.numero)
                    .then(function(response){

                        validacaoAnalisePresidente.dadosProcesso = response.data;

                });

            });     

    }

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
                historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_PARECER_TECNICO_GERENTE ||
                historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.VALIDAR_PARECER_TECNICO_GERENTE){
			
			parecerGerenteService.findParecerTecnicoByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
                });
                
        } else if(historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.VALIDAR_ANALISE_PELO_DIRETOR ||
                historico.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_ANALISE_PELO_DIRETOR ){

			parecerDiretorTecnicoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
            	.then(function(response){
					abrirModal(response.data, idProcesso);
				});

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
           tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.INVALIDAR_PARECER_TECNICO_GERENTE||
		   tramitacao.idAcao === validacaoAnalisePresidente.acaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE;
	};

};

exports.controllers.ValidacaoAnalisePresidenteController = ValidacaoAnalisePresidenteController;
