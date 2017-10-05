var ModalVisualizarLicenca = {
	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function($scope) {

		var ctrl = this;
		ctrl.dateUtil = app.utils.DateUtil;

		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'close'});
		};

		ctrl.suspenderLicenca = function(idLicenca,justificativa, qtdeDiasSuspensao){
			var licenca = {
				id: idLicenca,
			};
			console.log(idLicenca, justificativa,qtdeDiasSuspensao);
			ctrl.dismiss({$value: 'close'});
		};

	},
	templateUrl: 'components/visualizarLicenca/modalVisualizarLicenca.html'
};

exports.directives.ModalVisualizarLicenca = ModalVisualizarLicenca;