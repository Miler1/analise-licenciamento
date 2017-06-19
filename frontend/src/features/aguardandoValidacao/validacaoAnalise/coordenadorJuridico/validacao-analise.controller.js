var ValidacaoAnaliseJuridicaController = function($rootScope, analiseJuridicaService, $route, $scope, 
		mensagem, $location, consultorService, documentoAnaliseService, processoService) {

	$rootScope.tituloPagina = 'VALIDAÇÃO PARECER JURÍDICO';

	var validacaoAnaliseJuridica = this;

	validacaoAnaliseJuridica.analiseJuridicaValidacao = {};

	validacaoAnaliseJuridica.init = init;
	validacaoAnaliseJuridica.downloadDocumentoAnalise = downloadDocumentoAnalise;
	validacaoAnaliseJuridica.isParecerNaoValidado = isParecerNaoValidado;
	validacaoAnaliseJuridica.cancelar = cancelar;
	validacaoAnaliseJuridica.concluir = concluir;

	validacaoAnaliseJuridica.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

	validacaoAnaliseJuridica.exibirDadosProcesso = exibirDadosProcesso; 
	
	function init() {

		analiseJuridicaService.getAnaliseJuridica($route.current.params.idAnalise)
			.then(function(response){
				validacaoAnaliseJuridica.analiseJuridica = response.data;

				validacaoAnaliseJuridica.analiseJuridicaValidacao.idConsultorJuridico =
					validacaoAnaliseJuridica.analiseJuridica.consultoresJuridicos[0].usuario.id;
			});
		
		consultorService.getConsultoresJuridicos()
			.then(function(response){
				validacaoAnaliseJuridica.consultores = response.data;
			});

		$rootScope.$broadcast('atualizarContagemProcessos');
	}

    function downloadDocumentoAnalise(idDocumento) {

        documentoAnaliseService.download(idDocumento);
    }

	function isParecerNaoValidado() {

		return validacaoAnaliseJuridica.analiseJuridicaValidacao.idTipoResultadoValidacao === validacaoAnaliseJuridica.TiposResultadoAnalise.PARECER_NAO_VALIDADO;
	}

    function cancelar() {

        $location.path('aguardando-validacao');
    }

	function concluir(){

		$scope.formularioValidacao.$setSubmitted();

		if (!$scope.formularioValidacao.$valid){

            mensagem.error('Preencha os campos destacados em vermelho para prosseguir com a validação.');
			return;
        }

        var analiseJuridica = montarAnaliseJuridica(validacaoAnaliseJuridica.analiseJuridicaValidacao);

		analiseJuridicaService.validarParecer(analiseJuridica)
            .then(function(response) {

                mensagem.success(response.data.texto);
				$location.path('aguardando-validacao');

            }, function(error){

                mensagem.error(error.data.texto);
            });
    }

	function montarAnaliseJuridica(analiseJuridicaValidacao){
		return {
			id: validacaoAnaliseJuridica.analiseJuridica.id,
			tipoResultadoValidacao: { id : analiseJuridicaValidacao.idTipoResultadoValidacao},
			parecerValidacao: analiseJuridicaValidacao.parecerValidacao,
			consultoresJuridicos:[ 
				{
					usuario: {
						id: analiseJuridicaValidacao.idConsultorJuridico
					}
				}
			]
		};
	}

	function exibirDadosProcesso() {

        processoService.visualizarProcesso({ idProcesso:validacaoAnaliseJuridica.analiseJuridica.analise.processo.id});
    }
};

exports.controllers.ValidacaoAnaliseJuridicaController = ValidacaoAnaliseJuridicaController;