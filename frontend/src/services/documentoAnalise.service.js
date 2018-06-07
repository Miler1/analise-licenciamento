var DocumentoAnaliseService = function(request, config, $window) {

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.generatePDFParecer = function(analiseJuridica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesJuridicas/downloadPDFParecer', analiseJuridica);
	};

};

exports.services.DocumentoAnaliseService = DocumentoAnaliseService;