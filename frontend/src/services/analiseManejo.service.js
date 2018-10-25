var AnaliseManejoService = function(request, config, Upload) {

    this.getById = function(id) {

        return request
            .get(config.BASE_URL() + "analiseManejo/" + id);
    };

    this.saveAnexo = function(id, file) {

        return request.upload(config.BASE_URL() + 'analiseManejo/' + id + '/anexo', file, Upload);
    };

    this.removeAnexo = function(token) {

        return request
            .delete(config.BASE_URL() + "analiseManejo/anexo/" + token);
    };

    this.finalizar = function(id) {

        return request
            .put(config.BASE_URL() + "analiseManejo/" + id);
    };

};

exports.services.AnaliseManejoService = AnaliseManejoService;