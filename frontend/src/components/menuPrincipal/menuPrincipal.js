var MenuPrincipal = {

	bindings: {

		itens: '<'
	},
	controller: function($location, mensagem, processoService, $timeout, $scope, $rootScope) {

		var ctrl = this;

		ctrl.isPerfilLicenciamento = false;

		var permissoes = ['LISTAR_PROCESSO', 'VINCULAR_PROCESSO_JURIDICO', 'CONSULTAR_PROCESSO', 'VALIDAR_PARECER_JURIDICO',
			'VINCULAR_PROCESSO_TECNICO', 'INICIAR_PARECER_JURIDICO', 'INICIAR_PARECER_TECNICO', 'APROVAR_ANALISE', 'VALIDAR_PARECER_TECNICO',
			'VALIDAR_PARECERES_JURIDICO_TECNICO', 'CONSULTAR_LICENCAS_EMITIDAS', 'SUSPENDER_LICENCA_EMITIDA', 'CANCELAR_LICENCA_EMITIDA'];

		_.forEach($rootScope.usuarioSessao.permissoes, function(permissao) {

			if(permissoes.includes(permissao)) {

				ctrl.isPerfilLicenciamento = true;
			}
		});

		this.$onInit = function() {

			if (ctrl.isPerfilLicenciamento) {

				atualizarContagemProcessos();
			}
		};

		this.irPara = function(url) {

			$location.path(url);
		};

		function atualizarContagemProcessos() {

			for (var i = 0; i < ctrl.itens.length; i++) {
				if(ctrl.itens[i].condicaoTramitacao && ctrl.itens[i].countItens && ctrl.itens[i].visivel())
					countProcessos(ctrl.itens[i]);
			}
		}

		function countProcessos(item) {

			var filtro = {};

			if(_.isFunction(item.condicaoTramitacao) && _.isArray(item.condicaoTramitacao())) {

				filtro.listaIdCondicaoTramitacao = item.condicaoTramitacao();

			} else if(_.isFunction(item.condicaoTramitacao))

				filtro.idCondicaoTramitacao = item.condicaoTramitacao();

			else
				filtro.idCondicaoTramitacao = item.condicaoTramitacao;

			if(_.isFunction(item.deveFiltrarPorUsuario))
				filtro.filtrarPorUsuario = item.deveFiltrarPorUsuario();
			else
				filtro.filtrarPorUsuario = item.deveFiltrarPorUsuario;

			if (_.isFunction(item.codigoPerfilSelecionado)){

				var codigoPerfilSelecionado = item.codigoPerfilSelecionado();

				filtro.isAnaliseJuridica = isAnaliseJuridica(codigoPerfilSelecionado);
				filtro.isAnaliseTecnica = isAnaliseTecnica(codigoPerfilSelecionado);
				filtro.isAnaliseGeo = isAnaliseGeo(codigoPerfilSelecionado);
				filtro.isCoordenador = isCoordenador(codigoPerfilSelecionado);
				filtro.isDiretor = isDiretor(codigoPerfilSelecionado);
				filtro.isDiretor = isSecretario(codigoPerfilSelecionado);
			}

			processoService.getProcessosCount(filtro)
				.then(function(response){
					 item.totalItens = response.data;
				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a quantidade de protocolos.");
				});
		}

		function isAnaliseJuridica(codigoPerfilSelecionado) {

			return codigoPerfilSelecionado === app.utils.Perfis.COORDENADOR_JURIDICO ||
				codigoPerfilSelecionado === app.utils.Perfis.CONSULTOR_JURIDICO;
		}

		function isAnaliseTecnica(codigoPerfilSelecionado) {

			return codigoPerfilSelecionado === app.utils.Perfis.COORDENADOR_TECNICO ||
				codigoPerfilSelecionado === app.utils.Perfis.ANALISTA_TECNICO;
		}

		function isAnaliseGeo(codigoPerfilSelecionado) {

			return codigoPerfilSelecionado === app.utils.Perfis.ANALISTA_GEO;
		}

		function isCoordenador(codigoPerfilSelecionado) {

			return codigoPerfilSelecionado === app.utils.Perfis.COORDENADOR;

		}

		function isDiretor(codigoPerfilSelecionado) {

			return codigoPerfilSelecionado === app.utils.Perfis.DIRETOR;

		}

		function isSecretario(codigoPerfilSelecionado) {

			return codigoPerfilSelecionado === app.utils.Perfis.SECRETARIO;

		}

		$scope.$on('atualizarContagemProcessos', function(event){

			atualizarContagemProcessos();
		});
	},
	templateUrl: 'components/menuPrincipal/menuPrincipal.html'
};

exports.directives.MenuPrincipal = MenuPrincipal;
