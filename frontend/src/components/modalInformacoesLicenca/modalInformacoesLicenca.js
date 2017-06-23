var ModalInformacoesLicenca = {

	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function() {

		var ctrl = this;

		ctrl.$onInit =  function() {

			ctrl.condicionantes = ctrl.resolve.condicionantes;
			ctrl.recomendacoes = ctrl.resolve.recomendacoes;
		};

		ctrl.confirmarDadosLicenca = function() {

			ctrl.close({$value: ctrl.condicionantes});
		};

		ctrl.cancelar = function() {

			ctrl.dismiss({$value: 'cancel'});
		};
	},
	templateUrl: 'components/modalInformacoesLicenca/modalInformacoesLicenca.html'
};

exports.directives.ModalInformacoesLicenca = ModalInformacoesLicenca;