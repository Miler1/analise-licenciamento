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

	this.getAnaliseJuridica = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'processos/' + idProcesso + '/analiseJuridica');
	};

	this.getInfoProcesso = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'processos/' + idProcesso + '/completo');
	};


	this.visualizarProcesso = function(processo) {
		$uibModal.open({
			controller: 'visualizacaoProcessoController',
			controllerAs: 'modalVisualizacaoProcessoCtrl',
			templateUrl: 'components/visualizacaoProcesso/visualizacaoProcesso.html',
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