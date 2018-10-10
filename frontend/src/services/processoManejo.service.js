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

	this.downloadPdfAnaliseTecnica = function(processo) {

		return request.postArrayBuffer(config.BASE_URL() + "processoManejo/downloadPdfAnalise", processo);
	};

};

exports.services.ProcessoManejoService = ProcessoManejoService;