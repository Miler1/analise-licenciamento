package models;

import java.util.List;

import models.licenciamento.Caracterizacao;
import static models.Inconsistencia.Categoria;
import static models.licenciamento.Caracterizacao.OrigemSobreposicao.*;

public class DadosProcessoVO {

	public Caracterizacao caracterizacao;

	public List<CamadaGeoAtividadeVO> atividades;

	public List<CamadaGeoRestricaoVO> restricoes;

	public Categoria categoria;

	public CamadaGeoComplexoVO complexo;

	private static Categoria preencheCategoria(Caracterizacao caracterizacao) {

		if(caracterizacao.origemSobreposicao.equals(EMPREENDIMENTO)) {

			return Categoria.PROPRIEDADE;

		} else if(caracterizacao.origemSobreposicao.equals(ATIVIDADE)) {

			return Categoria.ATIVIDADE;

		} else if(caracterizacao.origemSobreposicao.equals(COMPLEXO)) {

			return Categoria.COMPLEXO;

		} else {

			if(caracterizacao.atividadesCaracterizacao.get(0).atividade.dentroEmpreendimento) {

				return Categoria.PROPRIEDADE;

			} else {

				if(caracterizacao.geometriasComplexo != null && !caracterizacao.geometriasComplexo.isEmpty()) {

					return Categoria.COMPLEXO;

				} else {

					return Categoria.ATIVIDADE;

				}

			}

		}

	}

	public DadosProcessoVO(Caracterizacao caracterizacao, List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes) {

		this.caracterizacao = caracterizacao;
		this.atividades = atividades;
		this.restricoes = restricoes;
		this.categoria = preencheCategoria(caracterizacao);

	}

	public DadosProcessoVO(Caracterizacao caracterizacao, List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes, CamadaGeoComplexoVO complexo) {

		this.caracterizacao = caracterizacao;
		this.atividades = atividades;
		this.restricoes = restricoes;
		this.complexo = complexo;

	}

}
