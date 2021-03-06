var AnaliseManejoService = function(request, config, Upload, $window) {

    this.getById = function(id) {

        return request
            .get(config.BASE_URL() + "analiseTecnicaManejo/" + id);
    };

    this.removeAnexo = function(id) {

        return request
            .delete(config.BASE_URL() + "delete/manejo/" + id);
    };

    this.finalizar = function(analiseTecnica) {

            return request
            .put(config.BASE_URL() + "analiseTecnicaManejo", analiseTecnica);
    };

    this.upload = function(file, idAnaliseTecnica, idTipoDocumento) {

            return request.upload(config.BASE_URL() + "upload/" + idAnaliseTecnica + "/imovel/manejo/" + idTipoDocumento, file, Upload);
    };

    this.uploadDocumentoComplementar = function(file, idAnaliseTecnica) {

            return request.upload(config.BASE_URL() + "upload/" + idAnaliseTecnica + "/complementar", file, Upload);
    };

    this.downloadDocumento = function(idDocumento) {

            $window.open(config.BASE_URL() + "download/manejo/" + idDocumento);

    };

    this.atualizarDadosPdf = function(analiseTecnica, passo) {

            return request
            .put(config.BASE_URL() + "analiseTecnicaManejo/atributos/" + passo, analiseTecnica);

    };
};

exports.services.AnaliseManejoService = AnaliseManejoService;
