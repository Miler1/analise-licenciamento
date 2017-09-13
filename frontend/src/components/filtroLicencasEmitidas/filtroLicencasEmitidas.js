var FiltroLicencasEmitidas = {

	bindings: {
		paginacao: '=',
		atualizarLista: '=',
		atualizarPaginacao: '=',
		pesquisarAoInicializar: '<',
		onAfterUpdate: '=',
		isAprovadorLogado: '<'
	},

	controller: function(mensagem, licencaService, municipioService, atividadeService, 
		$scope, $rootScope, tipoLicencaService) {

		var ctrl = this;

		ctrl.openedAccordionPesquisaRapida = false;
		ctrl.openedAccordionPesquisaAvancada = false;
		ctrl.municipios = [];
		ctrl.atividades = [];
		ctrl.tiposLicencas = [];

		ctrl.maxDataInicio = new Date();

		this.pesquisaRapida = function(pagina){
			
			licencaService.getLicencasPesquisaRapida(ctrl.filtro)
				.then(function(response){

					ctrl.atualizarLista(response.data);

					if (_.isFunction(ctrl.onAfterUpdate))
						ctrl.onAfterUpdate(ctrl.filtro);

				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a lista de licenças emitidas.");
				});

				licencaService.getLicencasPesquisaRapidaCount(ctrl.filtro)
				.then(function(response){
					 ctrl.atualizarPaginacao(response.data, ctrl.filtro.paginaAtual);
				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a quantidade de licencas.");
				});
		};

		this.pesquisaAvancada = function(pagina){

			licencaService.getLicencasPesquisaAvancada(ctrl.filtro)
				.then(function(response){

					ctrl.atualizarLista(response.data);

					if (_.isFunction(ctrl.onAfterUpdate))
						ctrl.onAfterUpdate(ctrl.filtro);

				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a lista de licenças emitidas.");
				});

				licencaService.getLicencasPesquisaAvancadaCount(ctrl.filtro)
				.then(function(response){
					 ctrl.atualizarPaginacao(response.data, ctrl.filtro.paginaAtual);
				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a quantidade de processos.");
				});

			$rootScope.$broadcast('atualizarContagemProcessos');
		};

		this.pesquisar = function(pagina) {
			
			if (ctrl.filtro.periodoInicial && ctrl.filtro.periodoInicial) {
				
				var diff = moment(ctrl.filtro.periodoFinal, 'DD/MM/yyyy')
					.diff(moment(ctrl.filtro.periodoInicial, 'DD/MM/yyyy'), 'days');

				if (diff < 0) {
					mensagem.warning("O período inicial não pode ser maior que o período final.");
					return false;
				}
			}

			ctrl.filtro.paginaAtual = pagina || ctrl.paginacao.paginaAtual;
			ctrl.filtro.itensPorPagina = ctrl.paginacao.itensPorPagina;

			if (ctrl.openedAccordionPesquisaAvancada) {
				this.pesquisaAvancada(pagina);
			} else {
				this.pesquisaRapida(pagina);
			}
		};

		function setFiltrosPadrao(){

			ctrl.filtro = {};
		}

		this.limparFiltros = function(){

			setFiltrosPadrao();

			$('#cpfCnpjEmpreendimento').val('');

			this.pesquisar(1);
		};

		this.$postLink = function(){

			setFiltrosPadrao();

			municipioService.getMunicipiosByUf('PA').then(
				function(response){
					
					ctrl.municipios = response.data; 
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de municípios.');
				});

			atividadeService.getAtividades().then(
				function(response){
					
					ctrl.atividades = response.data;
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de atividades.');
				});

				tipoLicencaService.getTiposLicencas().then(
				function(response){
					
					ctrl.tiposLicencas = response.data;
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de licencas.');
				});

			if (ctrl.pesquisarAoInicializar){

				ctrl.pesquisar(1);
			}
		};

		$scope.$on('pesquisarLicencas', function(event){

			ctrl.pesquisar();
		});
	},

	templateUrl: 'components/filtroLicencasEmitidas/filtroLicencasEmitidas.html'

};

exports.directives.FiltroLicencasEmitidas = FiltroLicencasEmitidas;