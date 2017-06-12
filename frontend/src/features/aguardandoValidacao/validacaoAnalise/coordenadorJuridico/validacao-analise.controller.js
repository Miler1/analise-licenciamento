var ValidacaoAnaliseJuridicaController = function($rootScope, analiseJuridicaService, $route) {

	$rootScope.tituloPagina = 'VALIDAÇÃO PARECER JURÍDICO';

	var validacaoAnaliseJuridica = this;

	validacaoAnaliseJuridica.getAnaliseJuridica = getAnaliseJuridica;

	function getAnaliseJuridica() {

		analiseJuridicaService.getAnaliseJuridicaById($route.current.params.idAnalise)
			.then(function(response){
				validacaoAnaliseJuridica.analiseJuridica = response.data;
				validacaoAnaliseJuridica.analiseJuridica.parecer = 'testando';
			});
	}
};

exports.controllers.ValidacaoAnaliseJuridicaController = ValidacaoAnaliseJuridicaController;