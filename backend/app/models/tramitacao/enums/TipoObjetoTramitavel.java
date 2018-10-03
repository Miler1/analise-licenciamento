package models.tramitacao.enums;


// Enum com os tipos de objetos tramitaveis
// Estão cadastrados na tabela TipoObjetoTramitavel no banco de tramitacao do licenciamento-pa

public enum TipoObjetoTramitavel {
	
	LICENCIAMENTO_AMBIENTAL( 1L ),
	MANEJO_DIGITAL( 2L );

	private Long id;

	private TipoObjetoTramitavel( Long id ) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}
}
