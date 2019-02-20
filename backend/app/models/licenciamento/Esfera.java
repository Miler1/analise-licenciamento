package models.licenciamento;

public enum Esfera {

	MUNICIPAL(0, "Municipal"),
	
	ESTADUAL(1, "Estadual"),
	
	FEDERAL(2, "Federal");
	
	public int id;
	public String nome;
	
	Esfera (int id, String nome) {
		
		this.id = id;
		this.nome = nome;

	}

}
