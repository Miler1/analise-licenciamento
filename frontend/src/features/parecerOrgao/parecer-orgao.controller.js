var ParecerOrgaoController = function(mensagem, $scope, parecerOrgaoService, $rootScope,$routeParams, $timeout) {

	$scope.comunicado = null;
	
	$timeout(function () {
		
		parecerOrgaoService.findComunicado($routeParams.idComunicado)
		.then(function(response){

			$scope.comunicado = response.data;
			
			if (!comunicado.valido) {
				window.location = $rootScope.config.baseURL;
			}

		}).catch(function(response){

			mensagem.error(response.data);

		});
		
	}, 100);
	
	$scope.voltar = function () {
		window.location = $rootScope.config.baseURL;
	};

	$scope.enviar = function () {

		var params = {id: $routeParams.idComunicado,
					  parecerOrgao: $scope.descricaoParecer};
		parecerOrgaoService.enviar(params)
			.then(function (response) {

				window.location = $rootScope.config.baseURL;
		});
	};

};

exports.controllers.ParecerOrgaoController = ParecerOrgaoController;