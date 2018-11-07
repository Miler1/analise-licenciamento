var ProcessoManejoService = function(request, config, Upload, $uibModal) {

	this.salvarProcesso = function(processo) {

		return request
			.post(config.BASE_URL() + "processoManejo", processo);
	};

	this.getProcesso = function(id) {

		return request
			.get(config.BASE_URL() + "processoManejo/" + id);
	};

	this.iniciarAnalise = function(processo) {

		return request
			.post(config.BASE_URL() + "processoManejo/iniciar", processo);
	};

	this.downloadPdfAnaliseTecnica = function(processo) {

		return request.postArrayBuffer(config.BASE_URL() + "processoManejo/downloadPdfAnalise", processo);
	};

	this.getProcessos = function(filtro) {

		return request
			.post(config.BASE_URL() + "processosManejo", filtro);
	};

	this.visualizarProcessoManejo = function(processo) {

		var modalInstance = $uibModal.open({
			controller: 'visualizacaoProcessoManejoController',
			controllerAs: 'modalVisualizacaoProcessoManejoCtrl',
			templateUrl: 'components/visualizacaoProcessoManejo/visualizacaoProcessoManejo.html',
			windowClass: 'modalVisualizarProcessoManejo',
			size: 'lg',
			resolve: {
				processo: function() {
					return processo;
				}
			}
		});

	};

	this.getProcessosCount = function(filtro) {

		return request
		.post(config.BASE_URL() + "processoManejo/count", filtro);
	};

};

exports.services.ProcessoManejoService = ProcessoManejoService;