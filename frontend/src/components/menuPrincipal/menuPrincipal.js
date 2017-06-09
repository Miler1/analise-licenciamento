var MenuPrincipal = {

	bindings: {

		itens: '<'
	},
	controller: function($location, mensagem, processoService) {

		var ctrl = this;

		this.$onInit = function() {

			for (var i = 0; i < ctrl.itens.length; i++) {
				if(ctrl.itens[i].condicaoTramitacao)
					countProcessos(ctrl.itens[i]);
			}

		};

		this.irPara = function(url) {

			$location.path(url);
		};

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

			processoService.getProcessosCount(filtro)
				.then(function(response){
					 item.totalItens = response.data;
				})
				.catch(function(){
					mensagem.error("Ocorreu um erro ao buscar a quantidade de processos.");
				});

		}
	},
	templateUrl: 'components/menuPrincipal/menuPrincipal.html'
};

exports.directives.MenuPrincipal = MenuPrincipal;