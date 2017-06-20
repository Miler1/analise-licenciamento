var AnaliseTecnicaService = function(request, config) {

	this.getRestricoesGeo = function(idAnaliseTecnica) {

		return request
                .get(config.BASE_URL() + 'analisesTecnica/' + idAnaliseTecnica + '/restricoesGeo');
	};

	this.iniciar = function(analise) {

		return request
                .post(config.BASE_URL() + 'analisesTecnicas/iniciar', analise);
	};	
};

exports.services.AnaliseTecnicaService = AnaliseTecnicaService;
