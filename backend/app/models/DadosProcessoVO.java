package models;

import java.util.List;

import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.Caracterizacao;
import static models.Inconsistencia.Categoria;

public class DadosProcessoVO {

	public Caracterizacao caracterizacao;

	public List<CamadaGeoAtividadeVO> atividades;

	public List<CamadaGeoRestricaoVO> restricoes;

	public Categoria categoria;

	public CamadaGeoComplexoVO complexo;

	public DadosProcessoVO(Caracterizacao caracterizacao, List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes) {

		this.caracterizacao = caracterizacao;
		this.atividades = atividades;
		this.restricoes = restricoes;
		this.categoria = Categoria.preencheCategoria(caracterizacao);

		if(caracterizacao.isComplexo()) {
			this.complexo = Processo.preencheComplexo(caracterizacao);
		}

	}

}
