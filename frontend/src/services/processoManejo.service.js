var ProcessoManejoService = function(request, config) {

	this.salvarProcesso = function(processo) {

		return request
			.post(config.BASE_URL() + "processoManejo", processo);
	};
};

exports.services.ProcessoManejoService = ProcessoManejoService;