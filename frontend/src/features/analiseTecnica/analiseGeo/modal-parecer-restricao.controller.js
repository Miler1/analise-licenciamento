var ModalParecerRestricaoController = function ($uibModalInstance, $scope, restricao, restricaoGeo, analiseTecnica, empreendimentoGeo, imovel, mensagem) {

	var modalCtrl = this;
	modalCtrl.restricao = restricao;
	modalCtrl.analiseTecnica = analiseTecnica;
	modalCtrl.restricaoGeo = restricaoGeo;
	modalCtrl.empreendimentoGeo = empreendimentoGeo;
	modalCtrl.imovel = imovel;

	var map;

	$uibModalInstance.rendered.then(function(){

		modalCtrl.inicializarMapa();
		if(modalCtrl.restricao.feature.properties.parecer) {
			modalCtrl.codigoCamada = modalCtrl.restricao.feature.id;
			modalCtrl.parecer = modalCtrl.restricao.feature.properties.parecer;
		}

	});

	modalCtrl.tratarRestricao = function(restricaoProperties) {

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

	modalCtrl.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

	modalCtrl.inicializarMapa = function() {

		map = L.map('mapa-parecer-restricao',{
			layers: [L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
				maxZoom: 19,
				attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
			})],
			zoomControl: false,
			scrollWheelZoom: false
		});

		modalCtrl.dados = L.layerGroup().addTo(map);

		if(modalCtrl.imovel)
			modalCtrl.dados.addLayer(modalCtrl.imovel);
		modalCtrl.dados.addLayer(L.geoJSON(modalCtrl.empreendimentoGeo.feature).getLayers()[0]);
		modalCtrl.dados.addLayer(modalCtrl.restricaoGeo);

		map.fitBounds(L.geoJSON(modalCtrl.dados.toGeoJSON()).getBounds());

		window.onscroll = function () {

			if (map.scrollWheelZoom.enabled()) {

				map.scrollWheelZoom.disable();

			}

		};

	};

	modalCtrl.excluirParecer = function(codigoCamada) {

		_.remove(modalCtrl.analiseTecnica.pareceresTecnicosRestricoes, function(parecer) {
			return codigoCamada === parecer.codigoCamada;
		});

		modalCtrl.parecer = undefined;
		modalCtrl.restricao.feature.properties.parecer = undefined;

		$uibModalInstance.dismiss('cancel');

	};

	modalCtrl.confirmarParecer = function() {

		$scope.formParecerRestricaoGeo.$setSubmitted();

		if(!$scope.formParecerRestricaoGeo.$valid) {
			mensagem.error('Você deve adicionar um parecer para continuar.', {referenceId: 2});
			return;
		}

		modalCtrl.restricao.feature.properties.parecer = modalCtrl.parecer;

		if(!modalCtrl.analiseTecnica.pareceresTecnicosRestricoes || _.isEmpty(modalCtrl.analiseTecnica.pareceresTecnicosRestricoes)) {

			modalCtrl.analiseTecnica.pareceresTecnicosRestricoes = [];
			modalCtrl.analiseTecnica.pareceresTecnicosRestricoes.push({
				codigoCamada: modalCtrl.restricao.feature.id,
				parecer: modalCtrl.parecer
			});

		} else {

			_.find(modalCtrl.analiseTecnica.pareceresTecnicosRestricoes, function(parecer, index) {
				if(parecer.codigoCamada === modalCtrl.restricao.feature.id)
					modalCtrl.analiseTecnica.pareceresTecnicosRestricoes[index].parecer = modalCtrl.parecer;
				else
					modalCtrl.analiseTecnica.pareceresTecnicosRestricoes.push({
						codigoCamada: modalCtrl.restricao.feature.id,
						parecer: modalCtrl.parecer
					});

			});

		}

		$uibModalInstance.dismiss('cancel');

	};

};

exports.controllers.ModalParecerRestricaoController = ModalParecerRestricaoController;