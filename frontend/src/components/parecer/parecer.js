var Parecer = {

    bindings: {

        analiseTecnica: '<'
    },

    controller: function(tamanhoMaximoArquivoAnaliseMB) {

        var ctrl = this;

        ctrl.DEFERIDO = app.utils.TiposResultadoAnalise.DEFERIDO;
        ctrl.INDEFERIDO = app.utils.TiposResultadoAnalise.INDEFERIDO;
        ctrl.EMITIR_NOTIFICACAO = app.utils.TiposResultadoAnalise.EMITIR_NOTIFICACAO;
        ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
        ctrl.invalidarDocumento = invalidarDocumento;

        ctrl.$onInit = function() {

            setAnaliseTecnica(ctrl.analiseTecnica);
        };
        
        function setAnaliseTecnica(value) {

            ctrl.parecer = value.parecer;
            ctrl.documentos = value.documentos || [];
            ctrl.tipoResultadoAnalise = value.tipoResultadoAnalise || {};
            ctrl.analisesDocumentos = !_.isEmpty(value.analisesDocumentos) ? value.analisesDocumentos : criarAnalisesDocumentos(value.analise.processo.caracterizacoes[0].documentosEnviados);
        }

        function criarAnalisesDocumentos(documentosLicenciamento) {

            return documentosLicenciamento.map(function (documento) {

                return {

                    "validado": undefined,
                    "parecer": undefined,
                    "documento": documento
                };
            });
        }

        function invalidarDocumento(indice) {

            console.log(indice);
        }
    },

    templateUrl: 'components/parecer/parecer.html'
};

exports.directives.Parecer = Parecer;