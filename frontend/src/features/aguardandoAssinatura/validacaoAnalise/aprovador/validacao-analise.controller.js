var ValidacaoAnaliseAprovadorController = function($rootScope, $route, $routeParams, $scope, 
		mensagem, $location,processoService, $uibModal, analiseService, analiseJuridicaService, analiseTecnicaService) {

    var validacaoAnaliseAprovador = this;

    validacaoAnaliseAprovador.formularios = {};
    validacaoAnaliseAprovador.tabAtiva = 0;
    validacaoAnaliseAprovador.init = init;
    validacaoAnaliseAprovador.exibirDadosProcesso = exibirDadosProcesso;
    validacaoAnaliseAprovador.carregarDadosAnaliseJuridica = carregarDadosAnaliseJuridica;

    function init() {

      analiseService.getAnalise($routeParams.idAnalise)
        .then(function(response){
          validacaoAnaliseAprovador.analise = response.data;
          validacaoAnaliseAprovador.analise.processo.empreendimento.municipio = 
              validacaoAnaliseAprovador.analise.processo.empreendimento.endereco.municipio;
          carregarDadosAnaliseJuridica();
          carregarDadosAnaliseGeo();
		  carregarDadosAnaliseTecnica();
        });
    }

    function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseAprovador.analise.processo.id,
            numero: validacaoAnaliseAprovador.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseAprovador.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseAprovador.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseAprovador.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseAprovador.analise.processo.empreendimento.pessoa.cpf;
        }		

        processoService.visualizarProcesso(processo);
    }    

    function carregarDadosAnaliseJuridica() {

    	if(validacaoAnaliseAprovador.analise) {
			analiseJuridicaService.getAnaliseJuridica(validacaoAnaliseAprovador.analise.analiseJuridica.id)
				.then(function(response){
					validacaoAnaliseAprovador.analiseJuridica = response.data;
				});
      	}

    }

    function carregarDadosAnaliseGeo() {

		if(validacaoAnaliseAprovador.analise) {
			analiseTecnicaService.getRestricoesGeo(validacaoAnaliseAprovador.analise.analiseTecnica.id)
				.then(function(response) {
					validacaoAnaliseAprovador.analiseGeo = response.data;

				});
		}
    }

};

exports.controllers.ValidacaoAnaliseAprovadorController = ValidacaoAnaliseAprovadorController;
