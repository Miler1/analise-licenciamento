var EmitirLicenca = {

    bindings: {
        
        analiseLicenca: '<',
        identificador: '<',
        invalidar: '<'
    },
    controller: function() {

        var ctrl = this;

        this.$onInit = function() {

            ctrl.nome = 'analise_' + this.identificador;            
        };

        ctrl.setEmitir = function(emitir) {

            if(!emitir) {

                ctrl.invalidar(ctrl.identificador);            
            }

            ctrl.analiseLicenca.emitir = emitir;
        };
    },
    templateUrl: 'components/emitirLicenca/emitirLicenca.html'
};

exports.directives.EmitirLicenca = EmitirLicenca;