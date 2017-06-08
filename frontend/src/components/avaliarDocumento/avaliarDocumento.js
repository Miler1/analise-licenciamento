var AvaliarDocumento = {

    bindings: {

        validado: '<',
        identificador: '<',
        parecer: '<'
    },
    controller: function($location) {

        var ctrl = this;

        this.$onInit = function() {

            ctrl.nome = 'documento_' + this.identificador;
        };

        ctrl.setValido = function(valor) {

            ctrl.validado = valor;
        };

    },
    templateUrl: 'components/avaliarDocumento/avaliarDocumento.html'
};

exports.directives.AvaliarDocumento = AvaliarDocumento;