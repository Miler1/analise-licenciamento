var AnaliseManejoService = function(request, config, Upload) {

    this.getById = function(id) {

        return request
            .get(config.BASE_URL() + "analiseTecnicaManejo/" + id);
    };

    this.removeAnexo = function(token) {

        return request
            .delete(config.BASE_URL() + "analiseTecnicaManejo/anexo/" + token);
    };

    this.finalizar = function(id) {

        return request
            .put(config.BASE_URL() + "analiseTecnicaManejo/" + id);
	};

	this.upload = function(file, idAnaliseTecnica) {

        return request.upload(config.BASE_URL() + "upload/" + idAnaliseTecnica + "/imovel/manejo", file, Upload);
	};

	this.atualizaExibicaoPdf = function(analiseTecnicaManejo) {

		return request
		.put(config.BASE_URL() + "analiseTecnicaManejo/atualizaExibicaoPdf", analiseTecnicaManejo);
	}

};

exports.services.AnaliseManejoService = AnaliseManejoService;