var VisualizacaoProcessoController = function ($location, $anchorScroll, $timeout, $uibModalInstance, processo, $scope, processoService, mensagem, municipioService, documentoLicenciamentoService, imovelService) {

	var modalCtrl = this;

	modalCtrl.processo = processo;

	modalCtrl.baixarDocumento = baixarDocumento;

	modalCtrl.abreDocumentacao = true;

	modalCtrl.exibirDocumentacao = !modalCtrl.abreDocumentacao;

	var estiloPoligono = {
		color: 'tomato',
		opacity: 1,
		weight: 3,
		fillOpacity: 0.2
	};

	processoService.getInfoProcesso(processo.idProcesso)
		.then(function(response){

			modalCtrl.dadosProcesso = response.data;
			modalCtrl.limite = modalCtrl.dadosProcesso.empreendimento.imovel ? modalCtrl.dadosProcesso.empreendimento.imovel.limite : modalCtrl.dadosProcesso.empreendimento.municipio.limite;
			modalCtrl.inicializarMapa();

		})
		.catch(function(){
			mensagem.error("Ocorreu um erro ao buscar dados do processo.");
		});

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};


	function baixarDocumento(idDocumento) {

		documentoLicenciamentoService.download(idDocumento);
	}


	// Métodos referentes ao Mapa da caracterização
	this.inicializarMapa = function() {

		modalCtrl.map = new L.Map('mapa-processo', {
			zoomControl: false,
			minZoom: 5,
			maxZoom: 16,
			scrollWheelZoom: false
		}).setView([-3, -52.497545], 6);

		/* Termos de uso: http://downloads2.esri.com/ArcGISOnline/docs/tou_summary.pdf */
		L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
			attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
		}).addTo(modalCtrl.map);

		modalCtrl.map.on('moveend click', function() {

			if (!modalCtrl.map.scrollWheelZoom.enabled()) {

				modalCtrl.map.scrollWheelZoom.enable();

			}

		});

		window.onscroll = function () {

			if (modalCtrl.map.scrollWheelZoom.enabled()) {

				modalCtrl.map.scrollWheelZoom.disable();

			}

		};

		modalCtrl.atualizarMapa();
	};

	$(document).on( 'shown.bs.tab', 'a[data-toggle="tab"]', function (e) {

		var target = $(e.target).attr("data-target");

		if(target === '#tabCaracterizacao') {

			modalCtrl.resize();
		}
	});

	this.resize = function(){

		if(modalCtrl.map) {

			modalCtrl.map._onResize();

			modalCtrl.centralizarGeometria();

		}

	};


	this.atualizarMapa = function() {

		modalCtrl.inserirGeometria(modalCtrl.limite);

		modalCtrl.inserirEmpreendimento(modalCtrl.dadosProcesso.empreendimento.coordenadas);

		modalCtrl.map._onResize();
	};

	this.inserirGeometria = function(geo) {

		if(geo && modalCtrl.map) {

			var objectGeo = JSON.parse(geo);

			modalCtrl.areaGeometria = L.geoJson({
				'type': 'Feature',
				'geometry': objectGeo
			}, estiloPoligono);


			modalCtrl.map.addLayer(modalCtrl.areaGeometria);

		}

	};

	this.centralizarGeometria = function() {

		if(modalCtrl.areaGeometria){
			$timeout(function(){
				modalCtrl.map.fitBounds(modalCtrl.areaGeometria.getBounds());
			}, 200);
		}

	};

	this.inserirEmpreendimento = function(localizacao) {

		if(localizacao && modalCtrl.map) {

			var objectGeo = JSON.parse(localizacao);

			modalCtrl.localizacao = L.geoJson({
				'type': 'Feature',
				'geometry': objectGeo
			});

			modalCtrl.map.addLayer(modalCtrl.localizacao);

		}

	};

	this.visualizarFichaImovel = function() {

		modalCtrl.abreDocumentacao = false;
		modalCtrl.exibirDocumentacao = !modalCtrl.abreDocumentacao;

		$location.hash('ficha');

		$anchorScroll();

	};

};

exports.controllers.VisualizacaoProcessoController = VisualizacaoProcessoController;
