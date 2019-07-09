/**
 * Service para a validação de shapes
 **/
var ValidacaoShapeService = function(requestService, request, config, Upload) {

	var validacaoShape = this;

	/** Atribuição das funções **/
	// validacaoShape.list = list;
	validacaoShape.uploadShapeFile = uploadShapeFile;
	validacaoShape.abortEnvioPublicacao = abortEnvioPublicacao;
	// validacaoShape.salvarPublicacao = salvarPublicacao;
	// validacaoShape.verificaSePublicacaoFoiProcessado = verificaSePublicacaoFoiProcessado;
	// validacaoShape.buscarPublicacaoDeAreasPorId = buscarPublicacaoDeAreasPorId;
	// validacaoShape.listColunasTabelaPoligono = listColunasTabelaPoligono;

	// function list(pagina, filtro) {

	// 	var data = {
	// 		filtro: filtro
	// 	};

	// 	return requestService.post('admin/publicacao/area/list/' + pagina, data);
	// }

	function uploadShapeFile(arquivo) {

		//return requestService.uploadWithBlock(arquivo, 'shapefile/enviar');
		return request.upload(config.BASE_URL() + 'shapefile/enviar', arquivo, Upload);
	}

	function abortEnvioPublicacao() {
		//requestService.abortUpload();
		request.abortUpload();
	}

	// function salvarPublicacao(publicacao) {
	// 	return requestService.post('admin/publicacao/area/salvar', {validacaoShape: publicacao});
	// }

	// function verificaSePublicacaoFoiProcessado(idvalidacaoShape) {
	// 	return requestService.get('admin/publicacao/area/foiProcessado/' + idvalidacaoShape);
	// }

	// function buscarPublicacaoDeAreasPorId(idvalidacaoShape) {
	// 	return requestService.get('admin/publicacao/area/' + idvalidacaoShape);
	// }

    // function listColunasTabelaPoligono() {
    //     return requestService.get('admin/publicacao/area/colunasTabelaPoligono');
    // }

};

exports.services.ValidacaoShapeService = ValidacaoShapeService;