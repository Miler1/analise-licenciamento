package models.tramitacao;

import play.db.jpa.GenericModel;


//Tabela de mapeamento das ações de tramitação, essas ações estão na tabela ACOES no banco de tramitação
// De acordo com o que for inserido na tabela inserir aqui também

public class AcaoTramitacao extends GenericModel{
	
	public static final Long VINCULAR_CONSULTOR = 1l;
	public static final Long INICIAR_ANALISE_JURIDICA = 2l;
	public static final Long NOTIFICAR = 3l;
	public static final Long DEFERIR_ANALISE_JURIDICA = 4l;
	public static final Long INVALIDAR_PARECER_JURIDICO = 5l;
	public static final Long VALIDAR_DEFERIMENTO_JURIDICO = 6l;
	public static final Long VALIDAR_INDEFERIMENTO_JURIDICO = 7l;
	public static final Long INDEFERIR_ANALISE_JURIDICA = 8l;
	public static final Long SOLICITAR_AJUSTES_PARECER_JURIDICO = 9l;
	public static final Long VINCULAR_ANALISTA = 10l;
	public static final Long INICIAR_ANALISE_TECNICA = 11l;
	public static final Long DEFERIR_ANALISE_TECNICA_VIA_GERENTE = 12l;
	public static final Long INDEFERIR_ANALISE_TECNICA_VIA_GERENTE = 13l;
	public static final Long INVALIDAR_PARECER_TECNICO_PELO_GERENTE = 14l;
	public static final Long SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_GERENTE = 15l;
	public static final Long VALIDAR_DEFERIMENTO_TECNICO_PELO_GERENTE = 16l;
	public static final Long VALIDAR_INDEFERIMENTO_TECNICO_PELO_GERENTE = 17l;
	public static final Long INICIAR_PROCESSO = 18l;
	public static final Long VINCULAR_GERENTE = 19l;
	public static final Long INDEFERIR_ANALISE_TECNICA_VIA_COORDENADOR = 20l;
	public static final Long DEFERIR_ANALISE_TECNICA_VIA_COORDENADOR = 21l;
	public static final Long VALIDAR_DEFERIMENTO_TECNICO_PELO_COORDENADOR = 22l;
	public static final Long VALIDAR_INDEFERIMENTO_TECNICO_PELO_COORDENADOR = 23l;
	public static final Long INVALIDAR_PARECER_TECNICO_PELO_COORD_ENCAMINHANDO_GERENTE = 24l;
	public static final Long INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO = 25l;
	public static final Long SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR_PARA_ANALISTA = 26l;
	public static final Long SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR_PARA_GERENTE = 27l;
	public static final Long SOLICITAR_AJUSTES_ANALISE_JURIDICA_APROVADOR = 28l;
	public static final Long DEFERIR_ANALISE_JURIDICA_COORDENADOR_APROVADOR = 29l;
	public static final Long SOLICITAR_AJUSTES_ANALISE_TECNICA_APROVADOR = 30l;
	

}

