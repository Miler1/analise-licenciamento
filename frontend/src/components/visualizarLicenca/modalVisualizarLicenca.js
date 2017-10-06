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
				
				mensagem.error('Verifique o preenchimento correto do(s) campo(s) destacado(s)', {referenceId: 4});
				return;
			}

			var suspensao = {
				licenca: {
					id: ctrl.resolve.dadosLicenca.id
				},
				qtdeDiasSuspensao: ctrl.qtdeDiasSuspensao,
				justificativa: ctrl.justificativa
			};

			licencaEmitidaService.suspenderLicenca(suspensao)
				.then(function(response) {

					mensagem.success(response.data.texto, {referenceId: 0});
					ctrl.dismiss({$value: 'close'});
				}, function(error) {

					mensagem.error(error.data.texto, {referenceId: 4});
				});
		};

	},
	templateUrl: 'components/visualizarLicenca/modalVisualizarLicenca.html'
};

exports.directives.ModalVisualizarLicenca = ModalVisualizarLicenca;