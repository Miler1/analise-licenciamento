var ModalVisualizarInconsistenciaTecnica = {
	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function() {

		var ctrl = this;
		
		ctrl.$onInit =  function() {

			ctrl.inconsistenciaTecnica = ctrl.resolve.inconsistenciaTecnica;

		};
		
		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'cancel'});
		};


	},
	templateUrl: 'components/modalVisualizarInconsistenciaTecnica/modalVisualizarInconsistenciaTecnica.html'
};

exports.directives.ModalVisualizarInconsistenciaTecnica = ModalVisualizarInconsistenciaTecnica;