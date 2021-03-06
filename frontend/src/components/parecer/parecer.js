var Parecer = {

    bindings: {

        analiseTecnica: '<',
        formularios: '=',
        usuarioSessao: '='
    },
    controller: function(tamanhoMaximoArquivoAnaliseMB, $uibModal, mensagem, analiseTecnicaService, uploadService, documentoLicenciamentoService, documentoAnaliseService, modalSimplesService, TiposAnalise) {

        var ctrl = this;

        ctrl.DEFERIDO = app.utils.TiposResultadoAnalise.DEFERIDO;
        ctrl.INDEFERIDO = app.utils.TiposResultadoAnalise.INDEFERIDO;
        ctrl.EMITIR_NOTIFICACAO = app.utils.TiposResultadoAnalise.EMITIR_NOTIFICACAO;
        ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
        ctrl.invalidarDocumento = invalidarDocumento;
        ctrl.invalidarEmissaoLicenca = invalidarEmissaoLicenca;
        ctrl.baixarDocumento = baixarDocumento;
        ctrl.baixarDocumentoAnalise = baixarDocumentoAnalise;
        ctrl.clonarParecer = clonarParecer;
        ctrl.alterarLicenca = alterarLicenca;
        ctrl.removerDocumento = removerDocumento;
        ctrl.tiposAnalise = TiposAnalise;
        ctrl.visualizarJustificativaNotificacao = visualizarJustificativaNotificacao;

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

       ctrl.$postLink = function() {

            ctrl.formularios = {

                parecer: ctrl.formularioParecer,
                resultado: ctrl.formularioResultado
            };
	   };

	   ctrl.downloadPDFParecer = function() {

        var analise = JSON.parse(JSON.stringify(this.analiseTecnica));

        analise.analise.processo.empreendimento = null;

        documentoAnaliseService.generatePDFParecerTecnico(analise)
            .then(
                function(data, status, headers){

                    var a = document.createElement('a');
                    a.href = URL.createObjectURL(data.data.response.blob);
                    a.download = data.data.response.fileName ? data.data.response.fileName : 'parecer_analise_tecnica.pdf';
                    a.click();
                },

                function(error){

                    mensagem.error(error.data.texto);
                }
            );
       };

        function setAnaliseTecnica(value) {

            ctrl.analiseTecnica.documentos = value.documentos || [];
            ctrl.analiseTecnica.analisesDocumentos = !_.isEmpty(value.analisesDocumentos) ? value.analisesDocumentos : criarAnalisesDocumentos(value.analise.processo.caracterizacao.documentosEnviados);

            ctrl.formParecer = ctrl.formularioParecer;
            ctrl.formResultado = ctrl.formularioResultado;
        }

        function clonarParecer() {

            analiseTecnicaService.getParecerByNumeroProcesso(ctrl.numeroProcesso)
                .then(function(response){

                    if(response.data === null) {

                        ctrl.analiseTecnica.parecer = null;
                        mensagem.warning('Não foi encontrado um parecer para esse número de protocolo.');
                        return;
                    }
                    ctrl.analiseTecnica.parecer = response.data.parecer;

                }, function(error){

                    mensagem.warning(error.data.texto);
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
            documentoLicenciamentoService.download(idDocumento);
        }

        function baixarDocumentoAnalise(idDocumento) {
            documentoAnaliseService.download(idDocumento);
        }

        function removerDocumento(indiceDocumento) {

            ctrl.analiseTecnica.documentos.splice(indiceDocumento,1);
        }

        function alterarLicenca(indice) {

            var analiseLicenca = ctrl.analiseTecnica.licencasAnalise[indice];

	        var modalInstance = $uibModal.open({

                    component: 'modalInformacoesLicenca',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {

                        dadosLicenca: function() {

                            return angular.copy(analiseLicenca);
                        }
                    }
                });


                modalInstance.result.then(function (result) {

                    ctrl.analiseTecnica.licencasAnalise[indice] = result;
                }, function(){});

        }

        var configModal = {
            titulo: 'Reprovar Licença',
            conteudo: 'Ao reprovar a licença, os dados cadastrados serão excluídos, deseja continuar?',
            labelBotaoConfirmar: 'Continuar',
            labelBotaoCancelar: 'Cancelar'
        };

        function invalidarEmissaoLicenca(analiseLicenca) {

            var instanciaModal;

            if(_.isEmpty(analiseLicenca.condicionantes) &&
                !analiseLicenca.observacao &&
                _.isEmpty(analiseLicenca.recomendacoes) &&
                analiseLicenca.validade === analiseLicenca.validadeMaxima) {

                analiseLicenca.emitir = false;

            } else {

                instanciaModal = modalSimplesService.abrirModal(configModal);

                instanciaModal.result.then(function(){

                    analiseLicenca.condicionantes = [];
                    analiseLicenca.observacao = null;
                    analiseLicenca.recomendacoes = [];
                    analiseLicenca.validade = analiseLicenca.validadeMaxima;

                    analiseLicenca.emitir = false;

                }, function(){});

            }

        }

        function visualizarJustificativaNotificacao(justificativa) {

            if(!justificativa) return;

            var configModal = {
                titulo: 'Justificativa de notificação',
                conteudo: justificativa,
                labelBotaoCancelar: 'Fechar',
                exibirFooter: false
            };

            modalSimplesService.abrirModal(configModal);

        }

    },

    templateUrl: 'components/parecer/parecer.html'
};

exports.directives.Parecer = Parecer;
