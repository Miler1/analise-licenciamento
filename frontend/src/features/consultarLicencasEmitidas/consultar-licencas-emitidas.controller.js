var ConsultarLicencasEmitidasController = function($scope, config, $rootScope, processoService, licencaEmitidaService, licencaService, $uibModal, mensagem) {

	$rootScope.tituloPagina = 'CONSULTAR LICENCAS EMITIDAS';

	var consultarLicencas = this;

	consultarLicencas.atualizarListaLicencas = atualizarListaLicencas;
	consultarLicencas.atualizarPaginacao = atualizarPaginacao;
	consultarLicencas.onPaginaAlterada = onPaginaAlterada;
	consultarLicencas.visualizarProcesso = visualizarProcesso;
	consultarLicencas.downloadLicenca = downloadLicenca;
	consultarLicencas.recuperarInfoLicencaSuspender = recuperarInfoLicencaSuspender;
	consultarLicencas.isSuspensaoVisivel = isSuspensaoVisivel;

	consultarLicencas.licencas = [];
	consultarLicencas.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	consultarLicencas.TIPOS_CARACTERIZACOES = app.TIPOS_CARACTERIZACOES;

	function atualizarListaLicencas(licencas) {

		consultarLicencas.licencas = licencas;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		consultarLicencas.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarLicencas');
	}

	function visualizarProcesso(licenca) {

		return processoService.visualizarProcesso(licenca);
	}

	function recuperarInfoLicencaSuspender(licenca) {

		var licencaSuspender = null;

		licencaService.findInfoLicenca(licenca.idLicenca)
			.then(function(response) {

				if(response.data.licencaAnalise == null){
					licencaSuspender = response.data;
					return licencaEmitidaService.modalInfoLicencaSuspender(licencaSuspender);
				}
				licencaSuspender = response.data.licencaAnalise;
				licencaSuspender.numeroProcesso = response.data.licencaAnalise.caracterizacao.numeroProcesso;				
				licencaSuspender.caracterizacao = response.data.caracterizacao;
				licencaSuspender.dataCadastro = response.data.dataCadastro;
				licencaSuspender.dataValidade = response.data.dataValidade;
				licencaSuspender.id = response.data.id;

				return licencaEmitidaService.modalInfoLicencaSuspender(licencaSuspender);


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
};

exports.controllers.ConsultarLicencasEmitidasController = ConsultarLicencasEmitidasController;