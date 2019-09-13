package models;

import java.util.List;

public class DadosProcessoVO {

	public List<CamadaGeoAtividadeVO> atividades;

	public List<CamadaGeoRestricaoVO> restricoes;

	public DadosProcessoVO(List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes) {

		this.atividades = atividades;
		this.restricoes = restricoes;

	}

}
