package models.licenciamento;

public enum TipoLocalizacao {
	
	ZONA_URBANA(0, "Zona urbana"),
	
	ZONA_RURAL(1, "Zona rural");
	
	public int id;
	public String nome;
	
	TipoLocalizacao(int id, String nome) {
		
		this.id = id;
		this.nome = nome;
	}
}
