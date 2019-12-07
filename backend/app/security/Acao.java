package security;


public enum Acao {

	LISTAR_PROCESSO("ANL_LISTAR_PROCESSO"),
	VINCULAR_PROCESSO("ANL_VINCULAR_PROCESSO"),
	DESVINCULAR_PROCESSO("ANL_DESVINCULAR_PROCESSO"),
	CONSULTAR_PROCESSO("ANL_CONSULTAR_PROCESSO"),
	VALIDAR_PARECER_GCAP("ANL_VALIDAR_PARECER_GCAP"),
	VALIDAR_PARECER_GEO("ANL_VALIDAR_PARECER_GEO"),
	VALIDAR_PARECER_TECNICO("ANL_VALIDAR_PARECER_TECNICO"),
	INICIAR_PARECER_GCAP("ANL_INICIAR_PARECER_GCAP"),
	INICIAR_PARECER_GEO("ANL_INICIAR_PARECER_GEO"),
	INICIAR_PARECER_TECNICO("ANL_INICIAR_PARECER_TECNICO"),
	APROVAR_ANALISE("ANL_APROVAR_ANALISE"),
	VALIDAR_PARECERES("ANL_VALIDAR_PARECERES"),
	CONSULTAR_LICENCAS_EMITIDAS("ANL_CONSULTAR_LICENCAS_EMITIDAS"),
	EMITIR_LICENCA("ANL_EMITIR_LICENCA"),
	SUSPENDER_LICENCA_EMITIDA("ANL_SUSPENDER_LICENCA_EMITIDA"),
	CANCELAR_LICENCA_EMITIDA("ANL_CANCELAR_LICENCA_EMITIDA"),
	VISUALIZAR_NOTIFICACAO("ANL_VISUALIZAR_NOTIFICACAO"),
	SALVAR_INCONSISTENCIA_TECNICA("ANL_SALVAR_INCONSISTENCIA_TECNICA"),
	SALVAR_INCONSISTENCIA_GEO("ANL_SALVAR_INCONSISTENCIA_GEO"),
	EXCLUIR_INCONSISTENCIA_TECNICA("ANL_EXCLUIR_INCONSISTENCIA_TECNICA"),
	EXCLUIR_INCONSISTENCIA_GEO("ANL_EXCLUIR_INCONSISTENCIA_GEO"),

	//TODO SQUAD2 - Remover estas ações após finalização do novo fluxo
	VINCULAR_PROCESSO_JURIDICO("ANL_VINCULAR_PROCESSO_JURIDICO"),
	VALIDAR_PARECER_JURIDICO("ANL_VALIDAR_PARECER_JURIDICO"),
	INICIAR_PARECER_JURIDICO("ANL_INICIAR_PARECER_JURIDICO"),
	VALIDAR_PARECERES_JURIDICO_TECNICO("ANL_VALIDAR_PARECERES_JURIDICO_TECNICO"),
	LISTAR_PROCESSO_MANEJO("ANL_LISTAR_PROCESSO_MANEJO"),
	ANALISAR_PROCESSO_MANEJO("ANL_ANALISAR_PROCESSO_MANEJO"),
	VISUALIZAR_PROCESSO_MANEJO("ANL_VISUALIZAR_PROCESSO_MANEJO"),
	CADASTRAR_PROCESSO_MANEJO("ANL_CADASTRAR_PROCESSO_MANEJO"),
	VINCULAR_PROCESSO_TECNICO("ANL_VINCULAR_PROCESSO_TECNICO");

	public String codigo;

	private Acao(String codigo) {

		this.codigo = codigo;
	}
}
