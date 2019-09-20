var ValidacaoAnaliseGeoGerenteController = function($rootScope, analiseGeoService ,analiseTecnicaService, $route, $scope, 
		mensagem, $location, documentoAnaliseService, processoService, $uibModal, analistaService) {

    var validacaoAnaliseGeoGerente = this;

    validacaoAnaliseGeoGerente.analiseTecnicaValidacao = {};

    validacaoAnaliseGeoGerente.init = init;
    validacaoAnaliseGeoGerente.exibirDadosProcesso = exibirDadosProcesso; 
    // validacaoAnaliseGeoGerente.exibirAnaliseJuridica = exibirAnaliseJuridica;
    // validacaoAnaliseGeoGerente.downloadDocumentoAnalise = downloadDocumentoAnalise;
	// validacaoAnaliseGeoGerente.isParecerNaoValidado = isParecerNaoValidado;
	// validacaoAnaliseGeoGerente.isObrigatorio = isObrigatorio;
	// validacaoAnaliseGeoGerente.cancelar = cancelar;
	validacaoAnaliseGeoGerente.concluir = concluir;

    validacaoAnaliseGeoGerente.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    function init() {

		analiseGeoService.getAnliseGeoByAnalise($route.current.params.idAnalise)
			.then(function(response){
				validacaoAnaliseGeoGerente.analiseGeo = response.data;

				validacaoAnaliseGeoGerente.analiseGeoValidacao.idAnalistaTecnico =
					validacaoAnaliseGeoGerente.analiseGeo.analistasTecnicos[0].usuario.id;
                
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
		    

			});
        
        $('#situacao-fundiaria').summernote('disable');
		
		$rootScope.$broadcast('atualizarContagemProcessos');
    }

	function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id,
            numero: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.pessoa.cpf;
        }		

        processoService.visualizarProcesso(processo);
    }    

    // function exibirAnaliseJuridica() {

    //     processoService.getAnaliseJuridica(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id)
    //         .then(function(response){

    //             var modalInstance = $uibModal.open({

    //                 component: 'modalInformacoesAnaliseJuridica',
    //                 size: 'lg',
    //                 backdrop: 'static',
    //                 resolve: {

    //                     idAnalise: function() {

    //                         return response.data.id;
    //                     }
    //                 }    
    //             });    
    //         });
    // }

    // function downloadDocumentoAnalise(idDocumento) {

    //     documentoAnaliseService.download(idDocumento);
    // }

	// function isParecerNaoValidado() {

	// 	return validacaoAnaliseGeoGerente.analiseTecnicaValidacao.idTipoResultadoValidacaoGerente === 
    //                             validacaoAnaliseGeoGerente.TiposResultadoAnalise.PARECER_NAO_VALIDADO;
	// }

	// function isObrigatorio() {
		
    //     return [validacaoAnaliseGeoGerente.TiposResultadoAnalise.PARECER_NAO_VALIDADO,
	// 			validacaoAnaliseGeoGerente.TiposResultadoAnalise.SOLICITAR_AJUSTES]
	// 			.indexOf(validacaoAnaliseGeoGerente.analiseTecnicaValidacao.idTipoResultadoValidacaoGerente) !== -1;
	// }

    // function cancelar() {

    //     $location.path('aguardando-validacao');
    // }

	// function montarAnaliseTecnica(analiseTecnicaValidacao){
	// 	return {
	// 		id: validacaoAnaliseGeoGerente.analiseTecnica.id,
	// 		tipoResultadoValidacaoGerente: { id : analiseTecnicaValidacao.idTipoResultadoValidacaoGerente},
	// 		parecerValidacaoGerente: analiseTecnicaValidacao.parecerValidacaoGerente,
	// 		analistasTecnicos:[ 
	// 			{
	// 				usuario: {
	// 					id: analiseTecnicaValidacao.idAnalistaTecnico
	// 				}
	// 			}
	// 		]
	// 	};
	// }   

	function concluir() {

		$scope.formularioValidacao.$setSubmitted();

		if (!$scope.formularioValidacao.$valid){

            mensagem.error('Preencha os campos destacados em vermelho para prosseguir com a validação.');
			return;
        }

        var analiseTecnica = montarAnaliseTecnica(validacaoAnaliseGeoGerente.analiseTecnicaValidacao);

		analiseTecnicaService.validarParecerGerente(analiseTecnica)
            .then(function(response) {

                mensagem.success(response.data.texto);
				$location.path('aguardando-validacao');

            }, function(error){

                mensagem.error(error.data.texto);
            });
    }
};

exports.controllers.ValidacaoAnaliseGeoGerenteController = ValidacaoAnaliseGeoGerenteController;
