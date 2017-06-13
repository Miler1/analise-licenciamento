var Mapa = {

	bindings: {
		idMapa: '<',
		limite: '<',
		empreendimento: '<',
		ctrlMapa: '='
	},

	controller: function($timeout) {

		var ctrl = this;

		var estiloPoligono = {
			color: 'tomato',
			opacity: 1,
			weight: 3,
			fillOpacity: 0.2
		};

		this.inicializarMapa = function() {

			ctrl.map = new L.Map(ctrl.idMapa).setView([-3, -52.497545], 6);

			/* Termos de uso: http://downloads2.esri.com/ArcGISOnline/docs/tou_summary.pdf */
			L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
				attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
			}).addTo(ctrl.map);

			$timeout(function(){

				ctrl.inserirGeometria(ctrl.limite);

				ctrl.inserirEmpreendimento(ctrl.empreendimento);

				ctrl.map._onResize();

			}, 2000);

		};

		this.resize = function(){

			if(ctrl.map) {

				ctrl.map._onResize();

			}

		};


		this.$onInit = function(){

			$timeout(ctrl.inicializarMapa);
		};

		this.inserirGeometria = function(geo) {

			if(geo && ctrl.map) {

				var objectGeo = JSON.parse(geo);

				ctrl.areaGeometria = L.geoJson({
					'type': 'Feature',
					'geometry': objectGeo
				}, estiloPoligono);


				ctrl.map.addLayer(ctrl.areaGeometria);

				this.centralizarGeometria();

			}

		};

		this.centralizarGeometria = function() {

			if(ctrl.areaGeometria){
				$timeout(function(){
					ctrl.map.fitBounds(ctrl.areaGeometria.getBounds());
				}, 200);
			}

		};

		this.inserirEmpreendimento = function(localizacao) {

			if(localizacao && ctrl.map) {

				var objectGeo = JSON.parse(localizacao);

				ctrl.localizacao = L.geoJson({
					'type': 'Feature',
					'geometry': objectGeo
				});

				ctrl.map.addLayer(ctrl.localizacao);

			}

		};
	},

	templateUrl: 'components/mapa/mapa.html'
};

exports.directives.Mapa = Mapa;