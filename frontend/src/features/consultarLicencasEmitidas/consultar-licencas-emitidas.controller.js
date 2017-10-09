var ConsultarLicencasEmitidasController = function($scope, config, $rootScope, processoService, licencaEmitidaService, licencaService, $uibModal, mensagem) {

	$rootScope.tituloPagina = 'CONSULTAR LICENCAS EMITIDAS';

	var consultarLicencas = this;

	consultarLicencas.atualizarListaLicencas = atualizarListaLicencas;
	consultarLicencas.atualizarPaginacao = atualizarPaginacao;
	consultarLicencas.onPaginaAlterada = onPaginaAlterada;
	consultarLicencas.visualizarProcesso = visualizarProcesso;
	consultarLicencas.downloadLicenca = downloadLicenca;
	consultarLicencas.recuperarInfoLicenca = recuperarInfoLicenca;
	consultarLicencas.isSuspensaoVisivel = isSuspensaoVisivel;
	consultarLicencas.isCancelamentoVisivel = isCancelamentoVisivel;	

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

	function recuperarInfoLicenca(licenca, isSuspensao) {

		var licencaRecuperada = null;

		licencaService.findInfoLicenca(licenca.idLicenca)
			.then(function(response) {

				if(response.data.licencaAnalise == null){
					licencaRecuperada = response.data;
					licencaRecuperada.numeroProcesso = licenca.numeroProcesso;
					if(isSuspensao){
						return licencaEmitidaService.modalInfoSuspensao(licencaRecuperada);
					} else {
						return licencaEmitidaService.modalInfoCancelamento(licencaRecuperada);
					}
				}
				licencaRecuperada = response.data.licencaAnalise;
				licencaRecuperada.caracterizacao = response.data.caracterizacao;
				licencaRecuperada.dataCadastro = response.data.dataCadastro;
				licencaRecuperada.dataValidade = response.data.dataValidade;
				licencaRecuperada.id = response.data.id;
				licencaRecuperada.nome = response.data.caracterizacao.tipoLicenca.nome;
				licencaRecuperada.numeroProcesso = licenca.numeroProcesso;
				licencaRecuperada.tipoLicenca = licenca.tipoLicenca;

				if(isSuspensao){
					return licencaEmitidaService.modalInfoSuspensao(licencaRecuperada);
				} else {
					return licencaEmitidaService.modalInfoCancelamento(licencaRecuperada);
				}

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
					LICENCIAMENTO_CONFIG.usuarioSessao.autenticadoViaToken) {
					return true;
				}

				return false;
			}
};

exports.controllers.ConsultarLicencasEmitidasController = ConsultarLicencasEmitidasController;