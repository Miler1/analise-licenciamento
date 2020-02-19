var ModalVisualizarSolicitacaoLicenca = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function() {
        
        var ctrl = this;
        ctrl.titulo = "PROTOCOLO";

        ctrl.$onInit =  function() {

            ctrl.parecerTecnico = ctrl.resolve.parecerTecnico;
            ctrl.dadosProcesso = ctrl.resolve.dadosProcesso;
            ctrl.analiseTecnica = ctrl.resolve.analiseTecnica;

        };

        ctrl.fechar = function() {
			ctrl.dismiss({$value: 'cancel'});
		};

    },
    templateUrl: 'components/modalVisualizarSolicitacaoLicenca/modalVisualizarSolicitacaoLicenca.html'
};

exports.directives.ModalVisualizarSolicitacaoLicenca = ModalVisualizarSolicitacaoLicenca;