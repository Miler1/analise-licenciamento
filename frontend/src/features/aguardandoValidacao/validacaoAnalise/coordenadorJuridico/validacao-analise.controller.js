var ValidacaoAnaliseJuridicaController = function($rootScope, analiseJuridicaService, $route, $scope, mensagem, $location, consultorService) {

	$rootScope.tituloPagina = 'VALIDAÇÃO PARECER JURÍDICO';

	var validacaoAnaliseJuridica = this;

	validacaoAnaliseJuridica.analiseJuridicaValidacao = {};

	validacaoAnaliseJuridica.init = init;
	validacaoAnaliseJuridica.downloadDocumentoAnalise = downloadDocumentoAnalise;
	validacaoAnaliseJuridica.isParecerNaoValidado = isParecerNaoValidado;
	validacaoAnaliseJuridica.cancelar = cancelar;
	validacaoAnaliseJuridica.concluir = concluir;

	validacaoAnaliseJuridica.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

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

		//Aguardando serviço para validar parecer jurídico
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
};

exports.controllers.ValidacaoAnaliseJuridicaController = ValidacaoAnaliseJuridicaController;