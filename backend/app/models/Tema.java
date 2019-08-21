package models;

import models.tmsmap.LayerType;

public class Tema implements LayerType {
	

	public String cor;

	public String nome;

	@Override
	public String getName() {
		return this.nome;
	}

	@Override
	public String getColor() {
		return this.cor;
	}

	public Tema(String nome, String cor) {

		super();
		this.nome = nome;
		this.cor = cor;
	}

}