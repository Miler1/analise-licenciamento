var CoordenadorService = function (request, config) {

	this.getCoordenadores = function (idPerfil, idProcesso) {

		return request
			.get(config.BASE_URL() + "coordenadoresAprovacao?idPerfil=" + idPerfil + "&idProcesso=" + idProcesso);
	};
};

exports.services.CoordenadorService = CoordenadorService;