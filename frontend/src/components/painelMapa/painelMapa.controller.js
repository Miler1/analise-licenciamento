/**
 * Controller para a painel de mapa
 **/
var PainelMapaController = function ($scope) {
	var painelMapa = this;
	painelMapa.map = null;

	// Funções atribuídas
	painelMapa.instanciaMapa = instanciaMapa;

	// Função para receber os parâmetros do pug e iniciar a renderização do mapa
	painelMapa.init = function(id, fullscreen)
	{
		// Recebe o ID via parâmetro da controller pai (por conta dos $emits em escopos diferentes)
		painelMapa.id = id;
		painelMapa.isFullscreen = fullscreen;
		painelMapa.listaGeometriasMapa = [];
		painelMapa.instanciaMapa();
	};

	// Start do mapa
	function instanciaMapa() {
		painelMapa.map = new L.Map(painelMapa.id, {
			zoomControl: true,
			minZoom: 5,
			maxZoom: 16,
			scrollWheelZoom: true
		}).setView([-3, -52.497545], 6);
	
		/* Termos de uso: http://downloads2.esri.com/ArcGISOnline/docs/tou_summary.pdf */
		L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
			attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
		}).addTo(painelMapa.map);
	
		painelMapa.map.on('moveend click', function() {
			if (!painelMapa.map.scrollWheelZoom.enabled()) {
				painelMapa.map.scrollWheelZoom.enable();
			}
		});

		if(painelMapa.isFullscreen){
			painelMapa.map.addControl(new L.Control.Fullscreen({
				position: 'topright',
				title: {
					'false': 'Ativar modo de tela cheia',
					'true': 'Desativar modo de tela cheia'
				}
			}));
		}
		
		window.onscroll = function () {
			if (painelMapa.map.scrollWheelZoom.enabled()) {
				painelMapa.map.scrollWheelZoom.disable();
			}
		};
	}

	// Função para atualizar o mapa
	function atualizarMapa(event, shape) {
		painelMapa.listaGeometriasMapa[shape.tipo] = L.geoJSON(shape.geometria, shape.estilo);
		painelMapa.map.addLayer(painelMapa.listaGeometriasMapa[shape.tipo].bindPopup(shape.popupText));
		centralizarGeometrias();
	}
	$scope.$on('mapa:inserirGeometria', atualizarMapa);

	function removerGeometriaMapa(event, shape) {
		painelMapa.map.removeLayer(painelMapa.listaGeometriasMapa[shape.tipo]);
		delete painelMapa.listaGeometriasMapa[shape.tipo];
		centralizarGeometrias();
	}
	$scope.$on('mapa:removerGeometriaMapa', removerGeometriaMapa);

	function centralizarGeometrias() {
		var latLngBounds = new L.latLngBounds();

		Object.keys(painelMapa.listaGeometriasMapa).forEach(function(index){
			latLngBounds.extend(painelMapa.listaGeometriasMapa[index].getBounds());
		});
		
		painelMapa.map.fitBounds(latLngBounds);
	}

};

exports.controllers.PainelMapaController = PainelMapaController;
