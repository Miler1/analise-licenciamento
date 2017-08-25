var ValidacaoAnaliseAprovadorController = function($rootScope, $route, $routeParams, $scope, 
		mensagem, $location,processoService, $uibModal, analiseService, analiseJuridicaService, analiseTecnicaService,
		documentoAnaliseService) {

	var validacaoAnaliseAprovador = this;

	validacaoAnaliseAprovador.exibirDadosProcesso = exibirDadosProcesso;
	validacaoAnaliseAprovador.formularios = {};
	validacaoAnaliseAprovador.tabAtiva = 0;
	validacaoAnaliseAprovador.init = init;
	validacaoAnaliseAprovador.carregarDadosAnaliseJuridica = carregarDadosAnaliseJuridica;
	validacaoAnaliseAprovador.carregarDadosAnaliseTecnica = carregarDadosAnaliseTecnica;
	validacaoAnaliseAprovador.downloadDocumentoAnalise = downloadDocumentoAnalise;
	validacaoAnaliseAprovador.visualizarLicenca = visualizarLicenca;

	function init() {

	  analiseService.getAnalise($routeParams.idAnalise)
		.then(function(response){
		  validacaoAnaliseAprovador.analise = response.data;
		  carregarDadosAnaliseJuridica();
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


	function carregarDadosAnaliseTecnica() {

		analiseTecnicaService.getAnaliseTecnica($routeParams.idAnalise)
			.then(function(response){
				validacaoAnaliseAprovador.analiseTecnica = response.data;

				validacaoAnaliseAprovador.analiseTecnicaValidacao.idAnalistaTecnico =
					validacaoAnaliseAprovador.analiseTecnica.analistasTecnicos[0].usuario.id;
				
				if (validacaoAnaliseAprovador.analiseTecnica.tipoResultadoValidacaoGerente) {

					validacaoAnaliseAprovador.analiseTecnicaValidacao.idTipoResultadoValidacaoGerente =
						validacaoAnaliseAprovador.analiseTecnica.tipoResultadoValidacaoGerente.id;
				}
				
				validacaoAnaliseAprovador.analiseTecnicaValidacao.parecerValidacaoGerente =
					validacaoAnaliseAprovador.analiseTecnica.parecerValidacaoGerente;

				analistaService.getAnalistasTecnicosByProcesso(validacaoAnaliseAprovador.analiseTecnica.analise.processo.id)
					.then(function(response){
						validacaoAnaliseAprovador.analistas = response.data;
				});			
			

			});

	}

	function downloadDocumentoAnalise(idDocumento) {

		documentoAnaliseService.download(idDocumento);
	}

	function visualizarLicenca(indice) {

		var analiseLicenca = validacaoAnaliseAprovador.analiseTecnica.licencasAnalise[indice];

		var modalInstance = $uibModal.open({

				templateUrl: './parecerTecnico/modalVisualizarLicenca.html',
				size: 'lg',
				backdrop: 'static',
				resolve: {

					dadosLicenca: function() {

						return angular.copy(analiseLicenca);
					},
					isVisualizacao: true
				}
			});


			modalInstance.result.then(function (result) {

				validacaoAnaliseAprovador.analiseTecnica.licencasAnalise[indice] = result;
			}, function(){});

	}

};

exports.controllers.ValidacaoAnaliseAprovadorController = ValidacaoAnaliseAprovadorController;