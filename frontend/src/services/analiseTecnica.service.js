var AnaliseTecnicaService = function(request, config) {

	this.getRestricoesGeo = function(idAnaliseTecnica) {

		// return request
        //         	.get(config.BASE_URL() + 'analisesTecnicas/' + idAnaliseTecnica + '/restricoesGeo');
	};

	this.iniciar = function(analise) {

		return request
                	.post(config.BASE_URL() + 'analisesTecnicas/iniciar', analise);
	};

	this.iniciarAnaliseTecnicaCoordenador = function(analise) {

		return request
                	.post(config.BASE_URL() + 'analisesTecnicas/iniciarAnaliseTecnicaCoordenador', analise);
	};

	this.findAnalisesTecnicaByNumeroProcesso = function(numero) {

		return request
			.get(config.BASE_URL() + 'analisesTecnicas/findAnalisesTecnicaByNumeroProcesso/' + numero);

	};

	this.getAnaliseTecnica = function(idAnaliseTecnica) {

		return request
                	.get(config.BASE_URL() + 'analisesTecnicas/' + idAnaliseTecnica);
	};

	this.getAnaliseTecnicaByAnalise = function(idAnalise) {

		return request
			.get(config.BASE_URL() + 'analisesTecnicas/buscaAnaliseTecnicaByAnalise/' + idAnalise);

	};

	this.getParecerByNumeroProcesso = function(numeroProcesso) {

		return request
                .get(config.BASE_URL() + 'analisesTecnicas/parecer?numeroProcesso=' + numeroProcesso);
	};

	this.salvar = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesTecnicas', analise);
	};

	this.concluir = function(parecer) {

		return request
				.post(config.BASE_URL() + 'parecer/analistaTecnico/concluir', parecer);
	};

	this.validarParecer = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesTecnicas/validarParecer', analise);
	};

	this.validarParecerCoordenador = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesTecnicas/validarParecerCoordenador', analise);
    };

    this.solicitarAjusteAprovador = function(analise) {

        return request
            .post(config.BASE_URL() + 'analisesTecnicas/validarParecerAprovador', analise);
    };
};

exports.services.AnaliseTecnicaService = AnaliseTecnicaService;
