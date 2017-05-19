package models.tramitacao;

import play.db.jpa.GenericModel;


//Tabela de mapeamento das ações de tramitação, essas ações estão na tabela ACOES no banco de tramitação
// De acordo com o que for inserido na tabela inserir aqui também

public class AcaoTramitacao extends GenericModel{
	
	public static final Long VINCULAR = 1l;
	public static final Long INICIAR_ANALISE = 2l;
	public static final Long NOTIFICAR = 3l;
	public static final Long ANALISAR = 4l;
	public static final Long RECUSAR_ANALISE = 5l;
	public static final Long DEFERIR_ANALISE = 6l;
	public static final Long INDEFERIR_ANALISE = 7l;

}

