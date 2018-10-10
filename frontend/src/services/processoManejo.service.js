var ProcessoManejoService = function(request, config, Upload) {

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

	this.downloadPdfAnaliseTecnica = function(id) {

		return request
			.get(config.BASE_URL() + "processoManejo/downloadPdfAnaliseTecnica/" + id);
	};

};

exports.services.ProcessoManejoService = ProcessoManejoService;