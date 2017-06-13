package models.tramitacao;

import play.db.jpa.GenericModel;


//Tabela de mapeamento das ações de tramitação, essas ações estão na tabela ACOES no banco de tramitação
// De acordo com o que for inserido na tabela inserir aqui também

public class AcaoTramitacao extends GenericModel{
	
	public static final Long VINCULAR_CONSULTOR = 1l;
	public static final Long INICIAR_ANALISE_JURIDICA = 2l;
	public static final Long NOTIFICAR = 3l;
	public static final Long DEFERIR_ANALISE_JURIDICA = 4l;
	public static final Long INVALIDAR_ANALISE_JURIDICA = 5l;
	public static final Long VALIDAR_DEFERIMENTO_JURIDICO = 6l;
	public static final Long VALIDAR_INDEFERIMENTO_JURIDICO = 7l;
	public static final Long INDEFERIR_ANALISE_JURIDICA = 8l;
	public static final Long SOLICITAR_AJUSTES_PARECER_JURIDICO = 9l;
	public static final Long VINCULAR_ANALISTA = 10l;
	public static final Long INICIAR_ANALISE_TECNICA = 11l;
	public static final Long DEFERIR_ANALISE_TECNICA = 12l;
	public static final Long INDEFERIR_ANALISE_TECNICA = 13l;
	public static final Long INVALIDAR_PARECER_TECNICO = 14l;
	public static final Long SOLICITAR_AJUSTES_PARECER_TECNICO = 15l;
	public static final Long VALIDAR_DEFERIMENTO_TECNICO = 16l;
	public static final Long VALIDAR_INDEFERIMENTO_TECNICO = 17l;
	public static final Long INICIAR_PROCESSO = 18l;

}

