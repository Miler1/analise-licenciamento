var SolicitarAjusteAprovadorController = function (coordenadorService) {

	var ctrl = this;

	ctrl.solicitacao = {};

	ctrl.listarCoordenadores = listarCoordenadores;

	function listarCoordenadores() {

		coordenadorService.getCoordenadores(ctrl.solicitacao.tipo)
			.then(function (response) {

				ctrl.coordenadores = response.data;
			});
	}

};

exports.controllers.SolicitarAjusteAprovadorController = SolicitarAjusteAprovadorController;