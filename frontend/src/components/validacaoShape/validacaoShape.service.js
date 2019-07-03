/**
 * Service para a validação de shapes
 **/
var ValidacaoShapeService = function(requestService) {

	var validacaoShape = this;

    /** Atribuição das funções **/
	validacaoShape.list = list;
	validacaoShape.uploadShapeFile = uploadShapeFile;
	validacaoShape.abortUploadShapeFile = abortUploadShapeFile;
	validacaoShape.salvarLote = salvarLote;
	validacaoShape.verificaSeLoteFoiProcessado = verificaSeLoteFoiProcessado;
	validacaoShape.buscarLoteDeAreasPorId = buscarLoteDeAreasPorId;
	validacaoShape.listarCondicoes = listarCondicoes;
    validacaoShape.enviarInformacoesLoteADifisc = enviarInformacoesLoteADifisc;
    validacaoShape.listaCodigosDosLotes = listaCodigosDosLotes;
	validacaoShape.buscarLotePeloCodigo = buscarLotePeloCodigo;

	function list(pagina, filtro) {

		var data = {
			filtro: filtro
		};

		return requestService.post('validacaoShape/list/' + pagina, data);
	}

	function uploadShapeFile(arquivo) {

		return requestService.uploadWithBlock(arquivo, 'validacaoShape/upload/shapefile');
	}

	function abortUploadShapeFile() {
		requestService.abortUpload();
	}

	function salvarLote(lote) {
		return requestService.post('validacaoShape/save', {validacaoShape: lote});
	}

	function verificaSeLoteFoiProcessado(idvalidacaoShape) {
		return requestService.get('validacaoShape/foiProcessado/' + idvalidacaoShape);
	}

	function buscarLoteDeAreasPorId(idvalidacaoShape) {
		return requestService.get('validacaoShape/' + idvalidacaoShape);
	}

	function listarCondicoes(){
		return requestService.get('validacaoShape/listarCondicoes');
	}

	function enviarInformacoesLoteADifisc(idLote) {
		return requestService.get('validacaoShape/enviarDifisc/' + idLote);
	}

	function listaCodigosDosLotes(codigoLote) {
		return requestService.get('lote/listaDeCodigos/' + codigoLote);
	}

	function buscarLotePeloCodigo(codigoLote) {
		return requestService.get('lote/buscaPeloCodigo/' + codigoLote);
	}

};

exports.services.ValidacaoShapeService = ValidacaoShapeService;