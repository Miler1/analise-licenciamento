package models;

import models.licenciamento.AtividadeCaracterizacao;

import java.util.List;

public class CamadaGeoAtividade {

	public AtividadeCaracterizacao atividadeCaracterizacao;

	public List<CamadaGeo> camadasGeo;

	public CamadaGeoAtividade(AtividadeCaracterizacao atividadeCaracterizacao, List<CamadaGeo> camadasGeo) {

		this.atividadeCaracterizacao = atividadeCaracterizacao;
		this.camadasGeo = camadasGeo;
	}
}
