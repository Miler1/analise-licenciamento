var FiltroProcessosManejo = {

	bindings: {
		paginacao: '=',
		atualizarLista: '=',
		atualizarPaginacao: '=',
		pesquisarAoInicializar: '<'
	},

	controller: function() {

		var ctrl = this;

		ctrl.openedAccordion = false;
		ctrl.municipios = [];
		ctrl.tipologias = [];
		ctrl.atividades = [];
		ctrl.gerentesTecnicos = [];
		ctrl.analistasTecnicos = [];
		ctrl.manejosDigitais = [];
		ctrl.statusLicenca = [];


		this.pesquisar = function(pagina){

			ctrl.filtro.paginaAtual = pagina || ctrl.paginacao.paginaAtual;
			ctrl.filtro.itensPorPagina = ctrl.paginacao.itensPorPagina;
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

			if (ctrl.pesquisarAoInicializar){

				ctrl.pesquisar(1);
			}
		};
	},

	templateUrl: 'components/filtroProcessosManejo/filtroProcessosManejo.html'

};

exports.directives.FiltroProcessosManejo = FiltroProcessosManejo;