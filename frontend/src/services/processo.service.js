var ProcessoService = function(request, config) {

	this.getProcessos = function(filtro) {

		return request
			.post(config.BASE_URL() + "processos", filtro);
	};

	this.getProcessosCount = function(filtro) {

		return request
			.post(config.BASE_URL() + "processos/count", filtro);
	};	
};

exports.services.ProcessoService = ProcessoService;