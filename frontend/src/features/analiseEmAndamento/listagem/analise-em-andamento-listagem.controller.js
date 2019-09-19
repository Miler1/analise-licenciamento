var AnaliseEmAndamentoListagemController = function() {

    var analiseEmAndamentoListagem = this;

	analiseEmAndamentoListagem.perfis = app.utils.Perfis;
	analiseEmAndamentoListagem.disabledFields = [
		app.DISABLED_FILTER_FIELDS.SITUACAO,
		app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO,
		app.DISABLED_FILTER_FIELDS.GERENCIA,
		app.DISABLED_FILTER_FIELDS.COORDENADORIA,
		app.DISABLED_FILTER_FIELDS.CONSULTOR_JURIDICO
	];    
};

exports.controllers.AnaliseEmAndamentoListagemController = AnaliseEmAndamentoListagemController;