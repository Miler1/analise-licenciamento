var ModalInformacoesLicenca = {

	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function(mensagem, $anchorScroll, $scope) {

		var ctrl = this;

		var inicializaValidadesPossiveis = function() {

			ctrl.validades = [];

			ctrl.validadeLicenca = ctrl.resolve.dadosLicenca.validade;

			var val = ctrl.validadeLicenca;

			while(val > 0) {

				ctrl.validades.push(val);

				val--;
			}
		};

		ctrl.$onInit =  function() {

			ctrl.condicionantes = ctrl.resolve.dadosLicenca.condicionantes || [];
			ctrl.recomendacoes = ctrl.resolve.dadosLicenca.recomendacoes || [];
			ctrl.observacoes = ctrl.resolve.dadosLicenca.observacoes || [];
			inicializaValidadesPossiveis();
		};

		ctrl.confirmarDadosLicenca = function() {

			var dadosLicenca = ctrl.resolve.dadosLicenca;

			cancelarEdicoes();

			dadosLicenca.condicionantes = ajustarOrdem(ctrl.condicionantes);

			dadosLicenca.recomendacoes = ajustarOrdem(ctrl.recomendacoes);

			dadosLicenca.observacoes = ctrl.observacoes;

			ctrl.close({$value: dadosLicenca});
		};

		function ajustarOrdem(lista) {

			for(i=0;i<lista.length; i++) {
				lista[i].ordem = i;
			}

			return lista;
		}

		function cancelarEdicoes() {
			
			ctrl.recomendacoes.forEach(function(recomendacao){

				recomendacao.editing = false;
			});

			ctrl.condicionantes.forEach(function(condicionante){

				condicionante.editingTexto = false;
				condicionante.editingPrazo = false;
				
			});			
		}

		ctrl.inserirCondicionante = function() {

			$scope.formularioCondicionante.$setSubmitted();

			if(!ctrl.condicionante || !ctrl.condicionante.texto) {
				mensagem.error("Insira a condicionante.", {referenceId: 1});

				$anchorScroll('informacoesLicenca');
				return;
			}

			if(!ctrl.condicionante.prazo) {
				mensagem.error("Prazo não informado ou acima de 5 anos.", {referenceId: 1});

				$anchorScroll('informacoesLicenca');
				return;
			}

			ctrl.condicionantes.push({
				texto : ctrl.condicionante.texto,
				prazo : ctrl.condicionante.prazo,
				ordem : ctrl.condicionantes.length
			});

			$scope.formularioCondicionante.$setPristine();

			ctrl.condicionante = {};
		};

		ctrl.inserirRecomendacao = function() {

			$scope.formularioRecomendacao.$setSubmitted();

			if(!ctrl.recomendacao || !ctrl.recomendacao.texto) {
				mensagem.error("Insira a recomendação.", {referenceId: 1});

				$anchorScroll('informacoesLicenca');
				return;
			}

			ctrl.recomendacoes.push({
				texto : ctrl.recomendacao.texto,
				ordem : ctrl.recomendacoes.length
			});

			$scope.formularioRecomendacao.$setPristine();

			ctrl.recomendacao = {};
		};

		ctrl.cancelar = function() {

			ctrl.recomendacoes = [];
			ctrl.condicionantes = [];
			ctrl.observacoes = [];

			ctrl.dismiss({$value: 'cancel'});
		};

		ctrl.editarCondicionante = function (item) {
			item.editingTexto = true;
			item.editingPrazo = true;
		};


		ctrl.editarTextoCondicionante = function (item) {
			item.editingTexto = true;
		};


		ctrl.editarPrazoCondicionante = function (item) {
			item.editingPrazo = true;
		};

		ctrl.doneEditingTextoCondicionante = function (item) {

			if(!item.texto){

				mensagem.error("Condicionante não informada.", {referenceId: 1});

				$anchorScroll('informacoesLicenca');
				return;
			}

			item.editingTexto = false;
		};

		ctrl.doneEditingPrazoCondicionante = function (item) {

			if(!item.prazo){

				mensagem.error("Prazo não informado ou acima de 5 anos.", {referenceId: 1});

				$anchorScroll('informacoesLicenca');
				return;
			}

			item.editingPrazo = false;
		};

		ctrl.doneEditingCondicionante = function (item) {
			item.editingTexto = false;
			item.editingPrazo = false;
		};

		ctrl.excluirCondicionante = function (item) {

			ctrl.condicionantes = _.remove(ctrl.condicionantes, function(cond) {
				return cond !== item;
			});
		};

		ctrl.excluirRecomendacao = function (item) {

			ctrl.recomendacoes = _.remove(ctrl.recomendacoes, function(rec) {
				return rec !== item;
			});
		};

		ctrl.editarRecomendacao = function (item) {
			item.editing = true;
		};

		ctrl.doneEditing = function (item) {

			if(!item.texto){

				mensagem.error("Recomendação não informada.", {referenceId: 1});

				$anchorScroll('informacoesLicenca');
				return;
			}

			item.editing = false;
		};

		ctrl.sortOptions = {

			containment: '#board'
		};

	},
	templateUrl: 'components/modalInformacoesLicenca/modalInformacoesLicenca.html'
};

exports.directives.ModalInformacoesLicenca = ModalInformacoesLicenca;