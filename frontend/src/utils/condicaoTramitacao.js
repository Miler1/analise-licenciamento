var CAIXA_ENTRADA_GERENTE = [ 27, 10, 30, 33 ];

var MENU_EM_ANALISE_GERENTE = [31, 36];
var CAIXA_ENTRADA_ANALISTA_GEO = [25, 37];

var CondicaoTramitacao = {

	AGUARDANDO_VINCULACAO_JURIDICA: 1,
	AGUARDANDO_ANALISE_JURIDICA: 2,
	EM_ANALISE_JURIDICA: 3,
	NOTIFICADO_PELO_ANALISTA_GEO: 4,
	AGUARDANDO_VALIDACAO_JURIDICA: 5,
	ARQUIVADO: 6,
	AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE: 7,
	AGUARDANDO_ANALISE_TECNICA: 8,
	EM_ANALISE_TECNICA: 9,
	AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE: 10,
	AGUARDANDO_ASSINATURA_APROVADOR: 11,
	AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR: 12,
	AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR: 13,
	LICENCA_EMITIDA: 14,
	SUSPENSO : 15,
	CANCELADO : 16,
	AGUARDANDO_VINCULACAO_GEO_PELO_GERENTE : 24,
	AGUARDANDO_ANALISE_GEO : 25,
	EM_ANALISE_GEO : 26,
	AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE : 27,
	AGUARDANDO_VALIDACAO_GERENTE : 28,
	AGUARDANDO_VALIDACAO_DIRETORIA : 29,
	SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO : 30,
	EM_ANALISE_GERENTE: 31,
	AGUARDANDO_RESPOSTA_COMUNICADO : 32,
	SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA : 33,
	ANALISE_FINALIZADA: 34,
	NOTIFICADO_PELO_ANALISTA_TECNICO: 35,
	EM_ANALISE_TECNICA_GERENTE: 36,
	EM_ANALISE_DIRETOR: 37,
	CAIXA_ENTRADA_GERENTE: CAIXA_ENTRADA_GERENTE,
	MENU_EM_ANALISE_GERENTE: MENU_EM_ANALISE_GERENTE,
	CAIXA_ENTRADA_ANALISTA_GEO: CAIXA_ENTRADA_ANALISTA_GEO
};

exports.utils.CondicaoTramitacao = CondicaoTramitacao;