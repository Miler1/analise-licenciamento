var AnaliseTecnicaManejoController = function($rootScope, $scope, $routeParams, processoManejoService, $location, mensagem, $uibModal, observacaoService) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var TAMANHO_MAXIMO_ARQUIVO_MB = 10;

	var analiseTecnicaManejo = this;
	analiseTecnicaManejo.formularioAnaliseTecnica = null;
	analiseTecnicaManejo.analiseTecnica = null;
	analiseTecnicaManejo.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
	analiseTecnicaManejo.anexo = null;
	analiseTecnicaManejo.passos = {
		DADOS_IMOVEL: ['DADOS_IMOVEL', 'observacoesDadosImovel'],
		BASE_VETORIAL: ['BASE_VETORIAL', 'observacoesBaseVetorial'],
		ANALISE_VETORIAL: ['ANALISE_VETORIAL', 'observacoesAnaliseVetorial'],
		ANALISE_TEMPORAL: ['ANALISE_TEMPORAL', 'observacoesAnaliseTemporal'],
		INSUMOS_UTILIZADOS: ['INSUMOS_UTILIZADOS', 'observacoesInsumosUtilizados'],
		CALCULO_NDFI: ['CALCULO_NDFI', 'observacoesCalculoNDFI'],
		CALCULO_AREA_EFETIVA: ['CALCULO_AREA_EFETIVA', 'observacoesCalculoAreaEfetiva'],
		DETALHAMENTO_AREA_EFETIVA: ['DETALHAMENTO_AREA_EFETIVA', 'observacoesDetalhamentoAreaEfetiva'],
		CONSIDERACOES: ['CONSIDERACOES', 'observacoesConsideracoes'],
		CONCLUSAO: ['CONCLUSAO', 'observacoesConclusao']
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
			observacao.passoAnalise = analiseTecnicaManejo.passoAtual[0];

			observacaoService.save(observacao).then(function (response) {

				analiseTecnicaManejo.analiseTecnica[analiseTecnicaManejo.passoAtual[1]].push(response.data);

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

	analiseTecnicaManejo.removerObservacao = function(observacao) {

		observacaoService.delete(observacao.id).then(function (response) {

			analiseTecnicaManejo.analiseTecnica[analiseTecnicaManejo.passoAtual[1]].splice(analiseTecnicaManejo.analiseTecnica[analiseTecnicaManejo.passoAtual[1]].indexOf(observacao), 1);

		})
		.catch(function (response) {

			if (!!response.data.texto)
				mensagem.warning(response.data.texto);

			else
				mensagem.error("Ocorreu um erro ao excluir a observacao.");
		});
	};

};

exports.controllers.AnaliseTecnicaManejoController = AnaliseTecnicaManejoController;