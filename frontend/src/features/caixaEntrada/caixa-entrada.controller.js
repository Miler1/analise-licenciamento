var CaixaEntradaController = function($scope, $rootScope) {

	var caixaEntrada = this;
	
	caixaEntrada.perfis = app.utils.Perfis;
	caixaEntrada.disabledFields = [
		app.DISABLED_FILTER_FIELDS.SITUACAO, 
		app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO,
		app.DISABLED_FILTER_FIELDS.ANALISTA_TECNICO
	];
};

exports.controllers.CaixaEntradaController = CaixaEntradaController;