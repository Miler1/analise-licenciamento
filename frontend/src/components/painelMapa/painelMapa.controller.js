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
	painelMapa.esconderGeometriasNaoBaseMapa = esconderGeometriasNaoBaseMapa;
	painelMapa.exibeGeometriasNaoBaseMapa = exibeGeometriasNaoBaseMapa;

	// Função para receber os parâmetros do pug e iniciar a renderização do mapa
	painelMapa.init = function(id, fullscreen, sidebar)
	{
		// Recebe o ID via parâmetro da controller pai (por conta dos $emits em escopos diferentes)
		painelMapa.id = id;
		painelMapa.isFullscreen = fullscreen;
		painelMapa.sidebar = sidebar;
		// Lista de geometrias enviadas pelo usuário via upload (tem prioridade na hierarquia de centralizar)
		painelMapa.listaGeometriasMapa = [];
		// Lista de geometrias base do mapa, são centralizadas somente se a lista de geometrias enviadas estiver vazia
		painelMapa.listaGeometriasBase = [];
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
				position: 'topleft',
				title: {
					'false': 'Ativar modo de tela cheia',
					'true': 'Desativar modo de tela cheia'
				}
			}));
		}

		if (painelMapa.sidebar) {
			painelMapa.adicionaSideBar(painelMapa.map, true);
		}

		adicionarBotaoCentralizar();

		window.onscroll = function () {
			if (painelMapa.map.scrollWheelZoom.enabled()) {
				painelMapa.map.scrollWheelZoom.disable();
			}
		};
	}

	painelMapa.adicionaSideBar = function(mapa, insereSideBarMapa) {
		if (insereSideBarMapa) {
			var sidebar = L.control.sidebar("sidebar", {position: 'right'}).addTo(mapa);
			mapa.sidebar = sidebar;
		}
	};

	$scope.$on('mapa:adicionar-botao-centralizar-mapa-base', adicionarBotaoCentralizar);


	function adicionarBotaoCentralizar () {

		L.easyButton('fa-crosshairs fa-lg', function () {
			var maxZoom = 17;
			centralizaGeometriasBase(maxZoom);
		}, 'Centralizar no imóvel').addTo(painelMapa.map);
	}

	/** Adiciona geometrias base no mapa (que o usuário não fez upload por exemplo) **/
	function adicionarGeometriasBase(event, shape){
		painelMapa.listaGeometriasBase[shape.tipo] = L.geoJSON(shape.geometria, shape.estilo);

		if(shape.popupText){
			painelMapa.map.addLayer(painelMapa.listaGeometriasBase[shape.tipo].bindPopup(shape.popupText));
		} else {
			painelMapa.map.addLayer(painelMapa.listaGeometriasBase[shape.tipo]);
		}

		centralizaGeometriasBase();
	}

	$scope.$on('mapa:adicionar-geometria-base', adicionarGeometriasBase);

	/** Centraliza geometrias base de um mapa **/
	function centralizaGeometriasBase(maxZoom){

		var latLngBounds = new L.latLngBounds();

		Object.keys(painelMapa.listaGeometriasBase).forEach(function(index){
			latLngBounds.extend(painelMapa.listaGeometriasBase[index].getBounds());
		});

		if (maxZoom) {
			painelMapa.map.fitBounds(latLngBounds, {maxZoom: maxZoom});
		} else {
			painelMapa.map.fitBounds(latLngBounds);
		}
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

	// Função para esconder geometrias nao basicas do mapa
	function esconderGeometriasNaoBaseMapa(){
		Object.keys(painelMapa.listaGeometriasMapa).forEach(function(index){
			painelMapa.map.removeLayer(painelMapa.listaGeometriasMapa[index]);
		});
		centralizaGeometriasBase();
	}

	// Função para exibir as geometrias nao basicas do mapa
	function exibeGeometriasNaoBaseMapa(){
		Object.keys(painelMapa.listaGeometriasMapa).forEach(function(index){
			painelMapa.map.addLayer(painelMapa.listaGeometriasMapa[index]);
		});
		centralizarGeometrias(true);
	}

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

	function removerGeometriaMapaBase(event, shape) {

		shape.visivel = false;

		painelMapa.map.removeLayer(painelMapa.listaGeometriasBase[shape.tipo]);

		// Limpeza do elemento da lista de centralização especial
		painelMapa.specificGeometries.forEach(function(index, item) {
			if(index === shape.tipo){
				// Deleta o item da lista
				painelMapa.specificGeometries.splice(item,1);
			}
		});

		delete painelMapa.listaGeometriasBase[shape.tipo];

	}
	$scope.$on('mapa:removerGeometriaMapa', removerGeometriaMapa);

	$scope.$on('mapa:remover-geometria-base', removerGeometriaMapaBase);

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
			
			centralizaGeometriasBase();
		}

		painelMapa.map.fitBounds(latLngBounds);
	}

};

exports.controllers.PainelMapaController = PainelMapaController;
