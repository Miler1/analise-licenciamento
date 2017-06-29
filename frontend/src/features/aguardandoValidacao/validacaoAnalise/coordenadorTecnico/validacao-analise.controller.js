var ValidacaoAnaliseTecnicaController = function($rootScope, analiseTecnicaService, $route, $scope, 
		mensagem, $location, documentoAnaliseService, processoService) {

    var validacaoAnaliseTecnica = this;

    validacaoAnaliseTecnica.init = init;
    validacaoAnaliseTecnica.exibirDadosProcesso = exibirDadosProcesso; 
    validacaoAnaliseTecnica.exibirAnaliseJuridica = exibirAnaliseJuridica;


    function init() {

		analiseTecnicaService.getAnaliseTecnica($route.current.params.idAnalise)
			.then(function(response){
				validacaoAnaliseTecnica.analiseTecnica = response.data;

			});
		
		$rootScope.$broadcast('atualizarContagemProcessos');        
    }

	function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseTecnica.analiseTecnica.analise.processo.id,
            numero: validacaoAnaliseTecnica.analiseTecnica.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cpf;
        }		

        processoService.visualizarProcesso(processo);
    }    

    function exibirAnaliseJuridica() {
        
    }

};

exports.controllers.ValidacaoAnaliseTecnicaController = ValidacaoAnaliseTecnicaController;