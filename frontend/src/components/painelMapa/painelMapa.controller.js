/**
 * Controller para a painel de mapa
 **/
var PainelMapaController = function ($scope) {
	var painelMapa = this;
	painelMapa.map = null;
	// Lista para conter as geometrias que precisam de atenção especial durante a renderização
	painelMapa.specificGeometries = [];

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

		if(shape.popupText){
			painelMapa.map.addLayer(painelMapa.listaGeometriasMapa[shape.tipo].bindPopup(shape.popupText));
		} else {
			painelMapa.map.addLayer(painelMapa.listaGeometriasMapa[shape.tipo]);
		}

		if(shape.specificShape){
			painelMapa.specificGeometries.push(shape.tipo);
		}
		
		centralizarGeometrias(shape.specificShape);
	}
	$scope.$on('mapa:inserirGeometria', atualizarMapa);

	function removerGeometriaMapa(event, shape) {
		painelMapa.map.removeLayer(painelMapa.listaGeometriasMapa[shape.tipo]);

		// Limpeza do elemento da lista de centralização especial
		painelMapa.specificGeometries.forEach(function(index, item) {
			if(index === shape.tipo){
				// Deleta o item da lista
				painelMapa.specificGeometries.splice(item,1);
			}
		});

		delete painelMapa.listaGeometriasMapa[shape.tipo];
		
		// Caso não haja nenhuma geometria que necessite de centralização especial, volta centralizando tudo
		if(painelMapa.specificGeometries.length > 0){
			centralizarGeometrias(true);
		}else{
			centralizarGeometrias(false);
		}
		
	}
	$scope.$on('mapa:removerGeometriaMapa', removerGeometriaMapa);

	/** O parâmetro centralizarEspecifico remete a possibilidade de centralizar apenas 
	 * as geometrias relacionadas ao upload, não referente aos dados do empreendimento **/
	function centralizarGeometrias(centralizarEspecifico) {
		var latLngBounds = new L.latLngBounds();

		if(centralizarEspecifico){

			Object.keys(painelMapa.listaGeometriasMapa).forEach(function(index){
				painelMapa.specificGeometries.forEach(function(item) {
					if(index === item){
						latLngBounds.extend(painelMapa.listaGeometriasMapa[index].getBounds());
					}
				});
			});

		}else {

			Object.keys(painelMapa.listaGeometriasMapa).forEach(function(index){
				latLngBounds.extend(painelMapa.listaGeometriasMapa[index].getBounds());
			});
			
		}

		painelMapa.map.fitBounds(latLngBounds);
	}

};

exports.controllers.PainelMapaController = PainelMapaController;
