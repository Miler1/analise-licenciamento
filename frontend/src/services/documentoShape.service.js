var DocumentoShapeService = function(request, config, Upload) {

    this.upload = function(file) {

        return request.upload(config.BASE_URL() + 'upload/shape', file, Upload);
    };

    this.delete = function(nameFile) {

        return request
            .delete(config.BASE_URL() + "delete/shape/" + nameFile);
    };
};

exports.services.DocumentoShapeService = DocumentoShapeService;