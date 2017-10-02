var ModalVisualizarLicenca = {
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
	templateUrl: 'components/visualizarLicenca/modalVisualizarLicenca.html'
};

exports.directives.ModalVisualizarLicenca = ModalVisualizarLicenca;