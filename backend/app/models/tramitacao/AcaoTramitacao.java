package models.tramitacao;

import play.db.jpa.GenericModel;


//Tabela de mapeamento das ações de tramitação, essas ações estão na tabela ACOES no banco de tramitação
// De acordo com o que for inserido na tabela inserir aqui também

public class AcaoTramitacao extends GenericModel{
	
	public static final Long CLASSIFICAR = 1L;
	public static final Long CLASSIFICAR_RETIFICACAO = 2L;
	public static final Long VINCULAR = 3L;
	public static final Long DESVINCULAR = 4L;
	public static final Long INICIAR_ANALISE = 5L;
	public static final Long DESVINCULAR_EQUIPE = 6L;
	public static final Long ANALISAR = 7L;
	public static final Long RECUSAR_VINCULANDO_A_MESMA_EQUIPE = 8L;
	public static final Long ENCAMINHAR_PARA_ANALISE_POR_ATENDIMENTO_DA_NOTIFICACAO = 9L;
	public static final Long VALIDAR_SEM_PENDENCIA = 10L;
	public static final Long ENCAMINHAR_PARA_NOVA_ANALISE = 11L;
	public static final Long VALIDAR_COM_PENDENCIA = 12L;
	public static final Long EMITIR_NOTIFICACAO = 13L;
	public static final Long REGISTRAR_ENTREGA_PESSOAL = 14L;
	public static final Long REGISTRAR_ENVIO_NOTIFICACAO_COM_AR = 15L;
	public static final Long REGISTRAR_DADOS_DEVOLUCAO_AR = 16L;
	public static final Long REGISTRAR_DADOS_DE_ENTREGA_AR_COM_SUCESSO = 17L;
	public static final Long REGISTRAR_DADOS_DA_DEVOLUCAO_EXCEDENDO_LIMITE = 18L;
	public static final Long REGISTRAR_DADOS_PUBLICACAO_OFICIAL = 19L;
	public static final Long ENVIAR_PARA_CANCELAMENTO = 20L;
	public static final Long RESTAURAR = 21L;
	public static final Long CANCELAR = 22L;
	public static final Long CANCELAR_POR_DECISAO_ADMINISTRATIVA = 23L;
	public static final Long VINCULAR_EM_ANALISE = 24L;
	public static final Long RECUSAR_DESVINCULANDO_A_EQUIPE = 25L;
	public static final Long CANCELAR_POR_DECISAO_JUDICIAL = 26L;
	public static final Long ALTERAR_GERENTE_OPERACIONAL_VINCULADO = 27L;
	public static final Long RETIFICAR_IMOVEL_VALIDADO_SEM_PENDENCIAS = 28L;
	public static final Long CANCELAR_POR_DECISAO_ADMINISTRATIVA_NO_SICAR = 29L;
	public static final Long CANCELAR_POR_DECISAO_JUDICIAL_NO_SICAR = 30L;
	
	public static final Long ENCAMINHAR_PARA_PROCESSOS_ANALISADOS_POR_ATENDIMENTO_DA_NOTIFICACAO = 34L;
	public static final Long ENCAMINHAR_PARA_ANALISE_EXPEDIDA = 35L;
	public static final Long ENCAMINHAR_PARA_ANALISE_POR_ATENDIMENTO_DA_NOTIFICACAO_EXPEDIDA = 36L;
	public static final Long RECUSAR_AUTOMATICAMENTE = 37L;
	public static final Long FINALIZAR_ANALISE_EXPEDIDA= 38L;
	public static final Long APROVAR_ANALISE_EXPEDIDA = 39L;
	public static final Long RECUSAR_ANALISE_EXPEDIDA = 40L;
}

