var AnaliseManejoService = function(request, config, Upload) {

    this.getById = function(id) {

        return request
            .get(config.BASE_URL() + "analiseTecnicaManejo/" + id);
    };

    this.saveAnexo = function(id, file) {

        return request.upload(config.BASE_URL() + 'analiseTecnicaManejo/' + id + '/anexo', file, Upload);
    };

    this.removeAnexo = function(token) {

        return request
            .delete(config.BASE_URL() + "analiseTecnicaManejo/anexo/" + token);
    };

    this.finalizar = function(id) {

        return request
            .put(config.BASE_URL() + "analiseTecnicaManejo/" + id);
    };

};

exports.services.AnaliseManejoService = AnaliseManejoService;