var ProcessoService = function(request, config, $uibModal, $window) {

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

	this.getProcessosAnteriores = function(idProcessoAnterior) {

		return request
			.get(config.BASE_URL() + 'processos/' + idProcessoAnterior + '/processosAnteriores');

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

	this.visualizarNotificacao = function(processo) {

		var modalInstance = $uibModal.open({
			controller: 'visualizacaoNotificacaoController',
			controllerAs: 'modalVisualizacaoNotificacaoCtrl',
			templateUrl: 'components/visualizacaoNotificacao/visualizacaoNotificacao.html',
			windowClass: 'modalVisualizarNotificacao',
			size: 'lg',
			resolve: {
				processo: function() {
					return processo;
				}
			}
		});

	};

	this.baixarShapefile = function(idProcesso){

		$window.open(config.BASE_URL() + 'processos/baixarShapefile/' + idProcesso, '_blank');

	};

	this.baixarShapefileAtividades = function(idProcesso){

		$window.open(config.BASE_URL() + 'processos/baixarShapefileAtividades/' + idProcesso, '_blank');

	};
};

exports.services.ProcessoService = ProcessoService;
