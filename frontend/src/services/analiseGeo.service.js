var AnaliseGeoService = function(request, config) {

	this.getRestricoesGeo = function(idAnaliseTecnica) {

		return request
                	.get(config.BASE_URL() + 'analisesTecnicas/' + idAnaliseTecnica + '/restricoesGeo');
	};

	this.iniciar = function(analise) {

		return request
                	.post(config.BASE_URL() + 'analisesTecnicas/iniciar', analise);
	};	

	this.getAnaliseGeo = function(idAnaliseGeo) {

		return request
                	.get(config.BASE_URL() + 'analisesGeo/' + idAnaliseGeo);
	};

	this.getParecerByNumeroProcesso = function(numeroProcesso) {

		return request
                .get(config.BASE_URL() + 'analisesTecnicas/parecer?numeroProcesso=' + numeroProcesso);
	};	

	this.salvar = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesTecnicas', analise);
	};

	this.concluir = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesTecnicas/concluir', analise);
	};	

	this.validarParecer = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesTecnicas/validarParecer', analise);
	};

	this.validarParecerGerente = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesTecnicas/validarParecerGerente', analise);
    };

    this.solicitarAjusteAprovador = function(analise) {

        return request
            .post(config.BASE_URL() + 'analisesTecnicas/validarParecerAprovador', analise);
    };
};

exports.services.AnaliseGeoService = AnaliseGeoService;
