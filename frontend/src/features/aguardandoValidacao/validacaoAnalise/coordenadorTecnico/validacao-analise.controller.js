var ValidacaoAnaliseTecnicaController = function($rootScope, analiseTecnicaService, $route, $scope, 
		mensagem, $location, documentoAnaliseService, processoService, $uibModal, analistaService, gerenteService) {

    var validacaoAnaliseTecnica = this;

    validacaoAnaliseTecnica.analiseTecnicaValidacao = {};

    validacaoAnaliseTecnica.init = init;
    validacaoAnaliseTecnica.exibirDadosProcesso = exibirDadosProcesso; 
    validacaoAnaliseTecnica.exibirAnaliseJuridica = exibirAnaliseJuridica;
    validacaoAnaliseTecnica.downloadDocumentoAnalise = downloadDocumentoAnalise;
	validacaoAnaliseTecnica.isParecerNaoValidado = isParecerNaoValidado;
	validacaoAnaliseTecnica.isObrigatorio = isObrigatorio;
	validacaoAnaliseTecnica.cancelar = cancelar;
	validacaoAnaliseTecnica.concluir = concluir;
    validacaoAnaliseTecnica.getAnalistasTecnicos = getAnalistasTecnicos;
    validacaoAnaliseTecnica.getGerentesTecnicos = getGerentesTecnicos;
    validacaoAnaliseTecnica.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;  

    function init() {

		analiseTecnicaService.getAnaliseTecnica($route.current.params.idAnalise)
			.then(function(response){
				validacaoAnaliseTecnica.analiseTecnica = response.data;

				validacaoAnaliseTecnica.analiseTecnicaValidacao.idAnalistaTecnico =
					validacaoAnaliseTecnica.analiseTecnica.analistasTecnicos[0].usuario.id;                
			});

		analistaService.getAnalistasTecnicos()
			.then(function(response){
				validacaoAnaliseTecnica.analistas = response.data;
			});            
		
		$rootScope.$broadcast('atualizarContagemProcessos');        
    }

    function getAnalistasTecnicos() {

        analistaService.getAnalistasTecnicosByProcesso(validacaoAnaliseTecnica.analiseTecnica.analise.processo.id)
            .then(function(response){
                validacaoAnaliseTecnica.analistasGerentes = response.data;
            });
    }

    function getGerentesTecnicos() {

        gerenteService.getGerentesTecnicosByIdProcesso(validacaoAnaliseTecnica.analiseTecnica.analise.processo.id)
            .then(function(response){
                validacaoAnaliseTecnica.analistasGerentes = response.data;
            });
    }

	function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseTecnica.analiseTecnica.analise.processo.id,
            numero: validacaoAnaliseTecnica.analiseTecnica.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cpf;
        }		

        processoService.visualizarProcesso(processo);
    }    

    function exibirAnaliseJuridica() {

        processoService.getAnaliseJuridica(validacaoAnaliseTecnica.analiseTecnica.analise.processo.id)
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

		return validacaoAnaliseTecnica.analiseTecnicaValidacao.idTipoResultadoValidacao === 
                                validacaoAnaliseTecnica.TiposResultadoAnalise.PARECER_NAO_VALIDADO;
	}

	function isObrigatorio() {
		
        return [validacaoAnaliseTecnica.TiposResultadoAnalise.PARECER_NAO_VALIDADO,
				validacaoAnaliseTecnica.TiposResultadoAnalise.SOLICITAR_AJUSTES]
				.indexOf(validacaoAnaliseTecnica.analiseTecnicaValidacao.idTipoResultadoValidacao) !== -1;
	}

    function cancelar() {

        $location.path('aguardando-validacao');
    }

	function montarAnaliseTecnica(analiseTecnicaValidacao){
		return {
			id: validacaoAnaliseTecnica.analiseTecnica.id,
			tipoResultadoValidacao: { id : analiseTecnicaValidacao.idTipoResultadoValidacao},
			parecerValidacao: analiseTecnicaValidacao.parecerValidacao,
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

        var analiseTecnica = montarAnaliseTecnica(validacaoAnaliseTecnica.analiseTecnicaValidacao);

		analiseTecnicaService.validarParecer(analiseTecnica)
            .then(function(response) {

                mensagem.success(response.data.texto);
				$location.path('aguardando-validacao');

            }, function(error){

                mensagem.error(error.data.texto);
            });
    }    
};

exports.controllers.ValidacaoAnaliseTecnicaController = ValidacaoAnaliseTecnicaController;