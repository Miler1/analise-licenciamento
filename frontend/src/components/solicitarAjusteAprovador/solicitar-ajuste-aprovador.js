var SoliciarAjusteAprovador = {

	bindings: {

		analise: '<',
		analiseJuridica: '<',
		analiseTecnica: '<'
	},
	controller: function ($location, coordenadorService, mensagem, analiseTecnicaService, analiseJuridicaService) {

		var ctrl = this;

		ctrl.perfis = app.utils.Perfis;
		ctrl.solicitacao = {};

		ctrl.listarCoordenador = listarCoordenador;
		ctrl.solicitar = solicitar;

		function listarCoordenador() {

			coordenadorService.getCoordenador(ctrl.solicitacao.tipo, ctrl.analise.processo.id)
				.then(function (response) {

					ctrl.coordenadores = response.data;
				});
		}

		function sucesso(response) {
			mensagem.success(response.data.texto);
			$location.path('/aguardando-assinatura');
		}

		function erro(error) {
			mensagem.error(error.data.texto);
		}

		function solicitar() {

			ctrl.formulario.$setSubmitted();

			if (!ctrl.formulario.$valid) {

				mensagem.error('Preencha os campos destacados em vermelho para prosseguir com a solicitação.');
				return;
			}

			var analise = {
				parecerValidacaoAprovador: ctrl.solicitacao.justificativa,
				usuarioValidacao: ctrl.solicitacao.coordenador,
				tipoResultadoValidacao: { id: app.utils.TiposResultadoAnalise.SOLICITAR_AJUSTES },
				tipoResultadoValidacaoAprovador: { id: app.utils.TiposResultadoAnalise.SOLICITAR_AJUSTES }
			};

			if (ctrl.solicitacao.tipo === ctrl.perfis.COORDENADOR_TECNICO) {

				analise.id = ctrl.analise.analiseTecnica.id;
				analiseTecnicaService.solicitarAjusteAprovador(analise)
					.then(sucesso, erro);

			} else {

				analise.id = ctrl.analise.analiseJuridica.id;
				analiseJuridicaService.solicitarAjusteAprovador(analise)
					.then(sucesso, erro);
			}
		}
	},

	templateUrl: 'components/solicitarAjusteAprovador/solicitarAjusteAprovador.html'
};

exports.directives.SoliciarAjusteAprovador = SoliciarAjusteAprovador;
