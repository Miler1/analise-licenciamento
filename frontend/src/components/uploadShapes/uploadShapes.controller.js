/**
 * Controller para a tela de upload de shapes
 **/
var UploadShapesController = function ($injector, $scope, $timeout, $location, analiseGeoService, $rootScope, validacaoShapeService) {

	var uploadShapes = this;

	/** Variáveis para controle de lógica **/
	uploadShapes.shapesUploaded = 0;
	uploadShapes.doesntHasShapes = false;

	/**  **/
	uploadShapes.processo = $rootScope.processo;

	/** Atribuição de funções **/
	uploadShapes.enviaShapes = enviaShapes;
	uploadShapes.abrirModal = abrirModal;
	uploadShapes.cancelaEnvio = cancelaEnvio;

	function abrirModal() {
		$('#modalEspecificacoesArquivo').modal('show');
	}

	function enviaShapes() {

		var listaGeometrias = [];

		Object.keys(uploadShapes.listaGeometriasMapa).forEach(function(index){
			var geometria = {type:'', geometry:''};

			geometria.type = index;
			geometria.geometry = JSON.stringify(uploadShapes.listaGeometriasMapa[index].getLayers()[0].feature.geometry);
			listaGeometrias.push(geometria);
		});

		var cpfCnpjEmpreendimento = uploadShapes.processo.cpfEmpreendimento ? uploadShapes.processo.cpfEmpreendimento : uploadShapes.processo.cnpjEmpreendimento;

		validacaoShapeService.salvarGeometrias(listaGeometrias, uploadShapes.doesntHasShapes, cpfCnpjEmpreendimento)
			.then(function(response){
				console.log(response);

				// Aqui vai trocar a tramitacao de caixa de entrada pra análise
				var idAnaliseGeo = uploadShapes.processo.idAnaliseGeo;
				analiseGeoService.iniciar({ id : idAnaliseGeo })
					.then(function(response){

						$rootScope.$broadcast('atualizarContagemProcessos');
						$location.path('/analise-geo/' + idAnaliseGeo.toString());
				
				}, function(error){
					mensagem.error(error.data.texto);
				});

			});
	}

	function cancelaEnvio() {

		$location.path('/caixa-entrada');

	}

	// Invoke  para receber as funções da controller da controller do componente do Mapa
	$injector.invoke(exports.controllers.PainelMapaController, this,
		{
			$scope: $scope,
			$timeout: $timeout,
		}
	);
	uploadShapes.init('emptyMap',true);
	uploadShapes.init('mapa', true);

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