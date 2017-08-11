var SetorService = function(request, config) {

	this.getSetoresFilhos = function(idPerfil) {

		return request
			.get(config.BASE_URL() + "setores/setoresFilhos");
	};

	this.getSetoresPorTipo = function(tipoSetor) {

		return request
			.get(config.BASE_URL() + "setores/setoresPorTipo?tipoSetor=" + tipoSetor);
	};	
};

exports.services.SetorService = SetorService;