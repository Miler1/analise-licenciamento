var DocumentoAnaliseService = function(request, config, $window) {

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.generatePDFParecerJuridico = function(analiseJuridica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesJuridicas/downloadPDFParecerJuridico', analiseJuridica);
	};

	this.generatePDFParecerTecnico = function(analiseJuridica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesJuridicas/downloadPDFParecerTecnico', analiseJuridica);
	};

};

exports.services.DocumentoAnaliseService = DocumentoAnaliseService;