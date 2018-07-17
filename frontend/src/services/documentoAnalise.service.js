var DocumentoAnaliseService = function(request, config, $window) {

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.generatePDFParecerJuridico = function(analiseJuridica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesJuridicas/downloadPDFParecer', analiseJuridica);
	};

	this.generatePDFParecerTecnico = function(analiseTecnica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesTecnicas/downloadPDFParecer', analiseTecnica);
	};

	this.generatePDFNotificacaoParecerTecnico = function(analiseTecnica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesTecnicas/downloadPDFNotificacao', analiseTecnica);
	};
};

exports.services.DocumentoAnaliseService = DocumentoAnaliseService;