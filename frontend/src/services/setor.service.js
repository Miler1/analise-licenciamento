var SetorService = function(request, config) {

	this.getSetoresFilhos = function(idPerfil) {

		return request
			.get(config.BASE_URL() + "setores/setoresFilhos");
	};
};

exports.services.SetorService = SetorService;