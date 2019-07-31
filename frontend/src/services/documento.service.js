var DocumentoService = function(request, $window, config, Upload) {

    this.upload = function(file) {

        return request.upload(config.BASE_URL() + 'upload/documento', file, Upload);
    };

    this.delete = function(nameFile) {

        return request
            .delete(config.BASE_URL() + "delete/documento/" + nameFile);
    };

	this.download = function(nameFile) {

        $window.open(config.BASE_URL() + "download/documento/" + nameFile, '_blank');
	};
};

exports.services.DocumentoService = DocumentoService;