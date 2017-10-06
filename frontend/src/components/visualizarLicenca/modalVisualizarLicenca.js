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

			ctrl.formCancelamentoLicenca.$setSubmitted();

			if(!ctrl.formCancelamentoLicenca.$valid) {
				
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
			
						var cancelamento = {
							licenca: {
								id: ctrl.resolve.dadosLicenca.id
							},
							justificativa: ctrl.justificativaCancelamento
						};
			
						// licencaEmitidaService.suspenderLicenca(suspensao)
						// 	.then(function(response) {
			
						// 		mensagem.success(response.data.texto, {referenceId: 0});
						// 		ctrl.dismiss({$value: 'close'});
						// 	}, function(error) {
			
						// 		mensagem.error(error.data.texto, {referenceId: 4});
						// 	});
					};

	},
	templateUrl: 'components/visualizarLicenca/modalVisualizarLicenca.html'
};

exports.directives.ModalVisualizarLicenca = ModalVisualizarLicenca;