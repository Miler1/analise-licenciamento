var DocumentoService = function(request, $window, config, Upload) {

    this.upload = function(file) {

        return request.upload(config.BASE_URL() + 'upload/documento', file, Upload);
    };

    this.delete = function(nameFile) {

        return request
            .delete(config.BASE_URL() + "delete/documento/" + nameFile);
    };

    // Repensar no parâmetro do nome, já está sendo buscado no backend diretamente - Refatoração
    this.download = function(key, nome) {

        $window.open(config.BASE_URL() + "download/" + key, '_blank');
        
    };

    this.downloadById = function(id) {

        $window.open(config.BASE_URL() + "documentos/" + id + "/download", '_blank');
        
    };

    this.downloadParecerByIdAnaliseTecnica = function(id) {

        $window.open(config.BASE_URL() + "documentos/" + id + "/downloadParecerTecnico", '_blank');

    };

    this.downloadMinutaByIdAnaliseTecnica = function(id) {

        $window.open(config.BASE_URL() + "documentos/" + id + "/downloadMinutaByIdAnaliseTecnica", '_blank');

    };

    this.downloadRTVByIdAnaliseTecnica = function(idAnalisetecnica) {

        $window.open(config.BASE_URL() + "documentos/" + idAnalisetecnica + "/downloadRTVByIdAnaliseTecnica", '_blank');

    };

    this.downloadNoticacaoByIdAnaliseTecnica = function(idAnalisetecnica) {

        $window.open(config.BASE_URL() + "documentos/" + idAnalisetecnica + "/downloadNotificacaoByIdAnaliseTecnica", '_blank');

    };
    
};

exports.services.DocumentoService = DocumentoService;