package models.tramitacao;

import play.db.jpa.GenericModel;


//Tabela de mapeamento das ações de tramitação, essas ações estão na tabela ACOES no banco de tramitação
// De acordo com o que for inserido na tabela inserir aqui também

public class AcaoTramitacao extends GenericModel{

	public static final Long VINCULAR_CONSULTOR = 1L;
	public static final Long INICIAR_ANALISE_JURIDICA = 2L;
	public static final Long NOTIFICAR_PELO_ANALISTA_GEO = 3L;
	public static final Long DEFERIR_ANALISE_JURIDICA = 4L;
	public static final Long INVALIDAR_PARECER_JURIDICO = 5L;
	public static final Long VALIDAR_DEFERIMENTO_JURIDICO = 6L;
	public static final Long VALIDAR_INDEFERIMENTO_JURIDICO = 7L;
	public static final Long INDEFERIR_ANALISE_JURIDICA = 8L;
	public static final Long SOLICITAR_AJUSTES_PARECER_JURIDICO = 9L;
	public static final Long VINCULAR_ANALISTA = 10L;
	public static final Long INICIAR_ANALISE_TECNICA = 11L;
	// public static final Long DEFERIR_ANALISE_TECNICA_VIA_GERENTE = 12L;
	// public static final Long INDEFERIR_ANALISE_TECNICA_VIA_GERENTE = 13L;
	public static final Long INVALIDAR_PARECER_TECNICO_PELO_COORDENADOR = 14L;
	public static final Long SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR = 15L;
	public static final Long VALIDAR_DEFERIMENTO_TECNICO_PELO_GERENTE = 16L;
	public static final Long VALIDAR_INDEFERIMENTO_TECNICO_PELO_GERENTE = 17L;
	public static final Long INICIAR_PROTOCOLO = 18L;
	public static final Long VINCULAR_COORDENADOR = 19L;
	public static final Long INDEFERIR_ANALISE_TECNICA_VIA_COORDENADOR = 13L;
	public static final Long DEFERIR_ANALISE_TECNICA_VIA_COORDENADOR = 12L;
	public static final Long VALIDAR_DEFERIMENTO_TECNICO_PELO_COORDENADOR = 22L;
	public static final Long VALIDAR_INDEFERIMENTO_TECNICO_PELO_COORDENADOR = 23L;
	public static final Long INVALIDAR_PARECER_TECNICO_PELO_COORD_ENCAMINHANDO_COORDENADOR= 24L;
	public static final Long INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO = 25L;
	public static final Long SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR_PARA_ANALISTA = 26L;
	public static final Long SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR_PARA_COORDENADOR = 27L;
	public static final Long SOLICITAR_AJUSTES_ANALISE_JURIDICA_APROVADOR = 28L;
	public static final Long DEFERIR_ANALISE_JURIDICA_COORDENADOR_APROVADOR = 29L;
	public static final Long SOLICITAR_AJUSTES_ANALISE_TECNICA_APROVADOR = 30L;
	public static final Long EMITIR_LICENCA = 31L;
	public static final Long SUSPENDER_PROTOCOLO = 32L;
	public static final Long REEMITIR_LICENCA = 33L;
	public static final Long CANCELAR_PROTOCOLO = 34L;
	public static final Long RESOLVER_NOTIFICACAO_JURIDICA = 35L;
	public static final Long RESOLVER_NOTIFICACAO_TECNICA = 36L;
	public static final Long ARQUIVAR_PROTOCOLO = 37L;
	public static final Long ARQUIVAR_POR_RENOVACAO = 38L;
	public static final Long RENOVAR_SEM_ALTERACAO = 39L;
	public static final Long INICIAR_ANALISE_TECNICA_MANEJO = 40L;
	public static final Long DEFERIR_PROCESSO_MANEJO = 41L;
	public static final Long INDEFERIR_PROCESSO_MANEJO_ANALISE_SHAPE = 42L;
	public static final Long PRORROGAR_LICENCA = 43L;
	public static final Long ARQUIVAR_PRORROGACAO_POR_RENOVACAO = 44L;
	public static final Long INICIAR_ANALISE_SHAPE = 45L;
	public static final Long FINALIZAR_ANALISE_SHAPE = 46L;
	public static final Long SOLICITAR_REVISAO_SHAPE = 47L;
	public static final Long INDEFERIR_PROCESS_MANEJO_ANALISE_TECNICA = 48L;
	public static final Long DEFERIR_ANALISE_GEO_VIA_COORDENADOR= 49L;
	public static final Long INDEFERIR_ANALISE_GEO_VIA_COORDENADOR = 50L;
	public static final Long INICIAR_ANALISE_GEO = 51L;
	public static final Long INVALIDAR_PARECER_GEO_PELO_COORDENADOR = 52L;
	public static final Long VALIDAR_DEFERIMENTO_GEO_PELO_COORDENADOR = 53L;
	public static final Long VALIDAR_INDEFERIMENTO_GEO_PELO_COORDENADOR = 54L;
	public static final Long SOLICITAR_AJUSTES_PARECER_GEO_PELO_COORDENADOR = 55L;
	public static final Long SOLICITAR_AJUSTES_ANALISE_GEO_SECRETARIO = 56L;
	public static final Long RESOLVER_NOTIFICACAO_GEO = 57L;
	public static final Long INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO = 58L;
	public static final Long SOLICITAR_DESVINCULO_ANALISE_GEO = 59L;
	public static final Long APROVAR_SOLICITACAO_DESVINCULO = 60L;
	public static final Long NEGAR_SOLICITACAO_DESVINCULO = 61L;
	public static final Long INICIAR_ANALISE_COORDENADOR = 62L;
	public static final Long VALIDAR_PARECER_GEO_COORDENADOR = 63L;
	public static final Long RESOLVER_COMUNICADO = 64L;
	public static final Long AGUARDAR_RESPOSTA_COMUNICADO = 65L;
	public static final Long NOTIFICAR_PELO_ANALISTA_TECNICO = 66L;
	public static final Long INICIAR_ANALISE_TECNICA_COORDENADOR = 67L;
	public static final Long VALIDAR_PARECER_TECNICO_COORDENADOR = 68L;
	public static final Long APROVAR_SOLICITACAO_DESVINCULO_TECNICO = 69L;
	public static final Long NEGAR_SOLICITACAO_DESVINCULO_TECNICO = 70L;
	public static final Long SOLICITAR_DESVINCULO_ANALISE_TECNICA = 71L;
	public static final Long INICIAR_ANALISE_DIRETOR = 72L;
	public static final Long INICIAR_ANALISE_TECNICA_POR_VOLTA_DE_NOTIFICACAO = 73L;
	public static final Long VALIDAR_ANALISE_PELO_DIRETOR = 74L;
	public static final Long INVALIDAR_ANALISE_PELO_DIRETOR = 75L;
	public static final Long INICIAR_ANALISE_SECRETARIO = 76L;
	public static final Long RESOLVER_ANALISE_JURIDICA = 77L;
	public static final Long APROVAR_SOLICITACAO_LICENCA = 78L;
	public static final Long NEGAR_SOLICITACAO_LICENCA = 79L;

}
