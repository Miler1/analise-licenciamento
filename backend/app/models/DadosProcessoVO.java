package models;

import java.util.List;
import models.licenciamento.Caracterizacao;

public class DadosProcessoVO {

	public Caracterizacao caracterizacao;

	public List<CamadaGeoAtividadeVO> atividades;

	public List<CamadaGeoRestricaoVO> restricoes;

	public CamadaGeoComplexoVO complexo;

	public DadosProcessoVO(Caracterizacao caracterizacao, List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes, CamadaGeoComplexoVO complexo) {

		this.caracterizacao = caracterizacao;
		this.atividades = atividades;
		this.restricoes = restricoes;
		this.complexo = complexo;

	}

}
