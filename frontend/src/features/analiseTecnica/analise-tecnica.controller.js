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
        ctrl.analiseTecnica.analise.processo.empreendimento = null;
    };

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

        analiseTecnicaService.salvar(ctrl.analiseTecnica)
            .then(function(response) {

                mensagem.success(response.data.texto);
                carregarAnalise();
            
            }, function(error){

                mensagem.error(error.data.texto);
            });
    }

    function cancelar() {

        $window.history.back();
    }

     function carregarAnalise() {

        analiseTecnicaService.getAnaliseTecnica(ctrl.analiseTecnica.id)
            .then(function(response){

                ctrl.analiseTecnica = response.data;
            });
     }    
};

exports.controllers.AnaliseTecnicaController = AnaliseTecnicaController;

