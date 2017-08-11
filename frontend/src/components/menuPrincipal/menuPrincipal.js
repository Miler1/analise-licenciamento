var MenuPrincipal = {

	bindings: {

		itens: '<'
	},
	controller: function($location, mensagem, processoService, $timeout, $scope) {

		var ctrl = this;

		this.$onInit = function() {

			atualizarContagemProcessos();
		};

		this.irPara = function(url) {

			$location.path(url);
		};

		function atualizarContagemProcessos() {
			
			for (var i = 0; i < ctrl.itens.length; i++) {
				if(ctrl.itens[i].condicaoTramitacao)
					countProcessos(ctrl.itens[i]);
			}
		}

		function countProcessos(item) {

			var filtro = {};

			if(_.isFunction(item.condicaoTramitacao))
				filtro.idCondicaoTramitacao = item.condicaoTramitacao();
			else
				filtro.idCondicaoTramitacao = item.condicaoTramitacao;

			if(_.isFunction(item.deveFiltrarPorUsuario))
				filtro.filtrarPorUsuario = item.deveFiltrarPorUsuario();
			else
				filtro.filtrarPorUsuario = item.deveFiltrarPorUsuario;

			if (_.isFunction(item.idPerfilSelecionado)){

				var idPerfilSelecionado = item.idPerfilSelecionado();

				filtro.isAnaliseJuridica = isAnaliseJuridica(idPerfilSelecionado);
				filtro.isAnaliseTecnica = isAnaliseTecnica(idPerfilSelecionado);
			}

			processoService.getProcessosCount(filtro)
				.then(function(response){
					 item.totalItens = response.data;
				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a quantidade de processos.");
				});
		}

		function isAnaliseJuridica(idPerfilSelecionado) {

			return idPerfilSelecionado === app.utils.Perfis.COORDENADOR_JURIDICO || 
				   idPerfilSelecionado === app.utils.Perfis.CONSULTOR_JURIDICO;
		}

		function isAnaliseTecnica(idPerfilSelecionado) {

			return idPerfilSelecionado === app.utils.Perfis.COORDENADOR_TECNICO || 
				   idPerfilSelecionado === app.utils.Perfis.ANALISTA_TECNICO ||
				   idPerfilSelecionado === app.utils.Perfis.GERENTE_TECNICO;
		}

		$scope.$on('atualizarContagemProcessos', function(event){

			atualizarContagemProcessos();
		});  
	},
	templateUrl: 'components/menuPrincipal/menuPrincipal.html'
};

exports.directives.MenuPrincipal = MenuPrincipal;