var AnaliseTecnicaController = function ($rootScope, $scope, $routeParams, $window, $location,
    analiseTecnica, documentoLicenciamentoService, uploadService, mensagem, $uibModal, analiseTecnicaService,
    documentoAnaliseService, processoService, tamanhoMaximoArquivoAnaliseMB, restricoes, idAnaliseTecnica, TiposAnalise) {

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

    ctrl.vistoria = {
        vistoriaRealizada: null,
        documentoRit: null,
        documentos: [],
        conclusao: null,
        data: null,
        hora: null,
        descricao: null,
        cursosDagua: null,
        tipologiaVegetal: null,
        app: null,
        ocorrencia: null,
        residuosLiquidos: null,
        outras: null
    };

    ctrl.errors = { 

        vistoria: {
            vistoriaRealizada: false,
            conclusao: false,
            data: false,
            hora: false,
            descricao: false
        }

    };

    ctrl.dataAtual = new Date();

    ctrl.init = function () {

        ctrl.analiseTecnica = angular.copy(analiseTecnica);
        
    };

    ctrl.validarAnalise = function() {

        return analiseValida();
    };

    var vistoriaValida = function() {

        if(ctrl.vistoria.vistoriaRealizada === null || ctrl.vistoria.vistoriaRealizada === undefined) {

            ctrl.errors.vistoria.vistoriaRealizada = true;

        }

        if(!ctrl.vistoria.vistoriaRealizada) {

            if(ctrl.vistoria.conclusao === null || ctrl.vistoria.conclusao === '') {

                ctrl.errors.vistoria.conclusao = true;
    
            }

        } else {

            if(ctrl.vistoria.conclusao === null || ctrl.vistoria.conclusao === '') {

                ctrl.errors.vistoria.conclusao = true;
    
            }
    
            if(ctrl.vistoria.data === null) {
    
                ctrl.errors.vistoria.data = true;
    
            }
            
            if(ctrl.vistoria.hora === null) {
    
                ctrl.errors.vistoria.hora = true;
    
            }

            if(ctrl.vistoria.descricao === null) {
    
                ctrl.errors.vistoria.descricao = true;
    
            }

        }        

        return Object.keys(ctrl.errors.vistoria).some(function(key) {
            return ctrl.errors.vistoria[key];
        });

    };

    ctrl.avancar = function() {

        if(ctrl.tabAtiva === 2 && vistoriaValida()) {

            ctrl.tabAtiva = ctrl.tabAtiva + 1;

        }

    };

    $scope.snPaste = function(e, model) {
		var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
		e.preventDefault();
		setTimeout( function(){
		  document.execCommand( 'insertText', false, bufferText );
		}, 10 );
    };
    
    ctrl.upload = function(file, invalidFile, tiposDocumentosAnalise) {

        if(file) {

            uploadService.save(file)
                .then(function(response) {

                    if(tiposDocumentosAnalise === app.utils.TiposDocumentosAnalise.DOCUMENTO_RIT) {

                        ctrl.vistoria.documentoRit = {
                            key: response.data,
                            nome: file.name,
                            tipoDocumento: {
                                id: app.utils.TiposDocumentosAnalise.DOCUMENTO_RIT
                            }
                        };

                    } else if(tiposDocumentosAnalise === app.utils.TiposDocumentosAnalise.DOCUMENTO_VISTORIA) {

                        ctrl.vistoria.documentos.push({
                            key: response.data,
                            nome: file.name,
                            tipoDocumento: {
                                id: app.utils.TiposDocumentosAnalise.DOCUMENTO_VISTORIA
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

