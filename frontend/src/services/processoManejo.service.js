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

	this.getAnalise = function(id) {

		return request
			.get(config.BASE_URL() + "analiseManejo/" + id);
	};

	this.saveAnexo = function(id, file) {

		return Upload.upload({

			url: config.BASE_URL() + 'analiseManejo/' + id + '/anexo',
			data: { file: file }
		});
	};

	this.removeAnexo = function(token) {

		return request
			.delete(config.BASE_URL() + "analiseManejo/anexo/" + token);
	};

};

exports.services.ProcessoManejoService = ProcessoManejoService;