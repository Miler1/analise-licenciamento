var CaixaEntradaController = function($scope, $rootScope) {

	var caixaEntrada = this;
	
	caixaEntrada.perfis = app.utils.Perfis;
	caixaEntrada.disabledFields = [
		app.DISABLED_FILTER_FIELDS.SITUACAO, 
		app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO,
		app.DISABLED_FILTER_FIELDS.ANALISTA_TECNICO,
		app.DISABLED_FILTER_FIELDS.COORDENADORIA,
		app.DISABLED_FILTER_FIELDS.CONSULTOR_JURIDICO,
		app.DISABLED_FILTER_FIELDS.ANALISTA_GEO
	];

};

exports.controllers.CaixaEntradaController = CaixaEntradaController;
