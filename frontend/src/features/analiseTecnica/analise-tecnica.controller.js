var AnaliseTecnicaController = function($rootScope, $scope, $routeParams, $window, $location, 
        analiseTecnica, documentoLicenciamentoService, uploadService, mensagem, $uibModal, analiseTecnicaService, documentoAnaliseService, processoService) {    

    var ctrl = this;
    
    $rootScope.tituloPagina = 'PARECER Técnico';

    ctrl.processo = angular.copy(analiseTecnica.analise.processo);


    ctrl.exibirDadosProcesso = exibirDadosProcesso;
    
    function exibirDadosProcesso() {

        var processo = {

            idProcesso: ctrl.processo.id,
            numero: ctrl.processo.numero,
            denominacaoEmpreendimento: ctrl.processo.empreendimento.denominacao
        };

        if(ctrl.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = ctrl.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = ctrl.processo.empreendimento.pessoa.cpf;
        }
        processoService.visualizarProcesso(processo);
    }
};

exports.controllers.AnaliseTecnicaController = AnaliseTecnicaController;

