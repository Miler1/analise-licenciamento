var ConsultarLicencasEmitidasController = function($scope, config, $rootScope, processoService,
	licencaEmitidaService, licencaService, $uibModal, mensagem, dispensaLicencaService) {

	$rootScope.tituloPagina = 'CONSULTAR TÍTULOS EMITIDOS';

	var consultarLicencas = this;

	consultarLicencas.atualizarListaLicencas = atualizarListaLicencas;
	consultarLicencas.atualizarPaginacao = atualizarPaginacao;
	consultarLicencas.onPaginaAlterada = onPaginaAlterada;
	consultarLicencas.visualizarProcesso = visualizarProcesso;
	consultarLicencas.downloadLicenca = downloadLicenca;
	consultarLicencas.recuperarInfoLicenca = recuperarInfoLicenca;
	consultarLicencas.isSuspensaoVisivel = isSuspensaoVisivel;
	consultarLicencas.isCancelamentoVisivel = isCancelamentoVisivel;
	consultarLicencas.ajustarTamanhoContainer = ajustarTamanhoContainer;

	consultarLicencas.licencas = [];
	consultarLicencas.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	consultarLicencas.TIPOS_CARACTERIZACOES = app.TIPOS_CARACTERIZACOES;

	function atualizarListaLicencas(licencas) {

		consultarLicencas.licencas = licencas;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		consultarLicencas.paginacao.update(totalItens, paginaAtual);
	}

	function pesquisar() {
		$scope.$broadcast('pesquisarLicencas');
	}

	function onPaginaAlterada(){

		pesquisar();
	}

	function visualizarProcesso(licenca) {

		return processoService.visualizarProcesso(licenca);
	}

	function recuperarInfoLicenca(licenca, isSuspensao) {

		if (licenca.origemLicenca === app.ORIGEM_LICENCA.DISPENSA)
			preparaDLAParaSuspensaoOuCancelamento(licenca);
		else
			preparaLicencaParaSuspensaoOuCancelamento(licenca, isSuspensao);

	}

	function preparaLicencaParaSuspensaoOuCancelamento(licenca, isSuspensao) {

		var licencaRecuperada = null;

		licencaService.findInfoLicenca(licenca.idLicenca)
			.then(function(response) {

				if(response.data.licencaAnalise == null){

					licencaRecuperada = response.data;

				} else {

					licencaRecuperada = response.data.licencaAnalise;
					licencaRecuperada.caracterizacao = response.data.caracterizacao;
					licencaRecuperada.dataCadastro = response.data.dataCadastro;
					licencaRecuperada.dataValidade = response.data.dataValidade;
					licencaRecuperada.id = response.data.id;
					licencaRecuperada.nome = response.data.caracterizacao.tipoLicenca.nome;
					licencaRecuperada.tipoLicenca = licenca.tipoLicenca;

				}

				licencaRecuperada.numeroProcesso = licenca.numeroProcesso;

				if(isSuspensao){
					return openModalInfoSuspensao(licencaRecuperada);
				} else {
					return openModalInfoCancelamento(licencaRecuperada);
				}

			}, function(error) {

				mensagem.error(error.data.texto);
			});

	}

	function preparaDLAParaSuspensaoOuCancelamento(dla) {

		var dlaRecuperada = null;

		dispensaLicencaService.findInfoDLA(dla.idLicenca)
			.then(function(response) {

				dlaRecuperada = response.data;
				dlaRecuperada.numeroProcesso = response.data.caracterizacao.numeroProcesso;
				dlaRecuperada.tipoLicenca = dla.origemLicenca;

				return openModalInfoCancelamento(dlaRecuperada);

			}, function(error) {

				mensagem.error(error.data.texto);
			});

	}

	function downloadLicenca(licenca) {

		if (licenca.origemLicenca === app.ORIGEM_LICENCA.DISPENSA) {

			licencaEmitidaService.downloadDla(licenca.idLicenca);
		} else {

			licencaEmitidaService.downloadLicenca(licenca.idLicenca);
		}
	}

	function isSuspensaoVisivel(licenca) {

		if((licenca.tipoCaracterizacao === consultarLicencas.TIPOS_CARACTERIZACOES.SIMPLIFICADO ||
			licenca.tipoCaracterizacao === consultarLicencas.TIPOS_CARACTERIZACOES.DECLARATORIO) &&
			(LICENCIAMENTO_CONFIG.usuarioSessao.perfilSelecionado.id === app.utils.Perfis.APROVADOR &&
				LICENCIAMENTO_CONFIG.usuarioSessao.autenticadoViaToken)) {
			return true;

		}

		return false;

	}

	function isCancelamentoVisivel(licenca) {

		if (LICENCIAMENTO_CONFIG.usuarioSessao.perfilSelecionado.id === app.utils.Perfis.APROVADOR &&
			LICENCIAMENTO_CONFIG.usuarioSessao.autenticadoViaToken && licenca.ativo) {
			return true;
		}

		return false;
	}

	function openModalInfoSuspensao(licencaRecuperada) {

		var modalInstance = $uibModal.open({

			component: 'modalVisualizarLicenca',
			size: 'lg',
			resolve: {

				dadosLicenca: function () {

					return licencaRecuperada;
				},
				isSuspensao: true
			}
		}).closed.then(function(){
			pesquisar();
		});
	}

	function openModalInfoCancelamento(licencaRecuperada) {

		var modalInstance = $uibModal.open({

			component: 'modalVisualizarLicenca',
			size: 'lg',
			resolve: {

				dadosLicenca: function () {

					return licencaRecuperada;
				},
				isCancelamento: true
			}
		}).closed.then(function(){
			pesquisar();
		});

	}

	function ajustarTamanhoContainer(isUltimo) {

		if(!isUltimo)
			return;

		var lastDropdown = angular.element(".last-dropdown");
		var ul = lastDropdown.find('ul');
		var container = angular.element("section.container.main-container");
		var containerPaddingBottom = parseInt(container.css("padding-bottom"), 10);

		setTimeout(function(){

			if(lastDropdown.hasClass("open")) {

				container.css("padding-bottom", (containerPaddingBottom + (ul.height()/2)));

			} else {
				container.css("padding-bottom", (containerPaddingBottom - (ul.height()/2)));
			}

		},0);
	}

};

exports.controllers.ConsultarLicencasEmitidasController = ConsultarLicencasEmitidasController;