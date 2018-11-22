var AnaliseGeoManejoController = function($rootScope, $scope, $routeParams, processoManejoService, documentoShapeService, $location, mensagem) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var TAMANHO_MAXIMO_ARQUIVO_MB = 10;

	var analiseGeoManejo = this;
	analiseGeoManejo.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
	analiseGeoManejo.tipos = ['application/x-rar-compressed','application/zip','application/x-zip-compressed','multipart/x-zip', 'application/vnd.rar'];
	analiseGeoManejo.processo = {};
	analiseGeoManejo.arquivoShapeUtil = new app.utils.shapefile(mensagem);
	analiseGeoManejo.geoJsonArcgis = null;

	analiseGeoManejo.idTiposDocumento = {
		SHAPE_PROPRIEDADE_MANEJO: 8,
		SHAPE_AREA_MANEJO: 9,
		SHAPE_MANEJO: 10
	};

	analiseGeoManejo.documentosShape = [
		{titulo: "Área de manejo florestal solicitada - AMF (hectares)", documento: null, codigo: "SHAPE_PROPRIEDADE_MANEJO"},
		{titulo: "Área de preservação permanente - APP", documento: null, codigo: "SHAPE_AREA_MANEJO"},
		{titulo: "Área sem potencial", documento: null, codigo: "SHAPE_MANEJO"}
	];

	analiseGeoManejo.init = function() {

		processoManejoService.getProcesso($routeParams.idProcesso)
			.then(function (response) {

				analiseGeoManejo.processo = response.data;

				if (analiseGeoManejo.processo.nomeCondicao == 'Manejo digital em análise técnica' ) {

					$location.path('/analise-manejo/' + analiseGeoManejo.processo.analiseManejo.id + '/analise-tecnica');
					return;

				// Como ainda não existe a integração com o SIMLAM, esse bloco é necessário para manter a integridade do sistema
				} else if (analiseGeoManejo.processo.nomeCondicao == 'Manejo digital deferido' || analiseGeoManejo.processo.nomeCondicao == 'Manejo digital indeferido') {

					mensagem.warning("Processo já análisado.");
					$location.path('/analise-manejo');
					return;
				}
			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro obter dados do processo.");
			});
	};


	analiseGeoManejo.selecionarArquivo = function (files, file, documentoShape) {

		if (file) {

			if (analiseGeoManejo.tipos.indexOf(file.type) === -1 || file.name.substring(file.name.lastIndexOf('.')) !== '.zip') {

				mensagem.error("Extensão de arquivo inválida.");
				return;
			}

			if ((file.size / Math.pow(1000,2)) > analiseGeoManejo.TAMANHO_MAXIMO_ARQUIVO_MB) {

				mensagem.error("O arquivo deve ter um tamanho menor que " + TAMANHO_MAXIMO_ARQUIVO_MB + " MB.");
				return;
			}

			uploadArquivo(file, documentoShape);
		}
	};

	function uploadArquivo(arquivo, documentoShape) {

		if (!arquivo){

			return;
		}

		documentoShape.documento = {
			key: null,
			nome: null,
			geoJsonArcgis: null
		};

		documentoShapeService.upload(arquivo)
			.then(function(response){

				documentoShape.documento.key = response.data;
				documentoShape.documento.nome = arquivo.name;

				analiseGeoManejo.arquivoShapeUtil.shapefileToGeojson(arquivo, documentoShape, analiseGeoManejo.getGeometria);

				validarAnalise();
			})
			.catch(function(response){

				mensagem.warning(response.data.texto);
				return;
			});
	}

	analiseGeoManejo.removerArquivo = function (documentoShape) {

		documentoShapeService.delete(documentoShape.documento.key)
			.then(function(response){

				documentoShape.documento = null;
				validarAnalise();
			})
			.catch(function(response){

				mensagem.warning(response.data.texto);
				return;
			});
	};


	analiseGeoManejo.analisarShape = function() {

		if(!validarAnalise()) {

			mensagem.error('É necessário fornecer todos shapes obrigatórios.', { ttl: 10000 });
			return;
		}

		var documentosShape = [];

		analiseGeoManejo.documentosShape.forEach(function(item){

			if (item.documento) {

				var documentoShape = {
					geoJsonArcgis: item.documento.geoJsonArcgis,
					key: item.documento.key,
					tipo: { id: analiseGeoManejo.idTiposDocumento[item.codigo] }
				};

				documentosShape.push(documentoShape);
			}
		});

		analiseGeoManejo.processo.analisesTecnicaManejo = [{documentosShape: documentosShape}];

		processoManejoService.inicicarAnaliseShape(analiseGeoManejo.processo)
			.then(function(response) {

				mensagem.success('Analise dos shapes solicitada com sucesso!', { ttl: 10000 });

				$location.path('/analise-manejo');

			}, function(error){

				mensagem.error(error.data.texto);
			});

	};

	analiseGeoManejo.downloadArquivo = function(documentoShape) {

		location.href = documentoShapeService.download(documentoShape.documento.key);
	};


	function validarAnalise() {

		var shapes = {
			SHAPE_PROPRIEDADE_MANEJO: {possui: false, index: null},
			SHAPE_AREA_MANEJO: {possui: false, index: null},
			SHAPE_MANEJO: {possui: false, index: null}
		};

		analiseGeoManejo.documentosShape.forEach(function(item, index){

			// inicialmente todos são considerados obrigários até que escolha uns dos shapes
			analiseGeoManejo.documentosShape[index].obrigatorio = true;

			shapes[item.codigo].index = index;

			if (item.documento) {
				shapes[item.codigo].possui = true;
			}
		});

		if (shapes.SHAPE_AREA_MANEJO.possui) {
			analiseGeoManejo.documentosShape[shapes.SHAPE_MANEJO.index].obrigatorio = false;
		}

		if (!shapes.SHAPE_AREA_MANEJO.possui && shapes.SHAPE_MANEJO.possui) {
			analiseGeoManejo.documentosShape[shapes.SHAPE_AREA_MANEJO.index].obrigatorio = false;
		}

		$scope.formularioAnaliseGeo.$setSubmitted();

		return (
			shapes.SHAPE_PROPRIEDADE_MANEJO.possui &&
			(shapes.SHAPE_AREA_MANEJO.possui || shapes.SHAPE_MANEJO.possui)
		);
	}

	analiseGeoManejo.getGeometria = function (geojson, documentoShape) {

		var geoJsonArcgis = analiseGeoManejo.arquivoShapeUtil.geojsonToArcGIS(geojson);
		documentoShape.documento.geoJsonArcgis = JSON.stringify(geoJsonArcgis);
		$scope.$apply();
	};


	analiseGeoManejo.cancelar = function() {

		$location.path('/analise-manejo');
	};
};

exports.controllers.AnaliseGeoManejoController = AnaliseGeoManejoController;