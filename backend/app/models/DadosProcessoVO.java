package models;

import java.util.List;
import models.licenciamento.Caracterizacao;

public class DadosProcessoVO {

	public Caracterizacao caracterizacao;

	public List<CamadaGeoAtividadeVO> atividades;

	public List<CamadaGeoRestricaoVO> restricoes;

	public DadosProcessoVO(Caracterizacao caracterizacao, List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes) {

		this.caracterizacao = caracterizacao;
		this.atividades = atividades;
		this.restricoes = restricoes;

	}

}
