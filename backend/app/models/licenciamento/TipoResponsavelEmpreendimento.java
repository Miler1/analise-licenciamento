package models.licenciamento;

import java.util.HashMap;
import java.util.HashSet;

public enum TipoResponsavelEmpreendimento {
	
	LEGAL(0, "Responsável legal"),
	
	TECNICO(1, "Responsável técnico");
	
	public int id;
	public String nome;
	
	TipoResponsavelEmpreendimento(int id, String nome) {
		
		this.id = id;
		this.nome = nome;
	}
	
	public static HashMap<TipoResponsavelEmpreendimento, String> getAllTiposResponsaveisWithNomes() {
		
		HashMap<TipoResponsavelEmpreendimento, String> tipos = new HashMap<>();
		
		for (TipoResponsavelEmpreendimento tipo : TipoResponsavelEmpreendimento.values()) {
			
			tipos.put(tipo, tipo.nome);
		}
		
		return tipos;
	}
}
