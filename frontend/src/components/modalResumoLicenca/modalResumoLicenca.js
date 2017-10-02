var ModalResumoLicenca = {
	
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

			ctrl.$onInit = function() {

				ctrl.condicionantes = ctrl.resolve.dadosLicenca.condicionantes || [];
				ctrl.recomendacoes = ctrl.resolve.dadosLicenca.recomendacoes || [];
				ctrl.observacoes = ctrl.resolve.dadosLicenca.observacoes || [];
				inicializaValidadesPossiveis();
			};
	
			ctrl.confirmarDadosLicenca = function() {
	
				var dadosLicenca = ctrl.resolve.dadosLicenca;
	
				cancelarEdicoes();
	
				dadosLicenca.condicionantes = ctrl.condicionantes;
	
				dadosLicenca.recomendacoes = ctrl.recomendacoes;
	
				dadosLicenca.observacoes = ctrl.observacoes;
	
				ctrl.close({$value: dadosLicenca});
			};
	
			function cancelarEdicoes() {
				
				ctrl.recomendacoes.forEach(function(recomendacao){
	
					recomendacao.editing = false;
				});
	
				ctrl.condicionantes.forEach(function(condicionante){
	
					condicionante.editingTexto = false;
					condicionante.editingPrazo = false;
					
				});			
			}

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
		templateUrl: 'components/ModalResumoLicenca/ModalResumoLicenca.html'
	};
	
	exports.directives.ModalResumoLicenca = ModalResumoLicenca;