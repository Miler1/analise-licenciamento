var AnaliseGeoManejoController = function($rootScope, $scope, $routeParams, processoManejoService, documentoShapeService, $location, mensagem) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var TAMANHO_MAXIMO_ARQUIVO_MB = 10;

	var analiseGeoManejo = this;
	analiseGeoManejo.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
	analiseGeoManejo.tiposSuportados = ['application/zip','application/x-zip-compressed','multipart/x-zip'];
	analiseGeoManejo.processo = {};
	analiseGeoManejo.arquivoShapeUtil = new app.utils.shapefile(mensagem);

	analiseGeoManejo.arquivosShape = [
		{
			titulo: "Área de manejo florestal solicitada - AMF (hectares)",
			key: null,
			nome: null,
			geoJsonArcgis: null,
			idTipoDocumento: 8,
			obrigatorio: true
		},
		{
			titulo: "Área de preservação permanente - APP",
			key: null,
			nome: null,
			geoJsonArcgis: null,
			idTipoDocumento: 9,
			obrigatorio: true
		},
		{
			titulo: "Área sem potencial",
			key: null,
			nome: null,
			geoJsonArcgis: null,
			idTipoDocumento: 10,
			obrigatorio: false
		}
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


	analiseGeoManejo.selecionarArquivo = function (files, file, arquivoShape) {

		if (file) {

			if (analiseGeoManejo.tiposSuportados.indexOf(file.type) === -1 || file.name.substring(file.name.lastIndexOf('.')) !== '.zip') {

				mensagem.error("Extensão de arquivo inválida.");
				return;
			}

			if ((file.size / Math.pow(1000,2)) > analiseGeoManejo.TAMANHO_MAXIMO_ARQUIVO_MB) {

				mensagem.error("O arquivo deve ter um tamanho menor que " + TAMANHO_MAXIMO_ARQUIVO_MB + " MB.");
				return;
			}

			uploadArquivo(file, arquivoShape);
		}
	};

	function uploadArquivo(file, arquivoShape) {

		if (!file){

			return;
		}

		analiseGeoManejo.arquivoShapeUtil.shapefileToGeojson(file, arquivoShape, analiseGeoManejo.getGeometria);

		documentoShapeService.upload(file)
			.then(function(response){

				arquivoShape.key = response.data;
				arquivoShape.nome = file.name;
			})
			.catch(function(response){

				mensagem.warning(response.data.texto);
				return;
			});
	}

	analiseGeoManejo.removerArquivo = function (arquivoShape) {

		documentoShapeService.delete(arquivoShape.key)
			.then(function(response){

				arquivoShape.key = null;
				arquivoShape.nome = null;
				arquivoShape.geoJsonArcgis = null;
			})
			.catch(function(response){

				mensagem.warning(response.data.texto);
				return;
			});
	};


	analiseGeoManejo.analisarShape = function() {

		if(!validarAnalise()) {

			mensagem.error('É necessário fornecer todos os shapes obrigatórios.', { ttl: 10000 });
			return;
		}

		var arquivosShape = [];

		analiseGeoManejo.arquivosShape.forEach(function(item){

			if (item.key) {

				var arquivoShape = {
					geoJsonArcgis: item.geoJsonArcgis,
					key: item.key,
					tipo: { id: item.idTipoDocumento }
				};

				arquivosShape.push(arquivoShape);
			}
		});

		analiseGeoManejo.processo.analisesTecnicaManejo = [{documentosShape: arquivosShape}];

		processoManejoService.inicicarAnaliseShape(analiseGeoManejo.processo)
			.then(function(response) {

				mensagem.success('Analise dos shapes solicitada com sucesso!', { ttl: 10000 });

				$location.path('/analise-manejo');

			}, function(error){

				mensagem.error(error.data.texto);
			});

	};

	analiseGeoManejo.downloadArquivo = function(arquivoShape) {

		location.href = documentoShapeService.download(arquivoShape.key);
	};


	function validarAnalise() {

		$scope.formularioAnaliseGeo.$setSubmitted();

		var analiseValida = true;

		analiseGeoManejo.arquivosShape.forEach(function(item){

			if (!item.key && item.obrigatorio) {
				analiseValida = false;
			}
		});

		return analiseValida;
	}

	analiseGeoManejo.getGeometria = function (geojson, arquivoShape) {

		var geoJsonArcgis = analiseGeoManejo.arquivoShapeUtil.geojsonToArcGIS(geojson);
		arquivoShape.geoJsonArcgis = JSON.stringify(geoJsonArcgis);
		$scope.$apply();
	};


	analiseGeoManejo.cancelar = function() {

		$location.path('/analise-manejo');
	};
};

exports.controllers.AnaliseGeoManejoController = AnaliseGeoManejoController;