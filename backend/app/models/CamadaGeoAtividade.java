package models;

import models.licenciamento.Atividade;

import java.util.List;

public class CamadaGeoAtividade {

	public Atividade atividade;

	public List<CamadaGeo> camadasGeo;

	public CamadaGeoAtividade(Atividade atividade, List<CamadaGeo> camadasGeo) {

		this.atividade = atividade;
		this.camadasGeo = camadasGeo;
	}
}
