var AnaliseGeoController = function($injector, $scope, $timeout, $uibModal, analiseGeo, restricoes, idAnaliseGeo, processoService, empreendimentoService) {
	
	var idMapa = 'mapa-restricoes',
	mapa,
	layersRestricoes = $scope.layersRestricoes = {},
	meusDados,
	imovel,
	colors = ['#ef5350', '#EC407A', '#AB47BC', '#7E57C2', '#5C6BC0', '#42A5F5', '#29B6F6', '#26C6DA', '#26A69A',
		'#66BB6A', '#9CCC65', '#D4E157', '#FFEE58', '#FFCA28', '#FFA726', '#FF7043'];

	var ctrl = this;
	ctrl.restricoes = restricoes;
	ctrl.idAnaliseGeo= idAnaliseGeo;
	ctrl.analiseGeo = angular.copy(analiseGeo);
	ctrl.categoria = app.utils.Inconsistencia;
	ctrl.camadas = [];
	ctrl.estiloMapa = app.utils.EstiloMapa;

	var getLayer = function(descricao){

		var layerControle;
		if(['Imóvel Análisado','Empreendimento'].indexOf(descricao) === -1){

			if(!layersRestricoes[descricao]){
				layersRestricoes[descricao] = L.layerGroup();
				layersRestricoes[descricao].addTo(mapa);
				layersRestricoes[descricao].color = colors.pop();
			}

			layerControle = layersRestricoes[descricao];

		}else {

			layerControle = meusDados;

		}



		return layerControle;

	};

	$injector.invoke(exports.controllers.PainelMapaController, this,
		{
			$scope: $scope,
			$timeout: $timeout
		}
	);

	ctrl.init('mapa-localizacao-empreendimento', true, true);

	function piscarFeature(layer, color) {

		setTimeout(function(){

			var pisca = true;

			var interval = setInterval(function () {

				if (layer.setOpacity) {

					layer.setOpacity(pisca ? 0 : 1);

				} else {

					if (pisca) {
						layer.setStyle({
							color: 'red'
						});
					} else {
						layer.setStyle({
							color: color
						});
					}

				}

				pisca = !pisca;

			}, 200);

			setTimeout(function () {

				clearInterval(interval);

			}, 2000);
			mapa.off('moveend', funcaoAnterior);

			}, 350);

	}

	var funcaoAnterior;
	$scope.visualizarLayer = function(layer, color){

		if(!color && layer.options){

			color = layer.options.color;
		}

		funcaoAnterior = piscarFeature.bind(this, layer, color);

		mapa.on('moveend', funcaoAnterior);

		$('html, body').animate({

			scrollTop: $('#' + idMapa).offset().top - 50

		}, 600, function(){

			if(layer.getBounds){

				mapa.flyToBounds(layer.getBounds());

				layer.bringToFront();


			} else {

				mapa.flyTo(layer.getLatLng());

			}


		});

	};

	$scope.mostrarLayer = function(layer){
		layer.escondida = false;

		if(layer.setStyle){

			layer.setStyle({
				opacity: 1,
				fillOpacity: 0.1
			});

		}else{

			layer.setOpacity(1);

		}

	};
	$scope.esconderLayer = function(layer){

		layer.escondida = true;

		if(layer.setStyle){

			layer.setStyle({
				opacity: 0,
				fillOpacity: 0
			});

		}else{

			layer.setOpacity(0);

		}

	};

	$scope.tratarRestricao = function(restricaoProperties) {

		if(restricaoProperties.descricao === "Imóvel Sobreposto") {

			var n =  Math.log(restricaoProperties.restricao) / Math.LN10;
			var numCasas = 2-n;

			if(numCasas < 0)
				numCasas = 0;

			return restricaoProperties.restricao.toFixed(Math.ceil(numCasas)) + " %";

		} else {

			if(restricaoProperties.restricao > 1000) {
				var km = restricaoProperties.restricao / 1000;
				return km.toFixed(1) + " km";
			} else {
				return restricaoProperties.restricao.toFixed(1) + " m";
			}

		}

	};

	$scope.adicionarRestricao = function(restricao){

		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: './features/analiseGeo/analiseGeo/modal-parecer-restricao.html',
			controller: 'modalParecerRestricaoController',
			controllerAs: 'modalCtrl',
			size: 'lg',
			resolve: {
				analiseGeo: function () {
					return $scope.analiseGeo;
				},
				restricao: function () {
					return restricao;
				},
				restricaoGeo: function() {
					return angular.copy(restricao);
				},
				empreendimentoGeo: function () {
					var layerSelecionada;
					_.forEach($scope.meusDados.getLayers(), function(layer) {
						if(layer.feature.id === "meu-empreendimento")
							layerSelecionada = angular.copy(layer);
					});
					return layerSelecionada;
				},
				imovel: function () {
					var layerSelecionada;
					_.forEach($scope.meusDados.getLayers(), function(layer) {
						if(layer.feature.id === "meu-imovel")
							layerSelecionada = angular.copy(layer);
					});
					return layerSelecionada;
				}
			}
		});

		modalInstance.result.then(function(){},function(){});

	};

	var estiloPoligono = {
		color: colors.pop(),
		opacity: 1,
		weight: 3,
		fillOpacity: 0.1
	};

	var inserirGeometriaMunicipio = function(geo, mapa) {

		if(geo && mapa) {

			var objectGeo = JSON.parse(geo);

			var layer = L.geoJson({
				'type': 'Feature',
				'id': 'municipio-empreendimento',
				'geometry': objectGeo
			}, estiloPoligono);

			$scope.municipioGeormetria = layer.getLayers()[0];

		}

	};

	this.visualizarProcesso = function (processo) {

		processo.idProcesso = processo.id;
		processo.numeroProcesso = processo.numero;

		processoService.visualizarProcesso(processo);
    };

	this.controlaExibicaoCamadas = function(camada) {

		if (camada.visivel) {
			$scope.$emit('mapa:remover-geometria-base', camada);
		} else {
			adicionarGeometriaNoMapa(camada);
		}
	};

	function adicionarGeometriaNoMapa (camada) {

		camada.visivel = true;
		camada.color = ctrl.estiloMapa[camada.tipo].color;

		$scope.$emit('mapa:adicionar-geometria-base', {
			geometria: JSON.parse(camada.geometria),
			tipo: camada.tipo,
			estilo: {
				style: ctrl.estiloMapa[camada.tipo]
			},
			popupText: camada.item
		});
	}

	this.init = function() {

		$scope.analiseGeo = analiseGeo;

		_.forEach(restricoes.features, function(restricao) {
			var parecerTecnicoRestricao = _.find($scope.analiseGeo.pareceresTecnicosRestricoes, function(parecerRestricao) {
				return parecerRestricao.codigoCamada === restricao.id;
			});

			if(parecerTecnicoRestricao !== undefined)
				restricao.properties.parecer = parecerTecnicoRestricao.parecer;
		});

		$timeout(function() {

			var cpfCnpjEmpreendimento = $scope.analiseGeo.analise.processo.empreendimento.pessoa.cpf || $scope.analiseGeo.analise.processo.empreendimento.pessoa.cnpj;

			empreendimentoService.getDadosGeoEmpreendimento(cpfCnpjEmpreendimento)
				.then(function(response) {

					ctrl.camadasDadosEmpreendimento = response.data;

					ctrl.camadasDadosEmpreendimento.forEach(function (camada) {
						adicionarGeometriaNoMapa(camada);
					});

				});

		});

	};

	$scope.addInconsistencia = function(categoriaInconsistencia){
			var modalInstance = $uibModal.open({
			controller: 'inconsistenciaController',
			controllerAs: 'modalCtrl',
			templateUrl: 'features/analiseGeo/modalInconsistencia.html',
			size: 'lg',
			resolve: {
				analiseGeo: function () {
					return ctrl.analiseGeo;
				},
				categoriaInconsistencia: function(){
					return categoriaInconsistencia;
				}
			}		
		});

	};
		
};

exports.controllers.AnaliseGeoController = AnaliseGeoController;