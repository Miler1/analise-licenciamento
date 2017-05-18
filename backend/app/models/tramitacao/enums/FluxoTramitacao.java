package models.tramitacao.enums;

// Enum que contem os ids dos fluxos do analise do car 

public enum FluxoTramitacao {
	
	PROCESSO_ANALISE_CAR( 1L );

	private Long id;

	private FluxoTramitacao( Long id ) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}
}
