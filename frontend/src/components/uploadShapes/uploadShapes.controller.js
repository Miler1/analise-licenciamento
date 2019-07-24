/**
 * Controller para a tela de upload de shapes
 **/
var UploadShapesController = function ($injector, $scope, $timeout, $location, analiseGeoService, $rootScope, validacaoShapeService, $route, processoService) {

	var uploadShapes = this;

	/** Variáveis para controle de lógica **/
	uploadShapes.shapesUploaded = 0;
	uploadShapes.doesntHasShapes = false;
	uploadShapes.idMunicipio = '';

	uploadShapes.estiloMapa = app.utils.EstiloMapa;

	/** Utiliza o ID do processo salvo na URL para não perder a referência de buscar os dados **/
	uploadShapes.idProcesso = $route.current.params.idProcesso;

	/** Atribuição de funções **/
	uploadShapes.onInit = onInit;
	uploadShapes.enviaShapes = enviaShapes;
	uploadShapes.abrirModal = abrirModal;
	uploadShapes.cancelaEnvio = cancelaEnvio;
	uploadShapes.buscaProcesso = buscaProcesso;
	uploadShapes.hideUploadShapes = hideUploadShapes;
	

	function buscaProcesso() {
		processoService.getInfoProcesso(parseInt(uploadShapes.idProcesso))
			.then(function(response){

				uploadShapes.processo = response.data;

				uploadShapes.idMunicipio = uploadShapes.processo.empreendimento.municipio.id;

				$scope.$emit('mapa:adicionar-geometria-base', {
					geometria: JSON.parse(uploadShapes.processo.empreendimento.coordenadas), 
					tipo: 'EMP-LOCAL',
					estilo: {
						style: {
						}
					},
					popupText: 'Empreendimento',
				});

				$scope.$emit('mapa:adicionar-geometria-base', {
					geometria: JSON.parse(uploadShapes.processo.empreendimento.municipio.limite), 
					tipo: 'EMP-CIDADE',
					estilo: {
						style: {
							fillColor: 'transparent',
							color: '#FFF',
						}
					},
				});
		});
	}

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

		var cpfCnpjEmpreendimento = uploadShapes.processo.empreendimento.pessoa.cpf ? uploadShapes.processo.empreendimento.pessoa.cpf : uploadShapes.processo.empreendimento.pessoa.cnpj;

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

	// Função para preencher o processo em função do ID da URL
	function onInit(){
		$rootScope.$broadcast('atualizarContagemProcessos');
		uploadShapes.buscaProcesso();
	}
	
	uploadShapes.onInit();

	// Invoke  para receber as funções da controller da controller do componente do Mapa
	$injector.invoke(exports.controllers.PainelMapaController, this,
		{
			$scope: $scope,
			$timeout: $timeout,
		}
	);
	uploadShapes.init('mapa', true);

	function hideUploadShapes() {
		if(uploadShapes.doesntHasShapes){
			uploadShapes.esconderGeometriasNaoBaseMapa();
		}else {
			uploadShapes.exibeGeometriasNaoBaseMapa();
		}
	}

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