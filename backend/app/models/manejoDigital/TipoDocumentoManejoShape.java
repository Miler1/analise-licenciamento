package models.manejoDigital;

public enum TipoDocumentoManejoShape {

	PROPRIEDADE(0,"PROPRIEDADE"),
	AREA_MANEJO(1,"AREA_MANEJO"),
	MANEJO(3, "MANEJO");

	public int id;
	public String nome;

	TipoDocumentoManejoShape(int id, String nome) {

		this.id = id;
		this.nome = nome;
	}
}
