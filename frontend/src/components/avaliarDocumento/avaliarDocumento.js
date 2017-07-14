var AvaliarDocumento = {

    bindings: {
        
        analiseDocumento: '<',
        identificador: '<',
        invalidar: '<',
        habilitado: '<',
        mensagemNaoHabilitado: '@'
    },
    controller: function() {

        var ctrl = this;

        this.$onInit = function() {

            ctrl.nome = 'documento_' + this.identificador;            
        };

        ctrl.setValido = function(validado) {

            if(!ctrl.habilitado) {
                return;
            }

            if(validado) {

                ctrl.analiseDocumento.parecer = undefined;
            
            } else {

                ctrl.invalidar(ctrl.identificador);
            }

            ctrl.analiseDocumento.validado = validado;
        };
    },
    templateUrl: 'components/avaliarDocumento/avaliarDocumento.html'
};

exports.directives.AvaliarDocumento = AvaliarDocumento;