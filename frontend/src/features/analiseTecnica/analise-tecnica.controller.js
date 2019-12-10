var AnaliseTecnicaController = function ($rootScope, $scope, $window, $location,
    analiseTecnica, uploadService, mensagem, $uibModal, analiseTecnicaService,
    documentoAnaliseService, inconsistenciaVistoriaService, documentoService, processoService, restricoes, idAnaliseTecnica, TiposAnalise, analistaService) {

    $rootScope.tituloPagina = 'PARECER TÉCNICO';

    var ctrl = this;

    ctrl.DEFERIDO = app.utils.TiposResultadoAnalise.DEFERIDO;
    ctrl.EMITIR_NOTIFICACAO = app.utils.TiposResultadoAnalise.EMITIR_NOTIFICACAO;
    ctrl.processo = angular.copy(analiseTecnica.analise.processo);
    ctrl.imovel = angular.copy(analiseTecnica.analise.processo.empreendimento.imovel);
    ctrl.exibirDadosProcesso = exibirDadosProcesso;
    ctrl.concluir = concluir;
    ctrl.salvar = salvar;
    ctrl.cancelar = cancelar;
    ctrl.restricoes = restricoes;
    ctrl.idAnaliseTecnica = idAnaliseTecnica;
    ctrl.formularios = {};
    ctrl.tabAtiva = 0;
    ctrl.tiposAnalise = TiposAnalise;
    ctrl.visualizarJustificativaNotificacao = visualizarJustificativaNotificacao;
    ctrl.tiposDocumentosAnalise = app.utils.TiposDocumentosAnalise;
    ctrl.semInconsistenciaVistoria = null;
    ctrl.analistasTecnico = [];
    $scope.analistaSelecionado = null;

    ctrl.errors = { 

        vistoria: {
            realizada: false,
            conclusao: false,
            data: false,
            hora: false,
            descricao: false,
            documentoRit: false,
            inconsistenciaVistoria: false
        }

    };

    ctrl.dataAtual = new Date();

    ctrl.init = function () {

        ctrl.analiseTecnica = angular.copy(analiseTecnica);

        if(ctrl.analiseTecnica.vistoria === null) {

            ctrl.analiseTecnica.vistoria = {
                realizada: null,
                documentoRit: null,
                inconsistenciaVistoria: {
                    descricaoInconsistencia: null,
                    tipoInconsistencia: null,
                    anexos: []
                },
                anexos: [],
                equipe: [],
                conclusao: null,
                data: null,
                hora: null,
                descricao: null,
                cursosDagua: null,
                tipologiaVegetal: null,
                app: null,
                ocorrencia: null,
                residuosLiquidos: null,
                outrasInformacoes: null
            };

        }

        analistaService.getAnalistasTecnicoBySetor()
        .then(function(response) {
            ctrl.analistasTecnico = response.data;
        });

    };

    ctrl.validarAnalise = function() {

        return analiseValida();
    };

    ctrl.deletarInconsistenciaVistoria = function() {

        if(ctrl.analiseTecnica.vistoria && ctrl.analiseTecnica.vistoria.inconsistenciaVistoria.id) {

            inconsistenciaVistoriaService.deletar(ctrl.analiseTecnica.vistoria.inconsistenciaVistoria.id)
                .then(function(response) {

                    mensagem.success(response.data);
                    ctrl.analiseTecnica.vistoria.inconsistenciaVistoria = {
                        descricaoInconsistencia: null,
                        tipoInconsistencia: null,
                        anexos: []
                    };

                });

        }

    };

    ctrl.marcarSemInconsistencia = function() {

        ctrl.deletarInconsistenciaVistoria();
        ctrl.semInconsistenciaVistoria = true;

    };

    ctrl.limparErrosVistoria = function() {

        ctrl.errors.vistoria = {
            realizada: false,
            conclusao: false,
            data: false,
            hora: false,
            descricao: false,
            documentoRit: false,
            inconsistenciaVistoria: false
        };

    };

    ctrl.limparVistoria = function() {

        ctrl.semInconsistenciaVistoria = null;
        ctrl.limparErrosVistoria();
        ctrl.deletarInconsistenciaVistoria();

    };

    $rootScope.$on('buscarInconsistenciaVistoria', function(event, inconsistenciaVistoria) {
        ctrl.analiseTecnica.vistoria.inconsistenciaVistoria = inconsistenciaVistoria;
        ctrl.semInconsistenciaVistoria = null;
    });

    ctrl.adicionarAnalistaEquipe = function(analistaSelecionado) {

        var isAdded = _.some(ctrl.analiseTecnica.vistoria.equipe, function(analista) {
            return analistaSelecionado && analista.usuario.id === analistaSelecionado.id;
        });

        if(!isAdded && analistaSelecionado) {

            ctrl.analiseTecnica.vistoria.equipe.push({ usuario: analistaSelecionado });

            _.remove(ctrl.analistasTecnico, function(analista) {
                return analista.id === analistaSelecionado.id;
            });

        }

    };

    ctrl.removerAnalistaEquipe = function(analistaSelecionado) {

        if(analistaSelecionado === undefined) {

            ctrl.analistasTecnico = ctrl.analiseTecnica.vistoria.equipe.map(function(analista) {
                return analista.usuario;
            });
            ctrl.analiseTecnica.vistoria.equipe = [];

        } else {

            ctrl.analistasTecnico.push(analistaSelecionado);

            _.remove(ctrl.analiseTecnica.vistoria.equipe, function(analista) {
                return analista.usuario.id === analistaSelecionado.id;
            });

        }

    };

    var vistoriaValida = function() {

        if(ctrl.analiseTecnica.vistoria.realizada === null || ctrl.analiseTecnica.vistoria.realizada === undefined) {

            ctrl.errors.vistoria.realizada = true;

        }

        if(ctrl.analiseTecnica.vistoria.realizada !== null && ctrl.analiseTecnica.vistoria.realizada === 'true') {

            if(ctrl.analiseTecnica.vistoria.conclusao === null || ctrl.analiseTecnica.vistoria.conclusao === '') {

                ctrl.errors.vistoria.conclusao = true;

            }

            if(ctrl.analiseTecnica.vistoria.documentoRit === null) {

                ctrl.errors.vistoria.documentoRit = true;

            }

            if(ctrl.analiseTecnica.vistoria.inconsistenciaVistoria.id === undefined || ctrl.analiseTecnica.vistoria.inconsistenciaVistoria.id === null) {

                ctrl.errors.vistoria.inconsistenciaVistoria = true;

            }

            if(ctrl.analiseTecnica.vistoria.data === null) {
    
                ctrl.errors.vistoria.data = true;
    
            }
            
            if(ctrl.analiseTecnica.vistoria.hora === null) {
    
                ctrl.errors.vistoria.hora = true;
    
            }

            if(ctrl.analiseTecnica.vistoria.descricao === null) {
    
                ctrl.errors.vistoria.descricao = true;
    
            }

        } else if(ctrl.analiseTecnica.vistoria.realizada !== null) {

            if(ctrl.analiseTecnica.vistoria.conclusao === null || ctrl.analiseTecnica.vistoria.conclusao === '') {

                ctrl.errors.vistoria.conclusao = true;
    
            }

        }        

        return !Object.keys(ctrl.errors.vistoria).some(function(key) {
            return ctrl.errors.vistoria[key];
        });

    };

    ctrl.openModalInconsistencia = function() {

        ctrl.limparErrosVistoria();

        $uibModal.open({
			controller: 'modalInconsistenciaVistoriaController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			templateUrl: 'features/analiseTecnica/inconsistenciaVistoria/modal-inconsistencia-vistoria.html',
			size: 'lg',
			resolve: {
				inconsistenciaVistoria: function(){
					return ctrl.analiseTecnica.vistoria.inconsistenciaVistoria;
				}
            }
		});

    };

    ctrl.avancar = function() {

        if(ctrl.tabAtiva + 1 === 2 && vistoriaValida()) {

            ctrl.tabAtiva = ctrl.tabAtiva + 1;

            if(ctrl.analiseTecnica.vistoria.realizada === 'true') {

                ctrl.analiseTecnica.vistoria.realizada = true;

            } else {

                ctrl.analiseTecnica.vistoria.realizada = false;

            }

        }

    };

    $scope.snPaste = function(e, model) {
		var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
		e.preventDefault();
		setTimeout( function(){
		  document.execCommand('insertText', false, bufferText );
		}, 10 );
    };

    ctrl.baixarDocumento = function(anexo) {

        if(!anexo.id){

            documentoService.download(anexo.key, anexo.nomeDoArquivo);

        } else {

            documentoService.downloadById(anexo.id);

        }

    };
    
    ctrl.removerDocumentoRit = function() {

        ctrl.analiseTecnica.vistoria.documentoRit = null;

    };

    ctrl.removerDocumento = function(index) {

        ctrl.analiseTecnica.vistoria.anexos.splice(index, 1);

    };

    ctrl.upload = function(file, invalidFile, tipoDocumento) {

        if(file) {

            ctrl.limparErrosVistoria();

            uploadService.save(file)
                .then(function(response) {

                    if(tipoDocumento === ctrl.tiposDocumentosAnalise.DOCUMENTO_RIT) {

                        ctrl.analiseTecnica.vistoria.documentoRit = {
                            key: response.data,
                            nomeDoArquivo: file.name,
                            tipoDocumento: {
                                id: ctrl.tiposDocumentosAnalise.DOCUMENTO_RIT
                            }
                        };

                        ctrl.errors.vistoria.documentoRit = false;

                    } else if(tipoDocumento === ctrl.tiposDocumentosAnalise.DOCUMENTO_VISTORIA) {

                        ctrl.analiseTecnica.vistoria.anexos.push({
                            key: response.data,
                            nomeDoArquivo: file.name,
                            tipoDocumento: {
                                id: ctrl.tiposDocumentosAnalise.DOCUMENTO_VISTORIA
                            }
                        });

                    }

                }, function(error){
                    mensagem.error(error.data.texto);
                });

        } else if(invalidFile && invalidFile.$error === 'maxSize'){
            mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name);
        }

    };

    ctrl.downloadPDFNotificacao = function() {

        var analise = JSON.parse(JSON.stringify(this.analiseTecnica));

        analise.analise.processo.empreendimento = null;

        documentoAnaliseService.generatePDFNotificacaoParecerTecnico(analise)
            .then(
                function(data, status, headers){

                    var a = document.createElement('a');
                    a.href = URL.createObjectURL(data.data.response.blob);
                    a.download = data.data.response.fileName ? data.data.response.fileName : 'previa_notificacao_analise_tecnica.pdf';
                    a.click();
                },

                function(error){

                    mensagem.error(error.data.texto);
                }
            );
    };

    function verificarEmissoes() {

        if(ctrl.analiseTecnica.tipoResultadoAnalise.id !== ctrl.DEFERIDO)
            return true;

        for (i = 0; i < ctrl.analiseTecnica.licencasAnalise.length ; i++){
            if (ctrl.analiseTecnica.licencasAnalise[i].emitir){
                return true;
            }
        }
        return false;
    }


    function analiseValida() {

        ctrl.formularios.parecer.$setSubmitted();
        ctrl.formularios.resultado.$setSubmitted();

        var parecerPreenchido = ctrl.formularios.parecer.$valid;
        var resultadoPreenchido = ctrl.formularios.resultado.$valid;

        if(!parecerPreenchido || !resultadoPreenchido) {

            return false;
        }        
        
        var todosDocumentosValidados = true;
        var todosDocumentosAvaliados = true;

        ctrl.analiseTecnica.analisesDocumentos.forEach(function (analise) {

            if(analise.documento.tipo.tipoAnalise === ctrl.tiposAnalise.TECNICA) {

                todosDocumentosAvaliados = todosDocumentosAvaliados && (analise.validado === true || (analise.validado === false && analise.parecer));
                todosDocumentosValidados = todosDocumentosValidados && analise.validado;
            }
        });

        if (ctrl.analiseTecnica.tipoResultadoAnalise.id === ctrl.DEFERIDO) {

            return parecerPreenchido && todosDocumentosAvaliados && todosDocumentosValidados && resultadoPreenchido;

        } else if(ctrl.analiseTecnica.tipoResultadoAnalise.id === ctrl.EMITIR_NOTIFICACAO) {

            return parecerPreenchido && todosDocumentosAvaliados && resultadoPreenchido && !todosDocumentosValidados;

        } else {

            return parecerPreenchido && todosDocumentosAvaliados && resultadoPreenchido;
        }
    }

    function exibirDadosProcesso() {

        var processo = {

            idProcesso: ctrl.processo.id,
            numero: ctrl.processo.numero,
            denominacaoEmpreendimento: ctrl.processo.empreendimento.denominacao
        };

        if (ctrl.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = ctrl.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = ctrl.processo.empreendimento.pessoa.cpf;
        }
        processoService.visualizarProcesso(processo);
    }

    function concluir() {

        if(!analiseValida()) {
 
            mensagem.error('Não foi possível concluir a análise. Verifique se as seguintes condições foram satisfeitas: ' +
            '<ul>' +
                '<li>Para concluir é necessário descrever o parecer.</li>' + 
                '<li>Selecione um parecer para o protocolo (Deferido, Indeferido, Notificação).</li>' + 
                '<li>Para DEFERIDO, todos os documentos de validação técnica devem ter sido validados.</li>' + 
                '<li>Para EMITIR NOTIFICAÇÃO, pelo menos um documento de validação jurídica deve ter sido invalidado.</li>' + 
            '</ul>', { ttl: 10000 });
            return;            
        }

        if(!verificarEmissoes()){
            mensagem.error('Não foi possível concluir a análise. Verifique se as seguintes condições foram satisfeitas: ' +
            '<ul>' +
                '<li>Para o status Deferido, ao menos uma licença deve ser emitida.</li>' + 
                '<li>Se desejar indeferir o protocolo, marque a opção "Indeferido".</li>' + 
            '</ul>', { ttl: 10000 });
            return; 
        }

        ctrl.analiseTecnica.analise.processo.empreendimento = null;
        analiseTecnicaService.concluir(ctrl.analiseTecnica)
            .then(function(response) {

                mensagem.success(response.data.texto);
                $location.path('/analise-tecnica');

            }, function(error){

                mensagem.error(error.data.texto);
            });
    }

    function salvar() {

        ctrl.analiseTecnica.analise.processo.empreendimento = null;
        analiseTecnicaService.salvar(ctrl.analiseTecnica)
            .then(function (response) {

                mensagem.success(response.data.texto);
                carregarAnalise();

            }, function (error) {

                mensagem.error(error.data.texto);
            });
    }

    function cancelar() {

        ctrl.deletarInconsistenciaVistoria();
        $window.history.back();
    }

    function carregarAnalise() {

        analiseTecnicaService.getAnaliseTecnica(ctrl.analiseTecnica.id)
            .then(function (response) {

                ctrl.analiseTecnica = response.data;
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

};

exports.controllers.AnaliseTecnicaController = AnaliseTecnicaController;

