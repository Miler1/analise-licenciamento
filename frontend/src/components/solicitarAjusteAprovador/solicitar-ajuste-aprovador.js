var SoliciarAjusteAprovador = {

    bindings: {

        analise: '<'
    },
    controller: function (coordenadorService, mensagem, analiseTecnicaService) {

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
                    .then(function (response) {
                        mensagem.success(response.data.texto);
                    }, function (error) {
                        mensagem.error(error.data.texto);
                    });

            } else {

                analise.id = ctrl.analise.analiseJuridica.id;
                analiseJuridicaService.solicitarAjusteAprovador(analise)
                    .then(function (response) {
                        mensagem.success(response.data.texto);
                    }, function (error) {
                        mensagem.error(error.data.texto);
                    });
            }
        }
    },

    templateUrl: 'components/solicitarAjusteAprovador/solicitarAjusteAprovador.html'
};

exports.directives.SoliciarAjusteAprovador = SoliciarAjusteAprovador;
