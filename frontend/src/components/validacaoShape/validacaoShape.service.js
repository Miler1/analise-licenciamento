/**
 * Service para a validação de shapes
 **/
var ValidacaoShapeService = function(request, config, Upload) {

	var validacaoShape = this;

	/** Atribuição das funções **/
	validacaoShape.uploadShapeFile = uploadShapeFile;
	validacaoShape.abortEnvioPublicacao = abortEnvioPublicacao;
	validacaoShape.salvarGeometrias = salvarGeometrias;

	function uploadShapeFile(arquivo, idMunicipio, idEmpreendimento) {

		var data = {
			file: arquivo,
			idMunicipio: idMunicipio,
			idEmpreendimento: idEmpreendimento
		};

		return request.uploadData(config.BASE_URL() + 'shapefile/enviar', data, Upload);
	}

	function abortEnvioPublicacao() {
		request.abortUpload();
	}

	function salvarGeometrias(listaGeometrias, naoTemShapes, cpfCnpjEmpreendimento) {

		var geometrias = { 
			listaGeometrias: listaGeometrias, 
			naoTemShapes: naoTemShapes,
			cpfCnpjEmpreendimento: cpfCnpjEmpreendimento
		};

		return request.post(config.BASE_URL() + 'shapefile/salvar', geometrias);
	}

};

exports.services.ValidacaoShapeService = ValidacaoShapeService;