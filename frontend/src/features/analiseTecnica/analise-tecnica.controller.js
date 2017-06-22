var AnaliseTecnicaController = function ($rootScope, $scope, $routeParams, $window, $location,
    analiseTecnica, documentoLicenciamentoService, uploadService, mensagem, $uibModal, analiseTecnicaService, documentoAnaliseService, processoService, tamanhoMaximoArquivoAnaliseMB) {

    $rootScope.tituloPagina = 'PARECER Técnico';

    var ctrl = this;

    ctrl.processo = angular.copy(analiseTecnica.analise.processo);
    ctrl.exibirDadosProcesso = exibirDadosProcesso;
    ctrl.concluir = concluir;
    ctrl.salvar = salvar;
    ctrl.cancelar = cancelar;

    ctrl.init = function () {

        ctrl.analiseTecnica = angular.copy(analiseTecnica);
    };

    function analiseValida() {

        ctrl.formularioParecer.$setSubmitted();
        ctrl.formularioResultado.$setSubmitted();

        var parecerPreenchido = ctrl.formularioParecer.$valid;
        var resultadoPreenchido = ctrl.formularioResultado.$valid;
        var todosDocumentosValidados = true;
        var todosDocumentosAvaliados = true;

        ctrl.analiseTecnica.analisesDocumentos.forEach(function (analise) {

            todosDocumentosAvaliados = todosDocumentosAvaliados && (analise.validado === true || (analise.validado === false && analise.parecer));
            todosDocumentosValidados = todosDocumentosValidados && analise.validado;
        });

        if (ctrl.analiseJuridica.tipoResultadoAnalise.id === ctrl.DEFERIDO) {

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

        console.log(ctrl.analiseTecnica);
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
};

exports.controllers.AnaliseTecnicaController = AnaliseTecnicaController;

