var AnaliseTecnicaController = function ($rootScope, $scope, $routeParams, $window, $location,
    analiseTecnica, documentoLicenciamentoService, uploadService, mensagem, $uibModal, analiseTecnicaService,
    documentoAnaliseService, processoService, tamanhoMaximoArquivoAnaliseMB, restricoes, idAnaliseTecnica) {

    $rootScope.tituloPagina = 'PARECER TÉCNICO';

    var ctrl = this;

    ctrl.DEFERIDO = app.utils.TiposResultadoAnalise.DEFERIDO;
    ctrl.processo = angular.copy(analiseTecnica.analise.processo);
    ctrl.exibirDadosProcesso = exibirDadosProcesso;
    ctrl.concluir = concluir;
    ctrl.salvar = salvar;
    ctrl.cancelar = cancelar;
    ctrl.restricoes = restricoes;
    ctrl.idAnaliseTecnica = idAnaliseTecnica;
    ctrl.formularios = {};
    ctrl.tabAtiva = 0;
    
    ctrl.init = function () {

        ctrl.analiseTecnica = angular.copy(analiseTecnica);
    };

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

            todosDocumentosAvaliados = todosDocumentosAvaliados && (analise.validado === true || (analise.validado === false && analise.parecer));
            todosDocumentosValidados = todosDocumentosValidados && analise.validado;
        });

        if (ctrl.analiseTecnica.tipoResultadoAnalise.id === ctrl.DEFERIDO) {

            return parecerPreenchido && todosDocumentosAvaliados && todosDocumentosValidados && resultadoPreenchido;

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
                '<li>Selecione um parecer para o processo (Deferido, Indeferido, Notificação).</li>' + 
                '<li>Para DEFERIDO, todos os documentos de validação jurídica devem estar no status válido.</li>' + 
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
        ctrl.analiseTecnica.licencasAnalise = [];
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
};

exports.controllers.AnaliseTecnicaController = AnaliseTecnicaController;

