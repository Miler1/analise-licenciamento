var Parecer = {

    bindings: {

        analiseTecnica: '<'
    },

    controller: function(tamanhoMaximoArquivoAnaliseMB, $uibModal, mensagem, analiseTecnicaService, uploadService, documentoLicenciamentoService, documentoAnaliseService, $scope, $timeout) {

        var ctrl = this;

        ctrl.DEFERIDO = app.utils.TiposResultadoAnalise.DEFERIDO;
        ctrl.INDEFERIDO = app.utils.TiposResultadoAnalise.INDEFERIDO;
        ctrl.EMITIR_NOTIFICACAO = app.utils.TiposResultadoAnalise.EMITIR_NOTIFICACAO;
        ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
        ctrl.invalidarDocumento = invalidarDocumento;
        ctrl.baixarDocumento = baixarDocumento;
        ctrl.baixarDocumentoAnalise = baixarDocumentoAnalise;
        ctrl.clonarParecer = clonarParecer;
        ctrl.alterarLicenca = alterarLicenca;
        ctrl.upload = function(file, invalidFile) {

            if(file) {

                uploadService.save(file)
                    .then(function(response) {

                        ctrl.analiseTecnica.documentos.push({

                            key: response.data,
                            nome: file.name,
                            tipoDocumento: {

                                id: app.utils.TiposDocumentosAnalise.ANALISE_TECNICA
                            }
                        });

                    }, function(error){

                        mensagem.error(error.data.texto);
                    });
            
            } else if(invalidFile && invalidFile.$error === 'maxSize'){

                mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name + ' . Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
            }            
        };        

        ctrl.$onInit = function() {

            setAnaliseTecnica(ctrl.analiseTecnica);
       };
        
        function setAnaliseTecnica(value) {

            ctrl.analiseTecnica.documentos = value.documentos || [];
            ctrl.analiseTecnica.analisesDocumentos = !_.isEmpty(value.analisesDocumentos) ? value.analisesDocumentos : criarAnalisesDocumentos(value.analise.processo.caracterizacoes[0].documentosEnviados);
        }

        function clonarParecer() {

            ctrl.formularioParecer.$setSubmitted();
            ctrl.formularioResultado.$setSubmitted();

            console.log(ctrl.analiseTecnica.tipoResultadoAnalise);
            
            // analiseTecnicaService.getParecerByNumeroProcesso(ctrl.numeroProcesso)
            //     .then(function(response){

            //         if(response.data === null) {

            //             ctrl.analiseTecnica.parecer = null;
            //             mensagem.error('Não foi encontrado um parecer para esse número de processo.');
            //             return;
            //         }
            //         ctrl.analiseTecnica.parecer = response.data.parecer;

            //     }, function(error){

            //         mensagem.error(error.data.texto);
            //     });
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
            documentoLicenciamentoService.download(idDocumento);                
        }

        function baixarDocumentoAnalise(idDocumento) {
            documentoAnaliseService.download(idDocumento);
        }

        function alterarLicenca(licenca) {

        }
    },

    templateUrl: 'components/parecer/parecer.html'
};

exports.directives.Parecer = Parecer;