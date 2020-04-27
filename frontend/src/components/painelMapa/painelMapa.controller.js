/**
 * Controller para a painel de mapa
 **/
var PainelMapaController = function ($scope, wmsTileService) {

	var painelMapa = this;
	painelMapa.map = null;
	painelMapa.cluster = null;
	painelMapa.markers = [];
	painelMapa.numPoints = 0;
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
		painelMapa.listaGeometriasBase = {};
		painelMapa.listaWmsLayers = [];
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

		painelMapa.cluster = L.markerClusterGroup({

			disableClusteringAtZoom: 15,
			removeOutsideVisibleBounds: true,
			zoomToBoundsOnClick: true,
			spiderfyOnMaxZoom: false,
			showCoverageOnHover: false

		});

		painelMapa.map.on('moveend click', function() {
			if (!painelMapa.map.scrollWheelZoom.enabled()) {
				painelMapa.map.scrollWheelZoom.enable();
			}
		});

		painelMapa.map.doubleClickZoom.disable();

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

    $scope.$on('mapa:centralizar-camada', centralizarCamadaEspecifica);

	$scope.$on('mapa:adicionar-wmslayer-mapa', adicionarWmsLayer);

	$scope.$on('mapa:controla-exibicao-wmslayer', controlaExibicaoWmsLayer);

	function controlaExibicaoWmsLayer(event, camada) {

		var wmslayer = painelMapa.listaWmsLayers[camada.tipo];

		if(camada.visivel){

			camada.visivel = false;
			painelMapa.map.removeLayer(wmslayer.layer);

		} else {
			camada.visivel = true;
			painelMapa.map.addLayer(wmslayer.layer);
		}
	}

	function adicionarWmsLayer(event, camada, naoExibirCamadas) {

		if (painelMapa.listaWmsLayers.length === 0) {

			var styles = camada.tipo === "TERRA_INDIGENA" || camada.tipo === "TERRA_INDIGENA_ZA" ? "terra_indigena_filled" : null;

			var wmsLayer = wmsTileService.novoTile(null, camada.nomeLayer, 5, 20, null, styles, null);

			camada.layer = wmsLayer;
			camada.legend = wmsLayer.legend;

			painelMapa.listaWmsLayers[camada.tipo] = camada;

			if(!naoExibirCamadas) {
				painelMapa.map.addLayer(wmsLayer);
			}
		}
	}

	function centralizarCamadaEspecifica(event, geometria) {

		var layer = L.geoJSON(JSON.parse(geometria));

		if(layer.getLatLng) {

			painelMapa.map.flyTo(layer.getLatLng(), 17);

		} else {

			painelMapa.map.flyToBounds(layer.getBounds(), { maxZoom: 17 });

		}
	}

	function adicionarBotaoCentralizar () {

		L.easyButton('fa-crosshairs fa-lg', function () {
			var maxZoom = 17;
			centralizaGeometriasBase(maxZoom);
		}, 'Centralizar no imóvel').addTo(painelMapa.map);
	}

	function adicionarPointCluster(point) {

		painelMapa.markers.push(point);
		painelMapa.cluster.addLayer(point);

		if(painelMapa.numPoints > 0 && painelMapa.markers.length === painelMapa.numPoints) {

			painelMapa.map.addLayer(painelMapa.cluster);

		}

	}

	function criarPopup(shape) {

		if(shape.tipo.toUpperCase() === 'EMP-CIDADE') {

			return shape.popupText;

		}

		var coteudoModal = preparaModalComDadosDaSobreposicao(shape);

		if(shape.geometria && shape.geometria.type.toLowerCase() === 'point') {
			return coteudoModal + '<b>Coordenadas:</b> [' + shape.geometria.coordinates[0] + ', ' + shape.geometria.coordinates[1] + ']</p>';
		} else {
			return coteudoModal + '<b>Área:</b> ' + shape.area.toFixed(2) + ' ha</p>';
		}

	}

	function preparaModalComDadosDaSobreposicao(shape) {

		var coteudoModal = '<p style="text-align:center;"><b>' + shape.nomeAreaSobreposicao + '</b><br> <hr>';

		if(shape.cpfCnpjAreaSobreposicao) {

			coteudoModal = coteudoModal +
				'<b>CPF/CNPJ:</b> ' + shape.cpfCnpjAreaSobreposicao + '</p>';

		} else if(shape.nomeAreaSobreposicao) {
			coteudoModal = coteudoModal + '<b>Nome:</b> ' + shape.nomeAreaSobreposicao + '</p>';
		}

		if(shape.dataAreaSobreposicao) {
			coteudoModal = coteudoModal + '<b>Data:</b> ' + shape.dataAreaSobreposicao + '</p>';
		}

		return coteudoModal;

	}

	/** Adiciona geometrias base no mapa (que o usuário não fez upload por exemplo) **/
	function adicionarGeometriasBase(event, shape){

		if(painelMapa.listaGeometriasBase[shape.tipo] === undefined) {

			painelMapa.listaGeometriasBase[shape.tipo] = {};

		}

		var item = shape.item || 'item';

		if(shape.geometria && shape.geometria.type.toLowerCase() === 'point') {

			painelMapa.listaGeometriasBase[shape.tipo][item] = L.marker(shape.geometria.coordinates.reverse());

			if(shape.popupText){

				painelMapa.listaGeometriasBase[shape.tipo][item].bindPopup(criarPopup(shape));

			}

			adicionarPointCluster(painelMapa.listaGeometriasBase[shape.tipo][item]);

		} else {

			var estilo = shape.estilo;

			if(shape.geometria && shape.geometria.type.toLowerCase() === 'linestring') {

				estilo = {
					style: {
						color: "#ffd700",
						weight: 2
					}
				};

			}

			painelMapa.listaGeometriasBase[shape.tipo][item] = L.geoJSON(shape.geometria, estilo);

			if(shape.popupText){

				painelMapa.listaGeometriasBase[shape.tipo][item].bindPopup(criarPopup(shape));

			}

			painelMapa.map.addLayer(painelMapa.listaGeometriasBase[shape.tipo][item]);

		}

	}

	function adicionarGeometriasBaseCluster(event, point) {

		painelMapa.cluster.addLayer(painelMapa.listaGeometriasBase[point.tipo][point.item]);

	}

	$scope.$on('mapa:adicionar-geometria-base', adicionarGeometriasBase);

	$scope.$on('mapa:adicionar-geometria-base-cluster', adicionarGeometriasBaseCluster);

	/** Centraliza geometrias base de um mapa **/
	function centralizaGeometriasBase(event, maxZoom) {

		if (maxZoom) {
			painelMapa.map.fitBounds(painelMapa.listaGeometriasBase.PROPRIEDADE.Propriedade.getBounds(), {maxZoom: maxZoom});
		} else {
			painelMapa.map.fitBounds(painelMapa.listaGeometriasBase.PROPRIEDADE.Propriedade.getBounds());
		}

	}

	// Função para atualizar o mapa
	function atualizarMapa(event, shape) {

		if(painelMapa.listaGeometriasMapa[shape.tipo] === undefined) {

			painelMapa.listaGeometriasMapa[shape.tipo] = {};

		}

		painelMapa.listaGeometriasMapa[shape.tipo].item = L.geoJSON(shape.geometria, shape.estilo);

		if(shape.popupText){
			painelMapa.map.addLayer(painelMapa.listaGeometriasMapa[shape.tipo].item.bindPopup(shape.popupText));
		} else {
			painelMapa.map.addLayer(painelMapa.listaGeometriasMapa[shape.tipo].item);
		}

		if(shape.specificShape){

			painelMapa.specificGeometries.push(shape.tipo);

		}

		centralizarGeometrias(shape.specificShape);
	}
	$scope.$on('mapa:inserirGeometria', atualizarMapa);

	$scope.$on('mapa:centralizar-mapa', centralizaGeometriasBase);

	$scope.$on('mapa:centralizar-geometrias', centralizaGeometrias);

	function centralizaGeometrias(evt, bounds) {

		painelMapa.map.fitBounds(bounds);

	}

	// Função para esconder geometrias nao basicas do mapa
	function esconderGeometriasNaoBaseMapa(geometriasRecuperadas){
		Object.keys(painelMapa.listaGeometriasMapa).forEach(function(index){
			painelMapa.map.removeLayer(painelMapa.listaGeometriasMapa[index].item);
		});

		centralizaGeometriasBase();
	}

	// Função para exibir as geometrias nao basicas do mapa
	function exibeGeometriasNaoBaseMapa(){

		Object.keys(painelMapa.listaGeometriasMapa).forEach(function(index){
			painelMapa.map.addLayer(painelMapa.listaGeometriasMapa[index].item);
		});

		centralizarGeometrias(true);
	}

	function removerGeometriaMapa(event, shape) {

		var item = shape.item || 'item';

		painelMapa.map.removeLayer(painelMapa.listaGeometriasMapa[shape.tipo][item]);

		// Limpeza do elemento da lista de centralização especial
		painelMapa.specificGeometries.forEach(function(index, item) {
			if(index === shape.tipo){
				// Deleta o item da lista
				painelMapa.specificGeometries.splice(item, 1);
			}
		});

		delete painelMapa.listaGeometriasMapa[shape.tipo][item];

		if(Object.keys(painelMapa.listaGeometriasMapa[shape.tipo]).length === 0) {

			delete painelMapa.listaGeometriasMapa[shape.tipo];

		}

		// Caso não haja nenhuma geometria que necessite de centralização especial, volta centralizando tudo
		if(painelMapa.specificGeometries.length > 0){

			centralizarGeometrias(true);

		} else {

			centralizarGeometrias(false);

		}

	}

	function removerGeometriaMapaBase(event, shape) {

		shape.visivel = false;

		painelMapa.map.removeLayer(painelMapa.listaGeometriasBase[shape.tipo][shape.item]);

		// Limpeza do elemento da lista de centralização especial
		painelMapa.specificGeometries.forEach(function(index, item) {

			if(index === shape.tipo) {

				// Deleta o item da lista
				painelMapa.specificGeometries.splice(item,1);

			}

		});

		delete painelMapa.listaGeometriasBase[shape.tipo][shape.item];

	}

	function removerGeometriaMapaBaseCluster(event, shape) {

		shape.visivel = false;
		painelMapa.cluster.removeLayer(painelMapa.listaGeometriasBase[shape.tipo][shape.item]);

	}

	$scope.$on('mapa:removerGeometriaMapa', removerGeometriaMapa);

	$scope.$on('mapa:remover-geometria-base', removerGeometriaMapaBase);

	$scope.$on('mapa:remover-geometria-base-cluster', removerGeometriaMapaBaseCluster);

	/** O parâmetro centralizarEspecifico remete a possibilidade de centralizar apenas
	 * as geometrias relacionadas ao upload, não referente aos dados do empreendimento **/
	function centralizarGeometrias(centralizarEspecifico) {

		var latLngBounds = new L.latLngBounds();

		if(centralizarEspecifico){

			Object.keys(painelMapa.listaGeometriasMapa).forEach(function(tipo){
				painelMapa.specificGeometries.forEach(function(tipoAdicionado) {

					if(tipo === tipoAdicionado){

						Object.keys(painelMapa.listaGeometriasMapa[tipo]).forEach(function(item){

							latLngBounds.extend(painelMapa.listaGeometriasMapa[tipo][item].getBounds());

						});

					}

				});
			});

			if(latLngBounds.isValid()) {

				painelMapa.map.fitBounds(latLngBounds);
			
			} else {

				centralizaGeometriasBase();

			}


		} else {

			centralizaGeometriasBase();

		}

	}

};

exports.controllers.PainelMapaController = PainelMapaController;
