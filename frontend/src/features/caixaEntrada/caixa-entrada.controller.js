var CaixaEntradaController = function($scope, $rootScope) {

	var caixaEntrada = this;
	
	caixaEntrada.perfis = app.utils.Perfis;
	caixaEntrada.disabledFields = [
		app.DISABLED_FILTER_FIELDS.SITUACAO, 
		app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO,
		app.DISABLED_FILTER_FIELDS.COORDENADORIA,
		app.DISABLED_FILTER_FIELDS.CONSULTOR_JURIDICO
	];

};

exports.controllers.CaixaEntradaController = CaixaEntradaController;
