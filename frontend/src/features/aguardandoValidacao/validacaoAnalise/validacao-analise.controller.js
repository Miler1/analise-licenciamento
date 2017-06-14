var ValidacaoAnaliseController = function() {

    var validacaoAnalise = this;

	validacaoAnalise.perfis = app.utils.Perfis;
	validacaoAnalise.disabledFields = [app.DISABLED_FILTER_FIELDS.SITUACAO, app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO];
    
};

exports.controllers.ValidacaoAnaliseController = ValidacaoAnaliseController;