var ModalParecerDocumento = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function() {

        var ctrl = this;

        ctrl.$onInit =  function() {

            ctrl.parecer = ctrl.resolve.parecer;
            ctrl.nomeDocumento = ctrl.resolve.nomeDocumento;
        };

        ctrl.confirmarParecer = function() {

            ctrl.close({$value: ctrl.parecer});
        };

        ctrl.cancelar = function() {

            ctrl.dismiss({$value: 'cancel'});
        };
    },
    templateUrl: 'components/modalParecerDocumento/modalParecerDocumento.html'
};

exports.directives.ModalParecerDocumento = ModalParecerDocumento;