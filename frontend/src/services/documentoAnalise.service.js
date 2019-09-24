var DocumentoAnaliseService = function(request, config, $window) {

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};
	this.generatePDFOficioOrgao = function(comunicado) {

		return request.postArrayBuffer(config.BASE_URL() + "analisesGeo/downloadPDFOficioOrgao/"+ comunicado);
	};


	this.generatePDFParecerJuridico = function(analiseJuridica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesJuridicas/downloadPDFParecer', analiseJuridica);
	};

	this.generatePDFParecerTecnico = function(analiseTecnica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesTecnicas/downloadPDFParecer', analiseTecnica);
	};

	this.generatePDFParecerGeo = function(analiseGeo) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesGeo/downloadPDFParecer', analiseGeo);
	};

	this.generatePDFCartaImagemGeo = function(analiseGeo) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesGeo/downloadPDFCartaImagem', analiseGeo);
	};

	this.generatePDFNotificacaoParecerJuridico = function(analiseJuridica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesJuridicas/downloadPDFNotificacao', analiseJuridica);
	};

	this.generatePDFNotificacaoParecerTecnico = function(analiseTecnica) {

		return request.postArrayBuffer(config.BASE_URL() + 'analisesTecnicas/downloadPDFNotificacao', analiseTecnica);
	};

};

exports.services.DocumentoAnaliseService = DocumentoAnaliseService;
