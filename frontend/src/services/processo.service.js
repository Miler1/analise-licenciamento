var ProcessoService = function(request, config) {

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
};

exports.services.ProcessoService = ProcessoService;