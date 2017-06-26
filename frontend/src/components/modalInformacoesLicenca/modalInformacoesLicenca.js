var ModalInformacoesLicenca = {

	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function() {

		var ctrl = this;

		ctrl.$onInit =  function() {

			ctrl.condicionantes = [];
			ctrl.recomendacoes = [];
		};

		ctrl.confirmarDadosLicenca = function() {

			ctrl.close({$value: ctrl.condicionantes});
		};

		ctrl.inserirCondicionante = function() {

			ctrl.condicionantes.push({
				texto : ctrl.condicionante.texto,
				prazo : ctrl.condicionante.prazo,
				ordem : ctrl.condicionantes.length
			});

			ctrl.condicionante = {};
		};

		ctrl.inserirRecomendacao = function() {

			ctrl.recomendacoes.push({
				texto : ctrl.recomendacao.texto,
				ordem : ctrl.recomendacoes.length
			});


			ctrl.recomendacao = {};
		};

		ctrl.cancelar = function() {

			ctrl.dismiss({$value: 'cancel'});
		};


		ctrl.editarRecomendacao = function (item) {
			item.editing = true;
		};

		ctrl.doneEditing = function (item) {
			item.editing = false;
		};

		ctrl.sortOptions = {

			//restrict move across columns. move only within column.
			/*accept: function (sourceItemHandleScope, destSortableScope) {
			return sourceItemHandleScope.itemScope.sortableScope.$id === destSortableScope.$id;
			},*/
			itemMoved: function (event) {
			},
			orderChanged: function (event) {
				//alteraOrdemRecomendacoes(event.source.itemScope.modelValue.ordem, event.dest.itemScope.modelValue.ordem);
			},
			containment: '#board'
		};

	},
	templateUrl: 'components/modalInformacoesLicenca/modalInformacoesLicenca.html'
};

exports.directives.ModalInformacoesLicenca = ModalInformacoesLicenca;