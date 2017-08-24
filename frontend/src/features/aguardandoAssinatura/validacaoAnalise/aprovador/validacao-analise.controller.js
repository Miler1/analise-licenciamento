var ValidacaoAnaliseAprovadorController = function($rootScope,aprovadorService, $route, $scope, 
		mensagem, $location,processoService, $uibModal) {

    var validacaoAnaliseAprovador = this;

    validacaoAnaliseAprovador.exibirDadosProcesso = exibirDadosProcesso;
    validacaoAnaliseAprovador.tabAtiva = 0;


    function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseAprovador.analiseTecnica.analise.processo.id,
            numero: validacaoAnaliseAprovador.analiseTecnica.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseAprovador.analiseTecnica.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseAprovador.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseAprovador.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseAprovador.analiseTecnica.analise.processo.empreendimento.pessoa.cpf;
        }		

        processoService.visualizarProcesso(processo);
    }

};

exports.controllers.ValidacaoAnaliseAprovadorController = ValidacaoAnaliseAprovadorController;