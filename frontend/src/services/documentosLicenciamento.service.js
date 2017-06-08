var DocumentoLicenciamentoService = function(request, config, $window) {

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentosLicenciamento/' + idDocumento + '/download', '_blank');

	};

};

exports.services.DocumentoLicenciamentoService = DocumentoLicenciamentoService;