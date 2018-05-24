var DocumentoAnaliseService = function(request, config, $window) {

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.generatePDFParecer = function(parecer) {

		return request
			.post(config.BASE_URL() + 'analiseJuridica/gerarPDF', parecer);
	};

};

exports.services.DocumentoAnaliseService = DocumentoAnaliseService;