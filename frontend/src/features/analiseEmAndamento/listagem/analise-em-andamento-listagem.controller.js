var AnaliseEmAndamentoListagemController = function() {

    var analiseEmAndamentoListagem = this;

	analiseEmAndamentoListagem.perfis = app.utils.Perfis;
	analiseEmAndamentoListagem.disabledFields = [
		app.DISABLED_FILTER_FIELDS.SITUACAO,
		app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO,
		app.DISABLED_FILTER_FIELDS.ANALISTA_TECNICO
	];    
};

exports.controllers.AnaliseEmAndamentoListagemController = AnaliseEmAndamentoListagemController;