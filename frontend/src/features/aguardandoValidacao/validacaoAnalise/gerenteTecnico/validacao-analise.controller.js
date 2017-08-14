var ValidacaoAnaliseTecnicaGerenteController = function($rootScope, analiseTecnicaService, $route, $scope, 
		mensagem, $location, documentoAnaliseService, processoService, $uibModal, analistaService) {

    var validacaoAnaliseTecnicaGerente = this;

    validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao = {};

    validacaoAnaliseTecnicaGerente.init = init;
    validacaoAnaliseTecnicaGerente.exibirDadosProcesso = exibirDadosProcesso; 
    validacaoAnaliseTecnicaGerente.exibirAnaliseJuridica = exibirAnaliseJuridica;
    validacaoAnaliseTecnicaGerente.downloadDocumentoAnalise = downloadDocumentoAnalise;
	validacaoAnaliseTecnicaGerente.isParecerNaoValidado = isParecerNaoValidado;
	validacaoAnaliseTecnicaGerente.isObrigatorio = isObrigatorio;
	validacaoAnaliseTecnicaGerente.cancelar = cancelar;
	validacaoAnaliseTecnicaGerente.concluir = concluir;

    validacaoAnaliseTecnicaGerente.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;  

    function init() {

		analiseTecnicaService.getAnaliseTecnica($route.current.params.idAnalise)
			.then(function(response){
				validacaoAnaliseTecnicaGerente.analiseTecnica = response.data;

				validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao.idAnalistaTecnico =
					validacaoAnaliseTecnicaGerente.analiseTecnica.analistasTecnicos[0].usuario.id;
                
                if (validacaoAnaliseTecnicaGerente.analiseTecnica.tipoResultadoValidacaoGerente) {

                    validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao.idTipoResultadoValidacaoGerente =
                        validacaoAnaliseTecnicaGerente.analiseTecnica.tipoResultadoValidacaoGerente.id;
                }
                
                validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao.parecerValidacaoGerente =
                    validacaoAnaliseTecnicaGerente.analiseTecnica.parecerValidacaoGerente;

                analistaService.getAnalistasTecnicosByProcesso(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.id)
                    .then(function(response){
                        validacaoAnaliseTecnicaGerente.analistas = response.data;
                });            
		    

			});

		
		$rootScope.$broadcast('atualizarContagemProcessos');        
    }

	function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.id,
            numero: validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.empreendimento.pessoa.cpf;
        }		

        processoService.visualizarProcesso(processo);
    }    

    function exibirAnaliseJuridica() {

        processoService.getAnaliseJuridica(validacaoAnaliseTecnicaGerente.analiseTecnica.analise.processo.id)
            .then(function(response){

                var modalInstance = $uibModal.open({

                    component: 'modalInformacoesAnaliseJuridica',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {

                        idAnalise: function() {

                            return response.data.id;
                        }
                    }    
                });    
            });
    }

    function downloadDocumentoAnalise(idDocumento) {

        documentoAnaliseService.download(idDocumento);
    }

	function isParecerNaoValidado() {

		return validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao.idTipoResultadoValidacaoGerente === 
                                validacaoAnaliseTecnicaGerente.TiposResultadoAnalise.PARECER_NAO_VALIDADO;
	}

	function isObrigatorio() {
		
        return [validacaoAnaliseTecnicaGerente.TiposResultadoAnalise.PARECER_NAO_VALIDADO,
				validacaoAnaliseTecnicaGerente.TiposResultadoAnalise.SOLICITAR_AJUSTES]
				.indexOf(validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao.idTipoResultadoValidacaoGerente) !== -1;
	}

    function cancelar() {

        $location.path('aguardando-validacao');
    }

	function montarAnaliseTecnica(analiseTecnicaValidacao){
		return {
			id: validacaoAnaliseTecnicaGerente.analiseTecnica.id,
			tipoResultadoValidacaoGerente: { id : analiseTecnicaValidacao.idTipoResultadoValidacaoGerente},
			parecerValidacaoGerente: analiseTecnicaValidacao.parecerValidacaoGerente,
			analistasTecnicos:[ 
				{
					usuario: {
						id: analiseTecnicaValidacao.idAnalistaTecnico
					}
				}
			]
		};
	}   

	function concluir() {

		$scope.formularioValidacao.$setSubmitted();

		if (!$scope.formularioValidacao.$valid){

            mensagem.error('Preencha os campos destacados em vermelho para prosseguir com a validação.');
			return;
        }

        var analiseTecnica = montarAnaliseTecnica(validacaoAnaliseTecnicaGerente.analiseTecnicaValidacao);

		analiseTecnicaService.validarParecerGerente(analiseTecnica)
            .then(function(response) {

                mensagem.success(response.data.texto);
				$location.path('aguardando-validacao');

            }, function(error){

                mensagem.error(error.data.texto);
            });
    }
};

exports.controllers.ValidacaoAnaliseTecnicaGerenteController = ValidacaoAnaliseTecnicaGerenteController;
