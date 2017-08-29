var AnaliseJuridicaService = function (request, config) {

    this.getAnaliseJuridica = function (idAnaliseJuridica) {

        return request
            .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica);
    };

    this.getParecerByNumeroProcesso = function (numeroProcesso) {

        return request
            .get(config.BASE_URL() + 'analisesJuridicas/parecer?numeroProcesso=' + numeroProcesso);
    };

    this.iniciar = function (analise) {

        return request
            .post(config.BASE_URL() + 'analisesJuridicas/iniciar', analise);
    };

    this.getDocumentosAnalisados = function (idAnaliseJuridica) {

        return request
            .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica + '/documentosAnalisados');
    };

    this.salvar = function (analise) {

        return request
            .post(config.BASE_URL() + 'analisesJuridicas', analise);
    };

    this.concluir = function (analise) {

        return request
            .post(config.BASE_URL() + 'analisesJuridicas/concluir', analise);
    };

    this.validarParecer = function (analise) {

        return request
            .post(config.BASE_URL() + 'analisesJuridicas/validarParecer', analise);
    };

    this.solicitarAjusteAprovador = function (analise) {

        return request
            .post(config.BASE_URL() + 'analisesJuridicas/validarParecerAprovador', analise);
    };
};

exports.services.AnaliseJuridicaService = AnaliseJuridicaService;