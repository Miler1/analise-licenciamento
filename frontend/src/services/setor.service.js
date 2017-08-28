var SetorService = function(request, config) {

	this.getSetoresByNivel = function(nivel) {

		return request
			.get(config.BASE_URL() + "setores/nivel/" + nivel);
	};

	this.getSetoresPorTipo = function(tipoSetor) {

		return request
			.get(config.BASE_URL() + "setores/setoresPorTipo?tipoSetor=" + tipoSetor);
	};	
};

exports.services.SetorService = SetorService;