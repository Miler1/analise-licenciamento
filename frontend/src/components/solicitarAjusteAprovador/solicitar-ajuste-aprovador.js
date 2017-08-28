var SoliciarAjusteAprovador = {

	bindings: {

		analise: '<'
	},
	controller: function (coordenadorService, mensagem) {

		var ctrl = this;

		ctrl.perfis = app.utils.Perfis;
		ctrl.solicitacao = {};

		ctrl.listarCoordenadores = listarCoordenadores;
		ctrl.solicitar = solicitar;

		function listarCoordenadores() {

			coordenadorService.getCoordenadores(ctrl.solicitacao.tipo, ctrl.analise.processo.id)
				.then(function (response) {

					ctrl.coordenadores = response.data;
				});
		}

		function solicitar() {

			ctrl.formulario.$setSubmitted();

			if(!ctrl.formulario.$valid) {

				mensagem.error('Preencha os campos destacados em vermelho para prosseguir com a solicitação.');
				return;
			}
		}
	},

	templateUrl: 'components/solicitarAjusteAprovador/solicitarAjusteAprovador.html'
};

exports.directives.SoliciarAjusteAprovador = SoliciarAjusteAprovador;
