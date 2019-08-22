var ParecerOrgaoController = function(mensagem, $scope, parecerOrgaoService, $rootScope, $route) {

	$scope.showMensagem = function () {

		var params = {id: $route.current.params.idComunicado};
		parecerOrgaoService.enviar(params)
			.then(function (response) {
				window.location = $rootScope.config.baseURL;
		});
	};

};

exports.controllers.ParecerOrgaoController = ParecerOrgaoController;