var DocumentoAnaliseService = function(request, config, $window) {

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.generatePDFParecer = function(analiseJuridica) {

		return request
			.post(config.BASE_URL() + 'analiseJuridica/gerarPDF', analiseJuridica);
	};

};

exports.services.DocumentoAnaliseService = DocumentoAnaliseService;