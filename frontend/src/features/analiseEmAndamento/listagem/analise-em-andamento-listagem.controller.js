var AnaliseEmAndamentoListagemController = function() {

    var analiseEmAndamentoListagem = this;

	analiseEmAndamentoListagem.perfis = app.utils.Perfis;
	analiseEmAndamentoListagem.disabledFields = [app.DISABLED_FILTER_FIELDS.SITUACAO, app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO];
    
};

exports.controllers.AnaliseEmAndamentoListagemController = AnaliseEmAndamentoListagemController;