var Parecer = {

    bindings: {

        analiseTecnica: '<'
    },

    controller: function(tamanhoMaximoArquivoAnaliseMB, $uibModal, mensagem) {

        var ctrl = this;

        ctrl.DEFERIDO = app.utils.TiposResultadoAnalise.DEFERIDO;
        ctrl.INDEFERIDO = app.utils.TiposResultadoAnalise.INDEFERIDO;
        ctrl.EMITIR_NOTIFICACAO = app.utils.TiposResultadoAnalise.EMITIR_NOTIFICACAO;
        ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
        ctrl.invalidarDocumento = invalidarDocumento;
        ctrl.baixarDocumento = baixarDocumento;
        ctrl.baixarDocumentoAnalise = baixarDocumentoAnalise;
        ctrl.clonarParecer = clonarParecer;

        ctrl.$onInit = function() {

            setAnaliseTecnica(ctrl.analiseTecnica);
        };
        
        function setAnaliseTecnica(value) {

            ctrl.analiseTecnica.documentos = value.documentos || [];
            ctrl.analiseTecnica.tipoResultadoAnalise = value.tipoResultadoAnalise || {id : 0};
            ctrl.analiseTecnica.analisesDocumentos = !_.isEmpty(value.analisesDocumentos) ? value.analisesDocumentos : criarAnalisesDocumentos(value.analise.processo.caracterizacoes[0].documentosEnviados);
        }

        function clonarParecer() {

            analiseTecnicaService.getParecerByNumeroProcesso(ctrl.numeroProcesso)
                .then(function(response){

                    if(response.data === null) {

                        ctrl.analiseTecnica.parecer = null;
                        mensagem.error('Não foi encontrado um parecer para esse número de processo.');
                        return;
                    }
                    ctrl.analiseTecnica.parecer = response.data.parecer;

                }, function(error){

                    mensagem.error(error.data.texto);
                });
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

            var analiseDocumento = ctrl.analiseTecnica.analisesDocumentos[indice];

                var modalInstance = $uibModal.open({

                    component: 'modalParecerDocumento',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {

                        nomeDocumento: function() {

                            return analiseDocumento.documento.tipo.nome;
                        },

                        parecer: function() {

                            return analiseDocumento.parecer;
                        }
                    }
                });

                modalInstance.result.then(function(response){

                    analiseDocumento.parecer = response;
                
                }, function() {

                    if(!analiseDocumento.parecer) {

                        analiseDocumento.validado = undefined;
                    }
                });
        }

        function baixarDocumento(idDocumento) {

        }

        function baixarDocumentoAnalise(idDocumento) {

        }
    },

    templateUrl: 'components/parecer/parecer.html'
};

exports.directives.Parecer = Parecer;