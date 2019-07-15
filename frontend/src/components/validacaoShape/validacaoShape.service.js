/**
 * Service para a validação de shapes
 **/
var ValidacaoShapeService = function(request, config, Upload) {

	var validacaoShape = this;

	/** Atribuição das funções **/
	validacaoShape.uploadShapeFile = uploadShapeFile;
	validacaoShape.abortEnvioPublicacao = abortEnvioPublicacao;
	validacaoShape.salvarGeometrias = salvarGeometrias;

	function uploadShapeFile(arquivo) {
		return request.upload(config.BASE_URL() + 'shapefile/enviar', arquivo, Upload);
	}

	function abortEnvioPublicacao() {
		request.abortUpload();
	}

	function salvarGeometrias(listaGeometrias) {

		var geometrias = { listaGeometrias: listaGeometrias };

		return request.post(config.BASE_URL() + 'shapefile/salvar', geometrias);
	}

};

exports.services.ValidacaoShapeService = ValidacaoShapeService;