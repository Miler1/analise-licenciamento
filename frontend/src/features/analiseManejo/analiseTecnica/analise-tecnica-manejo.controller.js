var AnaliseTecnicaManejoController = function($rootScope, $scope, $routeParams, processoManejoService, uploadService, $location, mensagem, $uibModal, observacaoService) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var analiseTecnicaManejo = this;
	analiseTecnicaManejo.formularioAnaliseTecnica = null;
	analiseTecnicaManejo.analiseTecnica = null;
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

	analiseTecnicaManejo.upload = function (file) {
		if (file && !analiseTecnicaManejo.validacaoErro) {

			if (!file.$error) {

				if (analiseTecnicaManejo.anexo) {

					var nameFile = analiseTecnicaManejo.processo.analiseManejo.pathShape.replace(/^.*[\\\/]/, '');

					uploadService.removeShape(nameFile)

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

	analiseGeoManejo.saveAnexo = function (file) {

		uploadService.saveAnexo(file)

			.then(function(response) {

				analiseTecnicaManejo.anexo = response.data;
				analiseTecnicaManejo.anexo.arquivo = file;

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

};

exports.controllers.AnaliseTecnicaManejoController = AnaliseTecnicaManejoController;