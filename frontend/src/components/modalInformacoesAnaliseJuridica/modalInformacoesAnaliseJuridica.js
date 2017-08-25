var ModalInformacoesAnaliseJuridica = {

	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function(mensagem, analiseJuridicaService, $scope, documentoAnaliseService) {

		var ctrl = this;

		var getInfoAnaliseJuridica = function() {

			analiseJuridicaService.getAnaliseJuridica(ctrl.resolve.idAnalise)
				.then(function(response){

				ctrl.analiseJuridica = response.data;

			});
		};

		ctrl.$onInit =  function() {

			getInfoAnaliseJuridica();
		};

		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'cancel'});
		};

	},
	templateUrl: 'components/modalInformacoesAnaliseJuridica/modalInformacoesAnaliseJuridica.html'
};

exports.directives.ModalInformacoesAnaliseJuridica = ModalInformacoesAnaliseJuridica;