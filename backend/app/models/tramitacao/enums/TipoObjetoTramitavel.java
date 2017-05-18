package models.tramitacao.enums;


// Enum com os tipos de objetos tramitaveis
// Estão cadastrados na tabela TipoObjetoTramitavel no banco de tramitacao do analise car

public enum TipoObjetoTramitavel {
	
	ANALISE_CAR( 1L );

	private Long id;

	private TipoObjetoTramitavel( Long id ) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}
}
