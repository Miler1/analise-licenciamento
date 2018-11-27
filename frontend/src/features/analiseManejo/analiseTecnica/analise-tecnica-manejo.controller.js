var AnaliseTecnicaManejoController = function($rootScope, $scope, $routeParams, analiseManejoService, $location, mensagem, $uibModal, observacaoService, $timeout) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var TAMANHO_MAXIMO_ARQUIVO_MB = 10;

	var analiseTecnicaManejo = this;
	analiseTecnicaManejo.formularioAnaliseTecnica = null;
	analiseTecnicaManejo.tipos = ['application/x-rar-compressed','application/zip','application/x-zip-compressed','multipart/x-zip', 'application/vnd.rar'];
	analiseTecnicaManejo.analiseTecnica = null;
	analiseTecnicaManejo.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
	analiseTecnicaManejo.anexos = [];
	analiseTecnicaManejo.passos = {
		DADOS_IMOVEL: ['DADOS_IMOVEL', 'observacoesDadosImovel', 'id-dados-imovel'],
		BASE_VETORIAL: ['BASE_VETORIAL', 'observacoesBaseVetorial', 'id-metodos-base-vetorial'],
		ANALISE_VETORIAL: ['ANALISE_VETORIAL', 'observacoesAnaliseVetorial', 'id-analise-vetorial-base-oficiais'],
		ANALISE_TEMPORAL: ['ANALISE_TEMPORAL', 'observacoesAnaliseTemporal', 'id-analise-temporal-imagens-satelite'],
		INSUMOS_UTILIZADOS: ['INSUMOS_UTILIZADOS', 'observacoesInsumosUtilizados', 'id-insumos-utilizados-analise-temporal'],
		CALCULO_NDFI: ['CALCULO_NDFI', 'observacoesCalculoNDFI', 'id-calculo-ndfi'],
		CALCULO_AREA_EFETIVA: ['CALCULO_AREA_EFETIVA', 'observacoesCalculoAreaEfetiva', 'id-calculo-area-efetiva-manejo'],
		DETALHAMENTO_AREA_EFETIVA: ['DETALHAMENTO_AREA_EFETIVA', 'observacoesDetalhamentoAreaEfetiva', 'id-detalhamento-area-efetiva-manejo'],
		CONSIDERACOES: ['CONSIDERACOES', 'observacoesConsideracoes', 'id-consideracoes'],
		DOCUMENTOS_COMPLEMENTARES: ['DOCUMENTOS_COMPLEMENTARES', 'observacoesDocumentosComplementares', 'id-documentos-complementares'],
		CONCLUSAO: ['CONCLUSAO', 'observacoesConclusao', 'id-conclusao']
	};
	analiseTecnicaManejo.listaPassos = [
		analiseTecnicaManejo.passos.DADOS_IMOVEL,
		analiseTecnicaManejo.passos.BASE_VETORIAL,
		analiseTecnicaManejo.passos.ANALISE_VETORIAL,
		analiseTecnicaManejo.passos.ANALISE_TEMPORAL,
		analiseTecnicaManejo.passos.INSUMOS_UTILIZADOS,
		analiseTecnicaManejo.passos.CALCULO_NDFI,
		analiseTecnicaManejo.passos.CALCULO_AREA_EFETIVA,
		analiseTecnicaManejo.passos.DETALHAMENTO_AREA_EFETIVA,
		analiseTecnicaManejo.passos.CONSIDERACOES,
		analiseTecnicaManejo.passos.DOCUMENTOS_COMPLEMENTARES,
		analiseTecnicaManejo.passos.CONCLUSAO
	];
	analiseTecnicaManejo.index = 0;

	analiseTecnicaManejo.passoAtual = analiseTecnicaManejo.passos.DADOS_IMOVEL;

	analiseTecnicaManejo.init = function() {

		analiseManejoService.getById($routeParams.idAnaliseManejo)
			.then(function (response) {

				analiseTecnicaManejo.analiseTecnica = response.data;

				analiseTecnicaManejo.analiseTecnica.totalAnaliseNDFI = 0;

				if (analiseTecnicaManejo.analiseTecnica.pathAnexo) {

					analiseTecnicaManejo.anexo = {file: { name: analiseTecnicaManejo.analiseTecnica.pathAnexo.substring(analiseTecnicaManejo.analiseTecnica.pathAnexo.lastIndexOf('/') + 1) } };
				}

				_.forEach(analiseTecnicaManejo.analiseTecnica.analisesNdfi, function(analise) {

					analiseTecnicaManejo.analiseTecnica.totalAnaliseNDFI += analise.area;
				});
			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro ao obter dados do processo.");
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

			observacao.analiseTecnicaManejo = { id: analiseTecnicaManejo.analiseTecnica.id };
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

	analiseTecnicaManejo.selecionarArquivo = function (files, file, anexo) {

		if (file) {

			if (analiseTecnicaManejo.tipos.indexOf(file.type) === -1 || file.name.substring(file.name.lastIndexOf('.')) !== '.zip') {

				mensagem.error("Extensão de arquivo inválida.");
				return;
			}

			if ((file.size / Math.pow(1000,2)) > analiseTecnicaManejo.TAMANHO_MAXIMO_ARQUIVO_MB) {

				mensagem.error("O arquivo deve ter um tamanho menor que " + TAMANHO_MAXIMO_ARQUIVO_MB + " MB.");
				return;
			}

			analiseTecnicaManejo.upload(file, anexo);
		}
	};

	analiseTecnicaManejo.upload = function (file) {

		if (file && !analiseTecnicaManejo.validacaoErro) {

			if (!file.$error) {

				if (analiseTecnicaManejo.analiseTecnica.id) {

					analiseManejoService.upload(file, analiseTecnicaManejo.analiseTecnica.id)
					.then(function(response) {

						if(analiseTecnicaManejo.analiseTecnica.documentosImovel.length < 2 ){

							analiseTecnicaManejo.anexos[0] = response.data;

						} else if (analiseTecnicaManejo.anexos.length === 1){

							analiseTecnicaManejo.anexos[1] = response.data;
							analiseTecnicaManejo.analiseTecnica.documentosImovel.push(response.data);
						}

					}, function(error){

						mensagem.error(error.data.texto);
					});
				}
			}
		}
	};

	analiseTecnicaManejo.removeAnexo = function (id, index) {

		analiseManejoService.removeAnexo(id)

			.then(function(response) {

				analiseTecnicaManejo.analiseTecnica.documentosImovel.splice(index, 1);

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

	analiseTecnicaManejo.downloadArquivo = function(idDocumento) {

		analiseManejoService.downloadDocumento(idDocumento);
	};

	analiseTecnicaManejo.selecionarDocumentoComplementar = function (file) {

		var extensoesPermitidas = [".zip", ".png", ".jpg", ".jpeg", ".bmp", ".pdf"];

		if (file) {

			var extensao = file.name.substring(file.name.lastIndexOf('.'));

			if (analiseTecnicaManejo.tipos.indexOf(file.type) === -1 || !extensao.includes(extensao)) {

				mensagem.error("Extensão de arquivo inválida.");
				return;
			}

			if ((file.size / Math.pow(1000,2)) > analiseTecnicaManejo.TAMANHO_MAXIMO_ARQUIVO_MB) {

				mensagem.error("O arquivo deve ter um tamanho menor que " + TAMANHO_MAXIMO_ARQUIVO_MB + " MB.");
				return;
			}

			analiseTecnicaManejo.uploadDocumentoComplementar(file);
		}
	};

	analiseTecnicaManejo.uploadDocumentoComplementar = function (file) {

		if (file && !analiseTecnicaManejo.validacaoErro) {

			if (!file.$error) {

				if (analiseTecnicaManejo.analiseTecnica.id) {

					analiseManejoService.uploadDocumentoComplementar(file, analiseTecnicaManejo.analiseTecnica.id)
						.then(function(response) {

							analiseTecnicaManejo.analiseTecnica.documentosComplementares.push(response.data);

						}, function(error){

							mensagem.error(error.data.texto);
						});
				}
			}
		}
	};

	analiseTecnicaManejo.removeDocumentoComplementar = function (id) {

		analiseManejoService.removeAnexo(id)

			.then(function(response) {

				analiseTecnicaManejo.analiseTecnica.documentosComplementares.forEach(function (documento, index) {

					if (documento.id == id) {

						analiseTecnicaManejo.analiseTecnica.documentosComplementares.splice(index, 1);
					}
				});

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

	analiseTecnicaManejo.sair = function() {

		$location.path('/analise-manejo');
	};

	analiseTecnicaManejo.voltar = function() {

		var aux = analiseTecnicaManejo.index;

		analiseTecnicaManejo.index -= 1;

		if(aux === 2 || aux === 4 || aux === 5) {

			analiseManejoService.atualizarDadosPdf(analiseTecnicaManejo.analiseTecnica, index);
		}

		analiseTecnicaManejo.passoAtual = analiseTecnicaManejo.listaPassos[analiseTecnicaManejo.index];
		click(document.getElementById(analiseTecnicaManejo.passoAtual[2]));
	};

	analiseTecnicaManejo.proximo = function() {

		var aux = analiseTecnicaManejo.index;

		analiseTecnicaManejo.index += 1;

		if(aux === 2 || aux === 4 || aux === 5) {

			analiseManejoService.atualizarDadosPdf(analiseTecnicaManejo.analiseTecnica, index);
		}

		analiseTecnicaManejo.passoAtual = analiseTecnicaManejo.listaPassos[analiseTecnicaManejo.index];
		click(document.getElementById(analiseTecnicaManejo.passoAtual[2]));
	};

	// Função usada para impedir o erro '$apply already in progress'
	function click(elemento) {

		$timeout(function(){
			elemento.click();
		});
	}

	analiseTecnicaManejo.changeTab = function(index) {

		var aux = analiseTecnicaManejo.index;

		analiseTecnicaManejo.index = index;

		if(aux === 2 || aux === 4 || aux === 5) {

			analiseManejoService.atualizarDadosPdf(analiseTecnicaManejo.analiseTecnica, index);
		}
		analiseTecnicaManejo.passoAtual = analiseTecnicaManejo.listaPassos[index];
	};

	analiseTecnicaManejo.confirmar = function() {

		analiseManejoService.finalizar($routeParams.idAnaliseManejo)
			.then(function (response) {

				mensagem.success(response.data.texto);
				$location.path('/analise-manejo');

			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro ao finalizar a análise do manejo.");
			});
	};

	$rootScope.$on('$locationChangeStart', function () {

		var aux = analiseTecnicaManejo.index;

		if(aux === 2 || aux === 4 || aux === 5) {

			analiseManejoService.atualizarDadosPdf(analiseTecnicaManejo.analiseTecnica, aux);
		}

	});

};

exports.controllers.AnaliseTecnicaManejoController = AnaliseTecnicaManejoController;
