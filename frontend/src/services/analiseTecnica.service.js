var AnaliseTecnicaService = function(request, config) {

	this.getRestricoesGeo = function(idAnaliseTecnica) {

		return request
                	.get(config.BASE_URL() + 'analisesTecnica/' + idAnaliseTecnica + '/restricoesGeo');
	};

	this.iniciar = function(analise) {

		return request
                	.post(config.BASE_URL() + 'analisesTecnicas/iniciar', analise);
	};	

	this.getAnaliseTecnica = function(idAnaliseTecnica) {

		return request
                	.get(config.BASE_URL() + 'analisesTecnicas/' + idAnaliseTecnica);
	};

	this.getParecerByNumeroProcesso = function(numeroProcesso) {

		return request
                .get(config.BASE_URL() + 'analisesTecnicas/parecer?numeroProcesso=' + numeroProcesso);
	};	
};

exports.services.AnaliseTecnicaService = AnaliseTecnicaService;
