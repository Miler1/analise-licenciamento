var CondicaoTramitacao = {

	AGUARDANDO_VINCULACAO_JURIDICA: 1,
	AGUARDANDO_ANALISE_JURIDICA: 2,
	EM_ANALISE_JURIDICA: 3,
	NOTIFICADO: 4,
	AGUARDANDO_VALIDACAO_JURIDICA: 5,
	ARQUIVADO: 6,
	AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE: 7,
	AGUARDANDO_ANALISE_TECNICA: 8,
	EM_ANALISE_TECNICA: 9,
	AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE: 10,
	AGUARDANDO_ASSINATURA_DIRETOR: 11,
	AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR: 12,
	AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR: 13

};

exports.utils.CondicaoTramitacao = CondicaoTramitacao;