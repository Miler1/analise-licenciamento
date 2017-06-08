var AvaliarDocumento = {

    bindings: {

        documento: '<',
        identificador: '<',
        parecer: '<'
    },
    controller: function($location) {

        var ctrl = this;

        this.$onInit = function() {

            ctrl.nome = 'documento_' + this.identificador;
        };

        ctrl.setValido = function(validado) {

            if(validado) {

                ctrl.documento.parecer = null;
            }

            ctrl.documento.validado = validado;
        };

    },
    templateUrl: 'components/avaliarDocumento/avaliarDocumento.html'
};

exports.directives.AvaliarDocumento = AvaliarDocumento;