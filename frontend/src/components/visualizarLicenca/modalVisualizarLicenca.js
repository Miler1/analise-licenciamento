var ModalVisualizarLicenca = {
	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function($scope, licencaEmitidaService, mensagem) {

		var ctrl = this;
		ctrl.dateUtil = app.utils.DateUtil;
		
		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'close'});
		};

		ctrl.suspenderLicenca = function(){

			ctrl.formSuspensaoLicenca.$setSubmitted();

			if(!ctrl.formSuspensaoLicenca.$valid) {
				
				mensagem.error('Verifique se o(s) campo(s) destacado(s) está(estão) completamente preenchido(s)', {referenceId: 4});
				return;
			}

			var suspensao = {
				licenca: {
					id: ctrl.resolve.dadosLicenca.id
				},
				qtdeDiasSuspensao: ctrl.qtdeDiasSuspensao,
				justificativa: ctrl.justificativa
			};

			licencaEmitidaService.suspenderLicenca(suspensao);
			ctrl.dismiss({$value: 'close'});
		};

	},
	templateUrl: 'components/visualizarLicenca/modalVisualizarLicenca.html'
};

exports.directives.ModalVisualizarLicenca = ModalVisualizarLicenca;