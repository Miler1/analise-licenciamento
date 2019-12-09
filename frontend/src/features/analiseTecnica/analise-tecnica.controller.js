var AnaliseTecnicaController = function ($rootScope, $window, $location,
    analiseTecnica,  mensagem, $uibModal, analiseTecnicaService, $route,
    documentoAnaliseService, processoService, restricoes, TiposAnalise,inconsistenciaService) {

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
    ctrl.idAnaliseTecnica = $route.current.params.idAnaliseTecnica;
    ctrl.formularios = {};
    ctrl.tabAtiva = 0;
    ctrl.tiposAnalise = TiposAnalise;
    ctrl.visualizarJustificativaNotificacao = visualizarJustificativaNotificacao;
    ctrl.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica;
    ctrl.analistaTecnico = $rootScope.usuarioSessao.usuarioEntradaUnica.nome;
   
    ctrl.itemValidoLicenca = {
        tipoLicenca: null,
        atividade: null,
        parametro: null,
        questionario: null,
        documento: null
    };
    
    ctrl.init = function () {

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
            .then(function(response){
                ctrl.analiseTecnica = response.data;
                ctrl.validarItensLicenca(app.utils.InconsistenciaTecnica.TIPO_LICENCA, null, ctrl.analiseTecnica);
                ctrl.validarItensLicenca(app.utils.InconsistenciaTecnica.ATIVIDADE, null, ctrl.analiseTecnica);
                var parametros = _.find(ctrl.analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                    if(inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null){
                        return _.find(inconsistenciaTecnica.inconsistenciaTecnicaParametro, function(parametroAtividade){
                            return parametroAtividade;
                        });
                    }
                });
                ctrl.validarInconsistenciaParametro(app.utils.InconsistenciaTecnica.ATIVIDADE, parametros);
                ctrl.validarItensLicenca(app.utils.InconsistenciaTecnica.QUESTIONARIO, null, ctrl.analiseTecnica);
                ctrl.validarItensLicenca(app.utils.InconsistenciaTecnica.DOCUMENTO, null, ctrl.analiseTecnica);
            }); 
    };

    ctrl.validarAnalise = function() {

        return analiseValida();
    };

    ctrl.validarInconsistenciaParametro = function(tipoDeInconsistenciaTecnica, parametro) {

        if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.PARAMETRO){

            inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                if(inconsistenciaTecnica !== null){
                    return inconsistenciaTecnica.inconsistenciaTecnicaParametro;
                }
            });

            if( inconsistenciaTecnica !== undefined && inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null && parametro.id === inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id ){           
                ctrl.itemValidoLicenca.parametro = false;
                return false;
            }else{
                ctrl.itemValidoLicenca.parametro = true;
                return true;
            }
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

    ctrl.adicionarInconsistenciaTecnicaTipoLicenca = function (analiseTecnica, tipoDeInconsistenciaTecnica) {

        var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
    
                return inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca;
            });
       
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null);
   
    };

    ctrl.adicionarInconsistenciaTecnicaAtividade = function (analiseTecnica,tipoDeInconsistenciaTecnica, atividadeCaracterizacao) {

        var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
    
            return inconsistenciaTecnica.inconsistenciaTecnicaAtividade;
        });
   
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, atividadeCaracterizacao, null);

    };

    ctrl.adicionarInconsistenciaTecnicaParametro = function (analiseTecnica,tipoDeInconsistenciaTecnica, parametroAtividade) {

        var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){

            if(inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null){
                if (parametroAtividade.id === inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id){
                    return inconsistenciaTecnica.inconsistenciaTecnicaParametro;
                }
            }
        });
   
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, parametroAtividade);

    };

    // Preparação para adicionar outros tipos de inconsistencia

    // ctrl.adicionarInconsistenciaTecnicaQuestionario = function (analiseTecnica,tipoDeInconsistenciaTecnica) {

    //     var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
    
    //         return inconsistenciaTecnica.inconsistenciaTecnicaQuestionario;
    //     });
   
    //     openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, atividadeCaracterizacao, null);

    // };

    // ctrl.adicionarInconsistenciaTecnicaDocumento = function (analiseTecnica,tipoDeInconsistenciaTecnica) {

    //     var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
    
    //         return inconsistenciaTecnica.inconsistenciaTecnicaDocumento;
    //     });
   
    //     openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, atividadeCaracterizacao, null);

    // };

    function openModal(analiseTecnicaModal, tipoDeInconsistenciaTecnicaModal, inconsistenciaTecnicaModal, atividadeCaracterizacaoModal, parametroAtividadeModal) {
            
        $uibModal.open({
            animation: true,
            templateUrl: './features/analiseTecnica/modalInconsistenciaTecnica.html',
            controller: 'inconsistenciaTecnicaController',
            controllerAs: 'modalCtrl',
            size: 'lg',
            resolve: {
                analiseTecnica: function () {
                    return analiseTecnicaModal;
                },
                tipoDeInconsistenciaTecnica: function(){
                    return tipoDeInconsistenciaTecnicaModal;
                },
                inconsistenciaTecnica: function(){
                    return inconsistenciaTecnicaModal;
                },
                atividadeCaracterizacao: function(){
                    return atividadeCaracterizacaoModal;
                },
                parametroAtividade: function(){
                    return parametroAtividadeModal;
                }
            }
        });
    }

    ctrl.validarItensLicenca = function (tipoDeInconsistenciaTecnica, parametro, analiseTecnica){

        var verificaAnaliseTecnica = null;

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id).then(function(response){
            verificaAnaliseTecnica = response.data;
        
            if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.TIPO_LICENCA){

                inconsistenciaTecnica = _.some( verificaAnaliseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                   return inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca;
                });

                if (!inconsistenciaTecnica){
                    ctrl.itemValidoLicenca.tipoLicenca = false;
                }else{
                    ctrl.itemValidoLicenca.tipoLicenca = true;
                }

            }else if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.ATIVIDADE){

                inconsistenciaTecnica = _.some( verificaAnaliseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                        return inconsistenciaTecnica.inconsistenciaTecnicaAtividade;
                });     

                if(!inconsistenciaTecnica){
                    ctrl.itemValidoLicenca.atividade = false;
                }else{
                    ctrl.itemValidoLicenca.atividade = true;
                }

            }else if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.QUESTIONARIO){

                inconsistenciaTecnica = _.some( verificaAnaliseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                    return inconsistenciaTecnica.inconsistenciaTecnicaQuestionario;
                });

                if(!inconsistenciaTecnica){            
                    ctrl.itemValidoLicenca.questionario = false;
                }else{
                    ctrl.itemValidoLicenca.questionario = true;
                }               

            }else if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO){

                inconsistenciaTecnica = _.some( verificaAnaliseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                    return inconsistenciaTecnica.inconsistenciaTecnicaDocumento;
                });

                if(!inconsistenciaTecnica){
                    ctrl.itemValidoLicenca.documento = false;
                }else{
                    ctrl.itemValidoLicenca.documento = true;
                }          

            }
        });

    };
    
    ctrl.excluirInconsistencia = function (analiseTecnica, tipoDeInconsistenciaTecnica, parametro){

        inconsistenciaTecnica =  _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){

            if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.TIPO_LICENCA){
                ctrl.itemValidoLicenca.tipoLicenca = false;
                return inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca;

            }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.ATIVIDADE){
                ctrl.itemValidoLicenca.atividade = false;
                return inconsistenciaTecnica.inconsistenciaTecnicaAtividade;

            }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.PARAMETRO){
                
                if (inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null && parametro.id === inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id){
                    ctrl.itemValidoLicenca.parametro = false;
                    return inconsistenciaTecnica.inconsistenciaTecnicaParametro;
                }             

            }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.QUESTIONARIO){
                ctrl.itemValidoLicenca.questionario = false;
                return inconsistenciaTecnica.inconsistenciaTecnicaQuestionario;

            }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO){
                ctrl.itemValidoLicenca.documento = false;
                return inconsistenciaTecnica.inconsistenciaTecnicaDocumento;
            }
        });
        inconsistenciaTecnica.tipoDeInconsistenciaTecnica = tipoDeInconsistenciaTecnica;

        inconsistenciaService.excluirInconsistenciaTecnica(inconsistenciaTecnica)
            .then(function (response) {

                mensagem.success(response.data);
                $uibModalInstance.close();
                if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.PARAMETRO){
                    if (parametro.id === inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id){
                        ctrl.validarInconsistenciaParametro(tipoDeInconsistenciaTecnica, parametro);
                    }
                }

            }).catch(function (response) {
            mensagem.error(response.data.texto);

        });        
    };

    $rootScope.$on('atualizarMarcacaoInconsistencia', function(event, tipoDeInconsistenciaTecnica) {
        ctrl.validarItensLicenca(tipoDeInconsistenciaTecnica, null, ctrl.analiseTecnica);
        ctrl.validarInconsistenciaParametro(tipoDeInconsistenciaTecnica, parametro); 

    });

};

exports.controllers.AnaliseTecnicaController = AnaliseTecnicaController;
