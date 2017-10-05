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

		ctrl.suspenderLicenca = function(){

			var suspensao = {
				licenca: {
					id: ctrl.resolve.dadosLicenca.id
				},
				qtdeDiasSuspensao: ctrl.qtdeDiasSuspensao,
				justificativa: ctrl.justificativa
			};

			ctrl.dismiss({$value: 'close'});
		};

	},
	templateUrl: 'components/visualizarLicenca/modalVisualizarLicenca.html'
};

exports.directives.ModalVisualizarLicenca = ModalVisualizarLicenca;