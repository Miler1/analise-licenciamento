package models.tramitacao.enums;

public enum FluxoTramitacao {
	
	PROCESSO_ANALISE_LICENCIAMENTO( 1L );

	private Long id;

	private FluxoTramitacao( Long id ) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}
}
