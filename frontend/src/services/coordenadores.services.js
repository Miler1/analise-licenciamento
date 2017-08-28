var CoordenadorService = function (request, config) {

	this.getCoordenadores = function (idPerfil) {

		return request
			.get(config.BASE_URL() + "coordenadores?idPerfil=" + idPerfil);
	};
};

exports.services.CoordenadorService = CoordenadorService;