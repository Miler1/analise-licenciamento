var DocumentoService = function(request, $window, config, Upload) {

    this.upload = function(file) {

        return request.upload(config.BASE_URL() + 'upload/documento', file, Upload);
    };

    this.delete = function(nameFile) {

        return request
            .delete(config.BASE_URL() + "delete/documento/" + nameFile);
    };

	this.download = function(key,nome) {

        $window.open(config.BASE_URL() + "download/"+key+"/documento/"+nome, '_blank');
    };
    
};

exports.services.DocumentoService = DocumentoService;