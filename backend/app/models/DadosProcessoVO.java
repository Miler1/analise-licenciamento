package models;

import java.util.List;

import models.licenciamento.Caracterizacao;
import static models.Inconsistencia.Categoria;

public class DadosProcessoVO {

	public Caracterizacao caracterizacao;

	public List<CamadaGeoAtividadeVO> atividades;

	public List<CamadaGeoRestricaoVO> restricoes;

	public CamadaGeoComplexoVO complexo;

	public Categoria categoria;

	public DadosProcessoVO(Caracterizacao caracterizacao, List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes) {

		this.caracterizacao = caracterizacao;
		this.atividades = atividades;
		this.restricoes = restricoes;
		this.categoria = Categoria.preencheCategoria(caracterizacao);

	}

	public DadosProcessoVO(Caracterizacao caracterizacao, List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes, CamadaGeoComplexoVO complexo) {

		this.caracterizacao = caracterizacao;
		this.atividades = atividades;
		this.restricoes = restricoes;
		this.complexo = complexo;

	}

}
