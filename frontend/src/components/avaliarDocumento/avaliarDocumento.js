var AvaliarDocumento = {

    bindings: {

        documento: '<',
        identificador: '<',
        parecer: '<',
        invalidar: '<'
    },
    controller: function($location) {

        var ctrl = this;

        this.$onInit = function() {

            ctrl.nome = 'documento_' + this.identificador;
        };

        ctrl.setValido = function(validado) {

            if(validado) {

                ctrl.documento.parecer = undefined;
            
            } else {

                ctrl.invalidar(ctrl.identificador);
            }

            ctrl.documento.validado = validado;
        };

    },
    templateUrl: 'components/avaliarDocumento/avaliarDocumento.html'
};

exports.directives.AvaliarDocumento = AvaliarDocumento;