var AnaliseGeoService = function(request, $window ,config) {

	this.getRestricoesGeo = function(idAnaliseGeo) {

		// return request
        //         	.get(config.BASE_URL() + 'analisesGeo/' + idAnaliseGeo + '/restricoesGeo');
	};

	this.iniciar = function(analise) {

		return request
                	.post(config.BASE_URL() + 'analisesGeo/iniciar', analise);
	};

	this.iniciarAnaliseCoordenador = function(analise) {

		return request
                	.post(config.BASE_URL() + 'analisesGeo/iniciarAnaliseCoordenador', analise);
	};

	this.getAnaliseGeo = function(idAnaliseGeo) {

		return request
                	.get(config.BASE_URL() + 'analisesGeo/' + idAnaliseGeo);
	};

	this.getPossuiAnexo = function(cpfCnpjEmpreendimento) {

		return request
					.get(config.BASE_URL() + 'analisesGeo/verificaAnexosEmpreendimento/' + cpfCnpjEmpreendimento);
	};

	this.getParecerByNumeroProcesso = function(numeroProcessoClone) {

		return request
                .get(config.BASE_URL() + 'analisesGeo/parecer?numeroProcesso=' + numeroProcessoClone);
	};

	this.salvar = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesGeo', analise);
	};

	this.concluir = function(parecer) {

		return request
				.post(config.BASE_URL() + 'parecer/analistaGeo/concluir', parecer);
	};

	this.validarParecer = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesGeo/validarParecer', analise);
	};

	this.listaComunicadosByIdAnaliseGeo = function(params) {
		return request
			.get(config.BASE_URL() + "analisesGeo/comunicado/listaComunicadosByIdAnaliseGeo/" + params);
	};

	this.getComunicadoByIdAnaliseGeoEmpreendimento = function(idAnaliseGeo, idEmpreendimento) {

		return request
			.get(config.BASE_URL() + "analisesGeo/comunicado/findComunicadoByIdAnaliseGeoEmpreendimento/" + idAnaliseGeo + "/" + idEmpreendimento);

	};

	this.getComunicadoByIdAnaliseGeoAtividade = function(idAnaliseGeo, idAtividade) {

		return request
			.get(config.BASE_URL() + "analisesGeo/comunicado/findComunicadoByIdAnaliseGeoAtividade/" + idAnaliseGeo + "/" + idAtividade);

	};

	this.getComunicadoByIdAnaliseGeoComplexo = function(idAnaliseGeo, idComplexo) {

		return request
			.get(config.BASE_URL() + "analisesGeo/comunicado/findComunicadoByIdAnaliseGeoComplexo/" + idAnaliseGeo + "/" + idComplexo);

	};

	this.validarParecerCoordenador = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesGeo/validarParecerCoordenador', analise);
    };

    this.solicitarAjusteAprovador = function(analise) {

        return request
            .post(config.BASE_URL() + 'analisesGeo/validarParecerAprovador', analise);
	};

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.getDadosProjeto = function(idProcesso) {

		return request
			.post(config.BASE_URL() + 'analisesGeo/processo/buscaDadosProcesso/' + idProcesso);

	};


	this.getAnaliseGeoByAnalise = function(idAnalise) {

		return request
			.get(config.BASE_URL() + 'analisesGeo/buscaAnaliseGeoByAnalise/' + idAnalise);

	};

	this.findAnalisesGeoByNumeroProcesso = function(numero) {
		return request
			.get(config.BASE_URL() + 'analisesGeo/findAnalisesGeoByNumeroProcesso/' + numero);

	};

	this.findParecerAjustesByAnaliseGeo = function(analiseGeo){
		return request
			.get(config.BASE_URL() + 'analisesGeo/findParecerAjustesByAnaliseGeo',analiseGeo);
	};

	this.getDadosRestricoesProjeto = function(idProcesso) {

		return request
                .get(config.BASE_URL() + 'analisesGeo/restricao/findAllById/' + idProcesso);

	};

	this.getDadosRestricoesEmpreendimento = function(cpfCnpj) {

		return request.get(config.BASE_URL() + 'empreendimento/sobreposicoes/' + cpfCnpj);

	};


};

exports.services.AnaliseGeoService = AnaliseGeoService;
