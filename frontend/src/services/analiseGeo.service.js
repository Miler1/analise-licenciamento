var AnaliseGeoService = function(request, $window ,config) {

	this.getRestricoesGeo = function(idAnaliseGeo) {

		return request
                	.get(config.BASE_URL() + 'analisesGeo/' + idAnaliseGeo + '/restricoesGeo');
	};

	this.iniciar = function(analise) {

		return request
                	.post(config.BASE_URL() + 'analisesGeo/iniciar', analise);
	};	

	this.getAnaliseGeo = function(idAnaliseGeo) {

		return request
                	.get(config.BASE_URL() + 'analisesGeo/' + idAnaliseGeo);
	};

	this.getParecerByNumeroProcesso = function(numeroProcesso) {

		return request
                .get(config.BASE_URL() + 'analisesGeo/parecer?numeroProcesso=' + numeroProcesso);
	};	

	this.salvar = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesGeo', analise);
	};

	this.concluir = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesGeo/concluir', analise);
	};	

	this.validarParecer = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesGeo/validarParecer', analise);
	};

	this.validarParecerGerente = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesGeo/validarParecerGerente', analise);
    };

    this.solicitarAjusteAprovador = function(analise) {

        return request
            .post(config.BASE_URL() + 'analisesGeo/validarParecerAprovador', analise);
	};
	
	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};
};

exports.services.AnaliseGeoService = AnaliseGeoService;
