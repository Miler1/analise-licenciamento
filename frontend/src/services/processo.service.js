var ProcessoService = function(request, config, $uibModal) {

	this.getProcessos = function(filtro) {

		return request
			.post(config.BASE_URL() + "processos", filtro);
	};

	this.getProcessosCount = function(filtro) {

		return request
			.post(config.BASE_URL() + "processos/count", filtro);
	};

	this.consultar = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'processos/' + idProcesso);
	};

	this.getAnaliseGeo = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'processos/' + idProcesso + '/analiseGeo');
	};

	this.getAnaliseJuridica = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'processos/' + idProcesso + '/analiseJuridica');
	};

	this.getInfoProcesso = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'processos/' + idProcesso + '/completo');
	};

	this.getInfoProcessoByNumero = function(numeroProcesso) {

		return request
			.get(config.BASE_URL() + 'processos/numero/' + numeroProcesso.replace('/','-') + '/completo');
	};

	this.visualizarProcesso = function(processo) {

		var modalInstance = $uibModal.open({
			controller: 'visualizacaoProcessoController',
			controllerAs: 'modalVisualizacaoProcessoCtrl',
			templateUrl: 'components/visualizacaoProcesso/visualizacaoProcesso.html',
			windowClass: 'modalVisualizarProcesso',
			size: 'lg',
			resolve: {
				processo: function() {
					return processo;
				}
			}
		});

	};
};

exports.services.ProcessoService = ProcessoService;