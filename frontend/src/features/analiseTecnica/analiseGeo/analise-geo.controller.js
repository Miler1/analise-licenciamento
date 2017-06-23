var AnaliseGeoController = function($scope, $timeout) {

	var idMapa = 'mapa-restricoes',
	mapa,
	layersRestricoes = $scope.layersRestricoes = {},
	meusDados,
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

	$scope.adicionarRestricao = function(idRestricao){

		window.alert('Restricao: ' + idRestricao + ' idAnalise: ' + $scope.idAnaliseTecnica);

	};

	this.init = function(restricoes, idAnaliseTecnica) {

		$scope.idAnaliseTecnica = idAnaliseTecnica;

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

		});

	};

};

exports.controllers.AnaliseGeoController = AnaliseGeoController;