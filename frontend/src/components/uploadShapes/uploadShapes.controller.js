/**
 * Controller para a tela de upload de shapes
 **/
var UploadShapesController = function ($injector, $scope, $timeout, $location, $rootScope, validacaoShapeService, $route, processoService, tiposSobreposicaoService) {

	var uploadShapes = this;

	/** Variáveis para controle de lógica **/
	uploadShapes.shapesUploaded = 0;
	uploadShapes.doesntHasShapes = true;
	uploadShapes.idMunicipio = '';
	uploadShapes.idEmpreendimento = '';

	uploadShapes.estiloMapa = app.utils.EstiloMapa;

	/** Utiliza o ID do protocolo salvo na URL para não perder a referência de buscar os dados **/
	uploadShapes.idProcesso = $route.current.params.idProcesso;

	/** Atribuição de funções **/
	uploadShapes.onInit = onInit;
	uploadShapes.enviaShapes = enviaShapes;
	uploadShapes.abrirModal = abrirModal;
	uploadShapes.cancelaEnvio = cancelaEnvio;
	uploadShapes.buscaProcesso = buscaProcesso;
	uploadShapes.hideUploadShapes = hideUploadShapes;
	uploadShapes.dadosProjeto = null;
	uploadShapes.categoria = app.utils.Inconsistencia;
	uploadShapes.verificarUploadFeito = verificarUploadFeito;
	uploadShapes.alterarGeometria = alterarGeometria;

	function buscaProcesso() {

		processoService.getInfoProcesso(parseInt(uploadShapes.idProcesso)).then(function(response){

			uploadShapes.processo = response.data;

			uploadShapes.idMunicipio = uploadShapes.processo.empreendimento.municipio.id;
			uploadShapes.idEmpreendimento = uploadShapes.processo.empreendimento.id;

			$scope.$emit('mapa:adicionar-geometria-base', {
				geometria: JSON.parse(uploadShapes.processo.empreendimento.empreendimentoEU.localizacao.geometria),
				tipo: 'PROPRIEDADE',
				estilo: {
					style: {
					}
				},
				popupText: 'Empreendimento',
				area: uploadShapes.processo.empreendimento.area,
				item: 'Propriedade'
			});

			$scope.$emit('mapa:adicionar-geometria-base', {
				geometria: JSON.parse(uploadShapes.processo.empreendimento.municipio.limite),
				tipo: 'EMP-CIDADE',
				estilo: {
					style: {
						fillColor: 'transparent',
						color: '#FFF',
					}
				}
			});

			if(uploadShapes.processo.empreendimento.possuiShape) {

				uploadShapes.doesntHasShapes = !uploadShapes.processo.empreendimento.possuiShape;
			}

			hideUploadShapes();

			_.forEach(uploadShapes.processo.empreendimentoCamandasGeo, function (empreendimentoCamandaGeo) {
				switch (empreendimentoCamandaGeo.tipoAreaGeometria.codigo) {
					case 'HID' :
						empreendimentoCamandaGeo.cor = '#2196F3';
						break;

					case 'APP' :
						empreendimentoCamandaGeo.cor = '#8BC34A';
						break;

					case 'AA' :
						empreendimentoCamandaGeo.cor = '#CDDC39';
						break;
				}

				$scope.$emit('shapefile:uploaded', {
					geometria: JSON.parse(empreendimentoCamandaGeo.geometria),
					tipo: empreendimentoCamandaGeo.tipoAreaGeometria.codigo,
					estilo: {
						style: {
							fillColor: empreendimentoCamandaGeo.cor,
							color: empreendimentoCamandaGeo.cor,
							fillOpacity: 0.2
						}
					},
					popupText: empreendimentoCamandaGeo.tipoAreaGeometria.nome,
					specificShape: true

				});

			});

			$scope.$emit('mapa:centralizar-mapa');

		});

	}

	function abrirModal() {
		$('#modalEspecificacoesArquivo').modal('show');
	}

	function enviaShapes() {

		var listaGeometrias = [];

		Object.keys(uploadShapes.listaGeometriasMapa).forEach(function(tipo){

			var geometria = {type:'', geometry:''};

			var empreendimentoCamandaGeo = _.find(uploadShapes.processo.empreendimentoCamandasGeo, function(e) {
				return e.tipoAreaGeometria.codigo === tipo;
			});

			if(empreendimentoCamandaGeo) {

				geometria.id = empreendimentoCamandaGeo.id;

			} else {

				geometria.id = null;

			}

			geometria.type = tipo;
			geometria.geometry = JSON.stringify(uploadShapes.listaGeometriasMapa[tipo].item.getLayers()[0].feature.geometry);

			listaGeometrias.push(geometria);

		});

		var cpfCnpjEmpreendimento = uploadShapes.processo.empreendimento.cpfCnpj;

		validacaoShapeService.salvarGeometrias(listaGeometrias, uploadShapes.doesntHasShapes, cpfCnpjEmpreendimento)
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$location.path('/analise-geo/' + uploadShapes.processo.analise.analiseGeo.id.toString());

			}, function(error){
				mensagem.error(error.data.texto);
			});
	}

	function cancelaEnvio() {
		$location.path('/caixa-entrada');
	}

	// Função para preencher o protocolo em função do ID da URL
	function onInit(){
		$rootScope.$broadcast('atualizarContagemProcessos');
		uploadShapes.buscaProcesso();
	}

	function verificarUploadFeito(tipo) {
		if(uploadShapes.processo === undefined || uploadShapes.processo.empreendimentoCamandasGeo === undefined) {
			return false;
		}
		return _.filter(uploadShapes.processo.empreendimentoCamandasGeo, function(e) {return e.tipoAreaGeometria.codigo === tipo;}).length > 0;
	}

	function alterarGeometria(tipo) {

		var index;

		_.forEachRight(uploadShapes.processo.empreendimentoCamandasGeo, function (e) {

			if(e.tipoAreaGeometria.codigo === tipo) {

				$scope.$emit('shapefile:eraseUpload', {geometria: null, tipo: e.tipoAreaGeometria.codigo});

				index = uploadShapes.processo.empreendimentoCamandasGeo.indexOf(e);
				uploadShapes.processo.empreendimentoCamandasGeo.splice(index, 1);
			}
		});
	}

	uploadShapes.onInit();

	// Invoke  para receber as funções da controller da controller do componente do Mapa
	$injector.invoke(exports.controllers.PainelMapaController, this,
		{
			$scope: $scope,
			$timeout: $timeout
		}
	);
	uploadShapes.init('mapa', true, false);

	function hideUploadShapes() {
		if(uploadShapes.doesntHasShapes){
			uploadShapes.esconderGeometriasNaoBaseMapa(uploadShapes.processo.empreendimentoCamandasGeo);
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

	uploadShapes.baixarShapefile = function(idProcesso) {

		processoService.baixarShapefile(idProcesso);

	};

	uploadShapes.baixarShapefileAtividades = function(idProcesso) {

		processoService.baixarShapefileAtividades(idProcesso);

	};

};
exports.controllers.UploadShapesController = UploadShapesController;
