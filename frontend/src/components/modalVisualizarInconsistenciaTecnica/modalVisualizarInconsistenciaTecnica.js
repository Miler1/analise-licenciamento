var ModalVisualizarLicenca = {
	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function() {

        var ctrl = this;
		
		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'cancel'});
		};


	},
	templateUrl: 'components/modalVisualizarInconsistenciaTecnica/modalVisualizarInconsistenciaTecnica.html'
};

exports.directives.ModalVisualizarLicenca = ModalVisualizarLicenca;