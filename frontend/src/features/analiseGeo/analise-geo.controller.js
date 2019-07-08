var AnaliseGeoController = function($scope, $timeout, $uibModal) {

	var idMapa = 'mapa-restricoes',
	mapa,
	layersRestricoes = $scope.layersRestricoes = {},
	meusDados,
	imovel,
	colors = ['#ef5350', '#EC407A', '#AB47BC', '#7E57C2', '#5C6BC0', '#42A5F5', '#29B6F6', '#26C6DA', '#26A69A',
		'#66BB6A', '#9CCC65', '#D4E157', '#FFEE58', '#FFCA28', '#FFA726', '#FF7043'];


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
			templateUrl: './features/analiseTecnica/analiseGeo/modal-parecer-restricao.html',
			controller: 'modalParecerRestricaoController',
			controllerAs: 'modalCtrl',
			size: 'lg',
			resolve: {
				analiseTecnica: function () {
					return $scope.analiseTecnica;
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

	this.visualizarFichaImovel = function () {

        var modalInstance = $uibModal.open({

            component: 'modalFichaImovel',
            size: 'lg',
			resolve: {
				imovel: $scope.analiseTecnica.analise.processo.empreendimento.imovel
			}
        });

		modalInstance.result.then(function(){},function(){});

    };
	this.init = function(restricoes, analiseTecnica) {

		$scope.analiseTecnica = analiseTecnica;

		_.forEach(restricoes.features, function(restricao) {
			var parecerTecnicoRestricao = _.find($scope.analiseTecnica.pareceresTecnicosRestricoes, function(parecerRestricao) {
				return parecerRestricao.codigoCamada === restricao.id;
			});

			if(parecerTecnicoRestricao !== undefined)
				restricao.properties.parecer = parecerTecnicoRestricao.parecer;
		});

		$timeout(function() {

			mapa = L.map(idMapa,{
				layers: [L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
					maxZoom: 19,
					attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
				})],
				maxZoom: 15,
				minZoom: 2
			});
			meusDados = $scope.meusDados = L.layerGroup().addTo(mapa);

			var measure = L.control.measure({
				position: 'topright',
				primaryLengthUnit: 'meters',
				primaryAreaUnit: 'hectares',
				captureZIndex: 10000,
				localization: 'pt_BR'
			});

			mapa.addControl(measure);			

			var restricoesGeoJson = L.geoJSON(restricoes);

			mapa.fitBounds(restricoesGeoJson.getBounds());

			restricoesGeoJson.eachLayer(function(layer){
				var properties = layer.feature.properties;

				var layerControle = getLayer(properties.descricao);

				if(layer.setStyle){

					if(!layerControle.color){
						layer.color = colors.pop();
					}

					layer.setStyle({
						color: layerControle.color || layer.color,
						fillOpacity: 0.1
					});
				}

				layerControle.addLayer(layer);

			});

			if ($scope.analiseTecnica.analise.processo.empreendimento &&
				!$scope.analiseTecnica.analise.processo.empreendimento.imovel &&
				$scope.analiseTecnica.analise.processo.empreendimento.municipio.limite) {

				inserirGeometriaMunicipio($scope.analiseTecnica.analise.processo.empreendimento.municipio.limite, mapa);

				meusDados.addLayer($scope.municipioGeormetria);

				mapa.flyToBounds($scope.municipioGeormetria.getBounds());
			}

		});

	};

};

exports.controllers.AnaliseGeoController = AnaliseGeoController;