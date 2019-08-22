var ParecerOrgaoController = function(mensagem, $scope, parecerOrgaoService, $rootScope ) {

	$scope.showMensagem = function () {
		var params = {id: 1};
		parecerOrgaoService.enviar(params)
			.then(function (response) {
				window.location = $rootScope.config.baseURL;
		});
	};

};

exports.controllers.ParecerOrgaoController = ParecerOrgaoController;