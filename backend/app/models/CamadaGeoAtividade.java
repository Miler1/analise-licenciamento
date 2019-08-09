package models;

import java.util.List;

public class CamadaGeoAtividade {

	public String atividade;

	public List<CamadaGeo> camadasGeo;

	public CamadaGeoAtividade(String atividade, List<CamadaGeo> camadasGeo) {

		this.atividade = atividade;
		this.camadasGeo = camadasGeo;
	}
}
