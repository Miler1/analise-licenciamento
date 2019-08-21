var ParecerOrgaoController = function(mensagem) {

	var parecerOrgao = this;

	parecerOrgao.showMensagem = function () {

		mensagem.success('Carta imagem em construção ...');
	};

};

exports.controllers.ParecerOrgaoController = ParecerOrgaoController;