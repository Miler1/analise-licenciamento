var AguardandoValidacaoListagemController = function() {

    var aguardandoValidacaoListagem = this;

	aguardandoValidacaoListagem.perfis = app.utils.Perfis;
	aguardandoValidacaoListagem.disabledFields = [
		app.DISABLED_FILTER_FIELDS.SITUACAO, 
		app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO
	];    
};

exports.controllers.AguardandoValidacaoListagemController = AguardandoValidacaoListagemController;