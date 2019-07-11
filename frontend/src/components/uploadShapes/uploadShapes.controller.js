/**
 * Controller para a tela de upload de shapes
 **/
var UploadShapesController = function ($injector, $scope, $timeout) {

	var uploadShapes = this;

	/** Variáveis para controle de lógica **/
	uploadShapes.shapesUploaded = 0;
	uploadShapes.abrirModal = abrirModal;

	/** Atribuição de funções **/
	uploadShapes.enviaShapes = enviaShapes;

	function abrirModal() {
		$('#modalEspecificacoesArquivo').modal('show');
	}

	function enviaShapes() {
		console.log();
	}

	// Invoke  para receber as funções da controller da controller do componente do Mapa
	$injector.invoke(exports.controllers.PainelMapaController, this,
		{
			$scope: $scope,
			$timeout: $timeout,
		}
	);
	uploadShapes.init('id1',true);

	// On para receber o valor do componente de upload
	$scope.$on('shapefile:uploaded', function(event, shape){
		$scope.$emit('mapa:inserirGeometria', shape);
		uploadShapes.shapesUploaded++; //Adiciona no contador
	});

	// On para remover o elemento que foi feito upload, mas cancelado
	$scope.$on('shapefile:eraseUpload', function(event, shape){
		$scope.$emit('mapa:removerGeometriaMapa', shape);
		uploadShapes.shapesUploaded--; //Reduz no contador
	});

};
exports.controllers.UploadShapesController = UploadShapesController;