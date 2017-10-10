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

		ctrl.cancelarLicenca = function(){
			
			ctrl.formCancelamentoLicenca.$setSubmitted();

			if(!ctrl.formCancelamentoLicenca.$valid) {
				
				mensagem.error('Por favor, preencha a justificativa para o cancelamento da licença.', {referenceId: 4});
				return;
			}

			var cancelamento = null;

			if (ctrl.resolve.dadosLicenca.tipoLicenca != "DLA"){

				cancelamento = {
					licenca: {
						id: ctrl.resolve.dadosLicenca.id
					},
					justificativa: ctrl.justificativaCancelamento
				};

				licencaEmitidaService.cancelarLicenca(cancelamento)
					.then(function(response) {

						mensagem.success(response.data.texto, {referenceId: 0});
						ctrl.dismiss({$value: 'close'});
					}, function(error) {

						mensagem.error(error.data.texto, {referenceId: 4});
					}
				);

			} else {

				cancelamento = {
					dispensaLicenciamento: {
						id: ctrl.resolve.dadosLicenca.id
					},
					justificativa: ctrl.justificativaCancelamento
				};

			licencaEmitidaService.cancelarDLA(cancelamento)
				.then(function(response) {

					mensagem.success(response.data.texto, {referenceId: 0});
					ctrl.dismiss({$value: 'close'});
				}, function(error) {

					mensagem.error(error.data.texto, {referenceId: 4});
				}
			);

			}
		};

	},
	templateUrl: 'components/visualizarLicenca/modalVisualizarLicenca.html'
};

exports.directives.ModalVisualizarLicenca = ModalVisualizarLicenca;