var ResumoEmpreendimento = {

	bindings: {
		empreendimento: '='
	},

	controller: function($timeout) {

		var ctrl = this;

		ctrl.enderecoEmpreendedorPrincipal = null;
		ctrl.enderecoEmpreendedorCorrespondencia = null;
		ctrl.geo = null;
		
		this.$onInit = function() {

			$timeout(function(){
				
				init();
			});

		};

		function init() {

			ctrl.enderecoEmpreendedorPrincipal = getEnderecoEmpreendedor(false);
			ctrl.enderecoEmpreendedorCorrespondencia = getEnderecoEmpreendedor(true);

		}

		function getEnderecoEmpreendedor(isCorrespondencia) {

			if(ctrl.empreendimento) {

				return _.find(ctrl.empreendimento.empreendimentoEU.empreendedor.pessoa.enderecos, {correspondencia: isCorrespondencia});
			}
		}

	},

	templateUrl: 'components/resumoEmpreendimento/resumoEmpreendimento.html'
};

exports.directives.ResumoEmpreendimento = ResumoEmpreendimento;