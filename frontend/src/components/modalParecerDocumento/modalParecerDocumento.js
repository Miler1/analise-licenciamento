var ModalParecerDocumento = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function($scope, mensagem) {

        var ctrl = this;

        ctrl.$onInit =  function() {

            ctrl.parecer = ctrl.resolve.parecer;
            ctrl.nomeDocumento = ctrl.resolve.nomeDocumento;
        };

        ctrl.confirmarParecer = function() {

            $scope.formularioParecerDocumento.$setSubmitted();

            if(!$scope.formularioParecerDocumento.$valid) {

                mensagem.error('Parecer de documento não validado é obrigatório.');
                return;
            }

            ctrl.close({$value: ctrl.parecer});
        };

        ctrl.cancelar = function() {

            ctrl.dismiss({$value: 'cancel'});
        };
    },
    templateUrl: 'components/modalParecerDocumento/modalParecerDocumento.html'
};

exports.directives.ModalParecerDocumento = ModalParecerDocumento;