var AnaliseGeoManejoController = function($rootScope, $scope, $routeParams, processoManejoService, uploadService, $location, mensagem) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var TAMANHO_MAXIMO_ARQUIVO_MB = 10;

	var analiseGeoManejo = this;
	analiseGeoManejo.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
	analiseGeoManejo.tipos = ['application/x-rar-compressed','application/zip','application/x-zip-compressed','multipart/x-zip', 'application/vnd.rar'];
	analiseGeoManejo.visualizarProcesso = null;
	analiseGeoManejo.formularioAnaliseGeo = null;
	analiseGeoManejo.processo = {};
	analiseGeoManejo.arquivoShapeUtil = new app.utils.shapefile(mensagem);
	analiseGeoManejo.validacaoErro = false;
	analiseGeoManejo.geoJsonArcgis = null;

	analiseGeoManejo.documentosShape = [
		{titulo: "Shape da propriedade", documento: null, codigo: "SHAPE_PROPRIEDADE_MANEJO", obrigatorio: true},
		{titulo: "Shape da área de manejo", documento: null, codigo: "SHAPE_AREA_MANEJO", obrigatorio: false},
		{titulo: "Shape do manejo", documento: null, codigo: "SHAPE_MANEJO", obrigatorio: false}
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

				mensagem.error("O arquivo deve ter um tamanho menor que 10 MB.");
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
			nome: null
		};

		uploadService.uploadShape(arquivo)
			.then(function(response){

				documentoShape.documento.key = response.data;
				documentoShape.documento.nome = arquivo.name;

				mensagem.success('Documento adicionado com sucesso.', {dontScroll: true});
			})
			.catch(function(response){

				mensagem.warning(response.data.texto);
				return;
			});
	}

	analiseGeoManejo.removerArquivo = function (documentoShape) {

		uploadService.deleteShape(documentoShape.documento.key)
			.then(function(response){

				documentoShape.documento = null;
				mensagem.success(response.data.texto);
			})
			.catch(function(response){

				mensagem.warning(response.data.texto);
				return;
			});
	};


	analiseGeoManejo.analisarShape = function() {

		if(!analiseValida()) {

			mensagem.error('O arquivo do shape não foi selecionado', { ttl: 10000 });
			return;
		}

		analiseGeoManejo.processo.analiseManejo = {geoJsonArcgis: analiseGeoManejo.geoJsonArcgis};

		processoManejoService.inicicarAnaliseShape(analiseGeoManejo.processo)
			.then(function(response) {

				mensagem.success('Analise de shape solicitada com sucesso !', { ttl: 10000 });

				$location.path('/analise-manejo');

			}, function(error){

				mensagem.error(error.data.texto);
			});

	};

	function analiseValida() {

		analiseGeoManejo.formularioAnaliseGeo.$setSubmitted();


		return (analiseGeoManejo.geoJsonArcgis);
	}


	// analiseGeoManejo.upload = function (file) {
	//
	// 	if (file && !analiseGeoManejo.validacaoErro) {
	//
	// 		if (!file.$error) {
	//
	// 			analiseGeoManejo.arquivoShapeUtil.shapefileToGeojson(file, analiseGeoManejo.saveGeometria);
	// 		}
	// 	}
	//
	// };

	// analiseGeoManejo.validarArquivo = function (file) {
	//
	// 	// Para funcionar no windows (não é enviado o type quando o arquivo é rar)
	// 	if (file) {
	//
	// 		if (analiseGeoManejo.tipos.indexOf(file.type) === -1 && file.name.substring(file.name.lastIndexOf('.')) !== '.rar') {
	//
	// 			mensagem.error("Extensão de arquivo inválida.");
	// 			analiseGeoManejo.validacaoErro = true;
	// 		}
	//
	// 		if ((file.size / Math.pow(1000,2)) > analiseGeoManejo.TAMANHO_MAXIMO_ARQUIVO_MB) {
	//
	// 			mensagem.error("O arquivo deve ter um tamanho menor que 10 MB.");
	// 			analiseGeoManejo.validacaoErro = true;
	// 		}
	//
	// 	} else {
	// 		analiseGeoManejo.validacaoErro = false;
	// 	}
	// };

	// analiseGeoManejo.saveGeometria = function (geojson) {
	//
	// 	var geoJsonArcgis = analiseGeoManejo.arquivoShapeUtil.geojsonToArcGIS(geojson);
	// 	analiseGeoManejo.geoJsonArcgis = JSON.stringify(geoJsonArcgis);
	// 	$scope.$apply();
	//
	// };

	// analiseGeoManejo.removeGeometria = function () {
	//
	// 	analiseGeoManejo.geoJsonArcgis = null;
	// };

	// analiseGeoManejo.analisarShape = function() {
	//
	// 	if(!analiseValida()) {
	//
	// 		mensagem.error('O arquivo do shape não foi selecionado', { ttl: 10000 });
	// 		return;
	// 	}
	//
	// 	analiseGeoManejo.processo.analiseManejo = {geoJsonArcgis: analiseGeoManejo.geoJsonArcgis};
	//
	// 	processoManejoService.inicicarAnaliseShape(analiseGeoManejo.processo)
	// 		.then(function(response) {
	//
	// 			mensagem.success('Analise de shape solicitada com sucesso !', { ttl: 10000 });
	//
	// 			$location.path('/analise-manejo');
	//
	// 		}, function(error){
	//
	// 			mensagem.error(error.data.texto);
	// 		});
	//
	// };

	analiseGeoManejo.cancelar = function() {

		$location.path('/analise-manejo');
	};

	// function analiseValida() {
	//
	// 	analiseGeoManejo.formularioAnaliseGeo.$setSubmitted();
	// 	return (analiseGeoManejo.geoJsonArcgis);
	// }


};

exports.controllers.AnaliseGeoManejoController = AnaliseGeoManejoController;