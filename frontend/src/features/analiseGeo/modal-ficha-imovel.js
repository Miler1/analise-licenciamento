var ModalFichaImovel = {

	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function($scope) {

		var ctrl = this;

		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'close'});
		};

	},
	templateUrl: './features/analiseTecnica/analiseGeo/modal-ficha-imovel.html'
};

exports.directives.ModalFichaImovel = ModalFichaImovel;