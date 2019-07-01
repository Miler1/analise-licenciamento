package models.licenciamento;

public enum TipoAnalise {
	
	JURIDICA(0,"JURIDICA"),
	TECNICA(1,"TECNICA"),
	GEO(3,"GEO");
	
	public int id;
	public String nome;	

	TipoAnalise(int id, String nome) {
		
		this.id = id;
		this.nome = nome;		
	}	

}
