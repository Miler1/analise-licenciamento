var AnaliseTecnicaManejoController = function($rootScope, $scope, $routeParams, processoManejoService, $location, mensagem, $uibModal, observacaoService) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var TAMANHO_MAXIMO_ARQUIVO_MB = 10;

	var analiseTecnicaManejo = this;
	analiseTecnicaManejo.formularioAnaliseTecnica = null;
	analiseTecnicaManejo.analiseTecnica = null;
	analiseTecnicaManejo.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
	analiseTecnicaManejo.anexo = null;
	analiseTecnicaManejo.passos = {
		DADOS_IMOVEL: 'DADOS_IMOVEL',
		BASE_VETORIAL: 'BASE_VETORIAL',
		ANALISE_VETORIAL: 'ANALISE_VETORIAL',
		ANALISE_TEMPORAL: 'ANALISE_TEMPORAL',
		INSUMOS_UTILIZADOS: 'INSUMOS_UTILIZADOS',
		CALCULO_NDFI: 'CALCULO_NDFI',
		CALCULO_AREA_EFETIVA: 'CALCULO_AREA_EFETIVA',
		DETALHAMENTO_AREA_EFETIVA: 'DETALHAMENTO_AREA_EFETIVA',
		CONSIDERACOES: 'CONSIDERACOES',
		CONCLUSAO: 'CONCLUSAO'
	};

	analiseTecnicaManejo.passoAtual = analiseTecnicaManejo.passos.DADOS_IMOVEL;

	analiseTecnicaManejo.init = function() {

		processoManejoService.getAnalise($routeParams.idAnaliseManejo)
			.then(function (response) {

				analiseTecnicaManejo.analiseTecnica = response.data;

			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro obter ao dados do processo.");
			});

	};

	analiseTecnicaManejo.abrirModal = function() {

		var modalInstance = $uibModal.open({
			controller: 'modalObservacaoController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard: false,
			templateUrl: './features/analiseManejo/analiseTecnica/modal-observacao.html'
		});

		modalInstance.result.then(function (observacao) {

			observacao.analiseManejo = { id: analiseTecnicaManejo.analiseTecnica.id };
			observacao.passoAnalise = analiseTecnicaManejo.passoAtual;

			observacaoService.save(observacao).then(function (response) {

				switch(analiseTecnicaManejo.passoAtual) {

					case analiseTecnicaManejo.passos.DADOS_IMOVEL:
						analiseTecnicaManejo.analiseTecnica.observacoesDadosImovel.push(response.data);
						break;

					case analiseTecnicaManejo.passos.BASE_VETORIAL:
						analiseTecnicaManejo.analiseTecnica.observacoesBaseVetorial.push(response.data);
						break;

					case analiseTecnicaManejo.passos.ANALISE_VETORIAL:
						analiseTecnicaManejo.analiseTecnica.observacoesAnaliseVetorial.push(response.data);
						break;

					case analiseTecnicaManejo.passos.ANALISE_TEMPORAL:
						analiseTecnicaManejo.analiseTecnica.observacoesAnaliseTemporal.push(response.data);
						break;

					case analiseTecnicaManejo.passos.INSUMOS_UTILIZADOS:
						analiseTecnicaManejo.analiseTecnica.observacoesInsumosUtilizados.push(response.data);
						break;

					case analiseTecnicaManejo.passos.CALCULO_NDFI:
						analiseTecnicaManejo.analiseTecnica.observacoesCalculoNDFI.push(response.data);
						break;

					case analiseTecnicaManejo.passos.CALCULO_AREA_EFETIVA:
						analiseTecnicaManejo.analiseTecnica.observacoesCalculoAreaEfetiva.push(response.data);
						break;

					case analiseTecnicaManejo.passos.DETALHAMENTO_AREA_EFETIVA:
						analiseTecnicaManejo.analiseTecnica.observacoesDetalhamentoAreaEfetiva.push(response.data);
						break;

					case analiseTecnicaManejo.passos.CONSIDERACOES:
						analiseTecnicaManejo.analiseTecnica.observacoesConsideracoes.push(response.data);
						break;

					case analiseTecnicaManejo.passos.CONCLUSAO:
						analiseTecnicaManejo.analiseTecnica.observacoesConclusao.push(response.data);
						break;
				}

			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro ao salvar a observacao.");
			});
		});
	};

	analiseTecnicaManejo.upload = function (file) {
		if (file && !analiseTecnicaManejo.validacaoErro) {

			if (!file.$error) {

				if (analiseTecnicaManejo.anexo) {

					processoManejoService.removeAnexo(analiseTecnicaManejo.anexo.token)

						.then(function(response) {

							analiseTecnicaManejo.anexo = null;
							analiseTecnicaManejo.saveAnexo(file);

						}, function(error){

							mensagem.error(error.data.texto);
						});

				} else {

					analiseTecnicaManejo.saveAnexo(file);
				}
			}

		}
	};

	analiseTecnicaManejo.saveAnexo = function (file) {

		processoManejoService.saveAnexo($routeParams.idAnaliseManejo, file)

			.then(function(response) {

				analiseTecnicaManejo.anexo = {
					token: response.data,
					file: file
				};

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

	analiseTecnicaManejo.removeAnexo = function () {

		processoManejoService.removeAnexo(analiseTecnicaManejo.anexo.token)

			.then(function(response) {

				analiseTecnicaManejo.anexo = null;

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

};

exports.controllers.AnaliseTecnicaManejoController = AnaliseTecnicaManejoController;