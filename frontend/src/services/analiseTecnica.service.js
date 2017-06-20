var AnaliseTecnicaService = function(request, config) {

	this.iniciar = function(analise) {

		return request
                .post(config.BASE_URL() + 'analisesTecnicas/iniciar', analise);
	};	
};

exports.services.AnaliseTecnicaService = AnaliseTecnicaService;