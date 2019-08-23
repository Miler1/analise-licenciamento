var ParecerOrgaoController = function(mensagem, $scope, parecerOrgaoService, $window,$rootScope,$routeParams, $timeout) {

	$scope.comunicado = null;
	
	$timeout(function () {
		
		parecerOrgaoService.findComunicado($routeParams.idComunicado)
		.then(function(response){

			$scope.comunicado = response.data;
			
			if (!comunicado.valido) {
				$window.location.href="http://www.ipaam.am.gov.br/";
			}

		}).catch(function(response){

			mensagem.error(response.data);

		});
		
	}, 100);
	
	$scope.cancelar = function () {
		$window.location.href="http://www.ipaam.am.gov.br/";
	};

	$scope.enviar = function () {

		var params = {id: $routeParams.idComunicado,
					  parecerOrgao: $scope.descricaoParecer};
		parecerOrgaoService.enviar(params)
			.then(function (response) {

				$window.location.href="http://www.ipaam.am.gov.br/";
		});
	};

};

exports.controllers.ParecerOrgaoController = ParecerOrgaoController;