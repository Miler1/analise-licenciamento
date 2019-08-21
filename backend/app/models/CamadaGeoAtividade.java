package models;

import models.licenciamento.AtividadeCaracterizacao;

import java.util.List;

public class CamadaGeoAtividade {

	public AtividadeCaracterizacao atividadeCaracterizacao;

	public List<CamadaGeo> camadasGeo;

	List<CamadaGeo> restricoes;

	public CamadaGeoAtividade(AtividadeCaracterizacao atividadeCaracterizacao, List<CamadaGeo> camadasGeo, List<CamadaGeo> restricoes) {

		this.atividadeCaracterizacao = atividadeCaracterizacao;
		this.camadasGeo = camadasGeo;
		this.restricoes = restricoes;
	}
}
