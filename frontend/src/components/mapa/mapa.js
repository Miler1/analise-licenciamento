var Mapa = {

	bindings: {
		idMapa: '<'
	},

	controller: function($timeout) {

		var ctrl = this;

		this.inicializarMapa = function() {

			ctrl.map = new L.Map(ctrl.idMapa).setView([-3, -52.497545], 6);

			/* Termos de uso: http://downloads2.esri.com/ArcGISOnline/docs/tou_summary.pdf */
			L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
				attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
			}).addTo(ctrl.map);


			$timeout(function(){
				ctrl.map._onResize();
			}, 150);
		};


		this.$onInit = function(){

			$timeout(ctrl.inicializarMapa);
		};
	},

	templateUrl: 'components/mapa/mapa.html'
};

exports.directives.Mapa = Mapa;