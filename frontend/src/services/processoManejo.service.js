var ProcessoManejoService = function(request, config) {

	this.salvarProcesso = function(processo) {

		return request
			.post(config.BASE_URL() + "processo/manejo", processo);
	};
};

exports.services.ProcessoManejoService = ProcessoManejoService;