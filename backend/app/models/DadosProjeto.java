package models;

import java.util.List;

public class DadosProjeto {

	public List<CamadaGeoAtividade> atividades;

	public List<CamadaGeoRestricao> restricoes;

	public DadosProjeto(List<CamadaGeoAtividade> atividades, List<CamadaGeoRestricao> restricoes) {

		this.atividades = atividades;
		this.restricoes = restricoes;

	}

}
