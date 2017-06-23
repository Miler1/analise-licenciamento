var ModalParecerRestricaoController = function ($uibModalInstance, $scope, restricao, empreendimentoGeo, imovel) {

	var modalCtrl = this;
	modalCtrl.restricao = restricao;
	modalCtrl.empreendimentoGeo = empreendimentoGeo;
	modalCtrl.imovel = imovel;

	var map;

	$uibModalInstance.rendered.then(function(){

		modalCtrl.inicializarMapa();

	});

	modalCtrl.tratarDistancia = function(distancia) {
		
		var result;

		if(distancia > 1000) {
			var km = distancia / 1000;
			result = km.toFixed(1) + " km";
		} else {
			result = distancia.toFixed(1) + " m";
		}

		return result;
	};

	modalCtrl.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

	modalCtrl.inicializarMapa = function() {

		map = L.map('mapa-parecer-restricao',{
			layers: [L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
				maxZoom: 19,
				attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
			})],
			zoomControl: false,
			scrollWheelZoom: false
		});

		modalCtrl.dados = L.layerGroup().addTo(map);

		if(modalCtrl.imovel)
			modalCtrl.dados.addLayer(modalCtrl.imovel);
		modalCtrl.dados.addLayer(L.geoJSON(modalCtrl.empreendimentoGeo.feature).getLayers()[0]);
		modalCtrl.dados.addLayer(modalCtrl.restricao);

		map.fitBounds(L.geoJSON(modalCtrl.dados.toGeoJSON()).getBounds());

		window.onscroll = function () {

			if (map.scrollWheelZoom.enabled()) {

				map.scrollWheelZoom.disable();

			}

		};

	};

};

exports.controllers.ModalParecerRestricaoController = ModalParecerRestricaoController;