var AnaliseJuridicaController = function($rootScope, $scope, $routeParams, $window, $location,
        analiseJuridica, documentoLicenciamentoService, uploadService, mensagem, $uibModal, analiseJuridicaService,
        documentoAnaliseService, processoService, TiposAnalise, modalSimplesService) {

    $rootScope.tituloPagina = 'PARECER JURÍDICO';
    var TAMANHO_MAXIMO_ARQUIVO_MB = 10;
    var ctrl = this;

    ctrl.DEFERIDO = app.utils.TiposResultadoAnalise.DEFERIDO;
    ctrl.INDEFERIDO = app.utils.TiposResultadoAnalise.INDEFERIDO;
    ctrl.EMITIR_NOTIFICACAO = app.utils.TiposResultadoAnalise.EMITIR_NOTIFICACAO;
    ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
    ctrl.analiseJuridica = angular.copy(analiseJuridica);
    ctrl.processo = angular.copy(ctrl.analiseJuridica.analise.processo);
    ctrl.analiseJuridica.analise.processo.empreendimento = null;
    ctrl.analiseJuridica.tipoResultadoAnalise = ctrl.analiseJuridica.tipoResultadoAnalise || {};
    ctrl.documentosAnalisados = angular.copy();
    ctrl.documentosParecer = angular.copy(ctrl.analiseJuridica.documentos || []);
    ctrl.editarMotivoInvalidacao = editarMotivoInvalidacao;
    ctrl.tiposAnalise = TiposAnalise;
    ctrl.visualizarJustificativaNotificacao = visualizarJustificativaNotificacao;

    ctrl.upload = function(file, invalidFile) {

        if(file) {

            uploadService.save(file)
                .then(function(response) {

                    ctrl.documentosParecer.push({

                        key: response.data,
                        nome: file.name,
                        tipoDocumento: {

                            id: app.utils.TiposDocumentosAnalise.ANALISE_JURIDICA
                        }
                    });

                }, function(error){

                    mensagem.error(error.data.texto);
                });

        } else if(invalidFile && invalidFile.$error === 'maxSize'){

            mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name + ' . Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
        }
    };

    ctrl.cancelar = function() {

        $window.history.back();
    };

    ctrl.salvar = function() {

        montarAnaliseJuridica();
        analiseJuridicaService.salvar(ctrl.analiseJuridica)
            .then(function(response) {

                mensagem.success(response.data.texto);
                carregarAnalise();

            }, function(error){

                mensagem.error(error.data.texto);
            });
    };

    ctrl.concluir = function(){

        if(!analiseValida()) {

            mensagem.error('Não foi possível concluir a análise. Verifique se as seguintes condições foram satisfeitas: ' +
            '<ul>' +
                '<li>Para concluir é necessário descrever o parecer.</li>' +
                '<li>Selecione um parecer para o processo (Deferido, Indeferido, Notificação).</li>' +
                '<li>Para DEFERIDO, todos os documentos de validação jurídica devem ter sido validados.</li>' +
                '<li>Para EMITIR NOTIFICAÇÃO, pelo menos um documento de validação jurídica deve ter sido invalidado.</li>' +
            '</ul>', { ttl: 10000 });
            return;
        }

        montarAnaliseJuridica();
        analiseJuridicaService.concluir(ctrl.analiseJuridica)
            .then(function(response) {

                mensagem.success(response.data.texto);
                $location.path('/analise-tecnica');

            }, function(error){

                mensagem.error(error.data.texto);
            });
    };

    ctrl.downloadPDFNotificacao = function() {

        var analiseJuridicaNotificacao = JSON.parse(JSON.stringify(this.analiseJuridica));

        analiseJuridicaNotificacao.documentos = JSON.parse(JSON.stringify(ctrl.documentosParecer));
        analiseJuridicaNotificacao.analisesDocumentos = JSON.parse(JSON.stringify(ctrl.analisesDocumentos));

        documentoAnaliseService.generatePDFNotificacaoParecerJuridico(analiseJuridicaNotificacao)
            .then(
                function(data, status, headers){

                    var a = document.createElement('a');
                    a.href = URL.createObjectURL(data.data.response.blob);
                    a.download = data.data.response.fileName ? data.data.response.fileName : 'previa_notificacao_analise_juridica.pdf';
                    a.click();
                },

                function(error){

                    mensagem.error(error.data.texto);
                }
            );
    };

    ctrl.downloadDocumentoLicenciamento = function(idDocumento) {

        documentoLicenciamentoService.download(idDocumento);
    };

    ctrl.downloadDocumentoAnalise = function(idDocumento) {

        documentoAnaliseService.download(idDocumento);
    };

    ctrl.downloadPDFParecer = function() {

        documentoAnaliseService.generatePDFParecerJuridico(this.analiseJuridica)
            .then(
                function(data, status, headers){

                    var a = document.createElement('a');
                    a.href = URL.createObjectURL(data.data.response.blob);
                    a.download = data.data.response.fileName ? data.data.response.fileName : 'parecer_analise_juridica.pdf';
                    a.click();
                },

                function(error){

                    mensagem.error(error.data.texto);
                }
            );
    };

    ctrl.removerDocumento = function(indiceDocumento) {

        ctrl.documentosParecer.splice(indiceDocumento,1);
    };

    ctrl.clonarParecer = function() {

        analiseJuridicaService.getParecerByNumeroProcesso(ctrl.numeroProcesso)
            .then(function(response){

                if(response.data === null) {

                    ctrl.analiseJuridica.parecer = null;
                    mensagem.error('Não foi encontrado um parecer para esse número de processo.');
                    return;
                }
                ctrl.analiseJuridica.parecer = response.data.parecer;

            }, function(error){

                mensagem.error(error.data.texto);
            });
    };

    ctrl.exibirDadosProcesso = function() {

        var copiaProcesso = {

            idProcesso: ctrl.processo.id,
            numero: ctrl.processo.numero,
            denominacaoEmpreendimento: ctrl.processo.empreendimento.denominacao
        };

        if(ctrl.processo.empreendimento.pessoa.cnpj) {

            copiaProcesso.cnpjEmpreendimento = ctrl.processo.empreendimento.pessoa.cnpj;

        } else {

            copiaProcesso.cpfEmpreendimento = ctrl.processo.empreendimento.pessoa.cpf;
        }

        processoService.visualizarProcesso(copiaProcesso);
    };

    ctrl.init = function() {
        getDocumentosAnalisados();
    };

    ctrl.invalidarDocumento = function(indice) {

    };

    ctrl.validarAnalise = function() {

        return analiseValida();
    };

    function montarAnaliseJuridica() {

        ctrl.analiseJuridica.documentos = ctrl.documentosParecer;
        ctrl.analiseJuridica.analisesDocumentos = ctrl.analisesDocumentos;
    }

    function editarMotivoInvalidacao(indiceDocumento) {

        var analiseDocumento = ctrl.analisesDocumentos[indiceDocumento];

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

    function getDocumentosAnalisados() {

        analiseJuridicaService.getDocumentosAnalisados(ctrl.analiseJuridica.id)
            .then(function(response){

                ctrl.analisesDocumentos = response.data;

                if(ctrl.analisesDocumentos.length === 0) {

                    var documentosProcesso = angular.copy(ctrl.analiseJuridica
                                                .analise
                                                .processo
                                                .caracterizacao
                                                .documentosEnviados);

                    ctrl.analisesDocumentos = documentosProcesso.map(function(documento){

                        return {

                            "validado": undefined,
                            "parecer": undefined,
                            "documento": documento
                        };
                    });
                }
            });
    }

    function analiseValida() {

        ctrl.formularioParecer.$setSubmitted();
        ctrl.formularioResultado.$setSubmitted();

        var parecerPreenchido = ctrl.formularioParecer.$valid;
        var resultadoPreenchido = ctrl.formularioResultado.$valid;
        var todosDocumentosValidados = true;
        var todosDocumentosAvaliados = true;

        ctrl.analisesDocumentos.forEach(function(analise) {

            // analise apenas os documento proprios da analise juridica
            if(analise.documento.tipo.tipoAnalise === ctrl.tiposAnalise.JURIDICA) {

                todosDocumentosAvaliados = todosDocumentosAvaliados && (analise.validado === true || (analise.validado === false && analise.parecer));
                todosDocumentosValidados = todosDocumentosValidados && analise.validado;
            }

        });

        if(ctrl.analiseJuridica.tipoResultadoAnalise.id === ctrl.DEFERIDO) {

            return parecerPreenchido && todosDocumentosAvaliados && todosDocumentosValidados && resultadoPreenchido;

        } else if(ctrl.analiseJuridica.tipoResultadoAnalise.id === ctrl.EMITIR_NOTIFICACAO) {

            return parecerPreenchido && todosDocumentosAvaliados && resultadoPreenchido && !todosDocumentosValidados;

        } else {

            return parecerPreenchido && todosDocumentosAvaliados && resultadoPreenchido;

        }
    }

     function carregarAnalise() {

        analiseJuridicaService.getAnaliseJuridica(ctrl.analiseJuridica.id)
            .then(function(response){

                ctrl.analiseJuridica = response.data;
                ctrl.analisesDocumentos = ctrl.analiseJuridica.analisesDocumentos;
                ctrl.documentos = ctrl.analiseJuridica.documentos;
                ctrl.analiseJuridica.analise.processo.empreendimento = null;
            });
     }
};

exports.controllers.AnaliseJuridicaController = AnaliseJuridicaController;

