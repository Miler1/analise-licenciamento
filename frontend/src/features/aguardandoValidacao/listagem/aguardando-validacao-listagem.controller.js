var AguardandoValidacaoListagemController = function() {

    var aguardandoValidacaoListagem = this;

	aguardandoValidacaoListagem.perfis = app.utils.Perfis;
	aguardandoValidacaoListagem.disabledFields = [
		app.DISABLED_FILTER_FIELDS.SITUACAO, 
		app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO,
		app.DISABLED_FILTER_FIELDS.COORDENADORIA,
		app.DISABLED_FILTER_FIELDS.CONSULTOR_JURIDICO
	];    
};

exports.controllers.AguardandoValidacaoListagemController = AguardandoValidacaoListagemController;