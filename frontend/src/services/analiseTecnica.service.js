var AnaliseTecnicaService = function(request, config) {

	this.getRestricoesGeo = function(idAnaliseTecnica) {

		return request
                .get(config.BASE_URL() + 'analisesTecnica/' + idAnaliseTecnica + '/restricoesGeo');
	};

};

exports.services.AnaliseTecnicaService = AnaliseTecnicaService;