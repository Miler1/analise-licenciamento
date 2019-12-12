package models;

import java.util.List;

import models.licenciamento.AtividadeCaracterizacao;
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

		} else if(caracterizacao.origemSobreposicao.equals(COMPLEXO)) {

			return Categoria.COMPLEXO;

		} else if(caracterizacao.origemSobreposicao.equals(ATIVIDADE)) {

			return Categoria.ATIVIDADE;

		} else {

			boolean isAtividadeDentroEmpreendimento = caracterizacao.atividadesCaracterizacao.stream().allMatch(AtividadeCaracterizacao::isAtividadeDentroEmpreendimento);

			if(isAtividadeDentroEmpreendimento) {

				return Categoria.PROPRIEDADE;

			} else {

				return Categoria.ATIVIDADE;

			}

		}

	}

	public DadosProcessoVO(Caracterizacao caracterizacao, List<CamadaGeoAtividadeVO> atividades, List<CamadaGeoRestricaoVO> restricoes) {

		this.caracterizacao = caracterizacao;
		this.atividades = atividades;
		this.restricoes = restricoes;
		this.categoria = preencheCategoria(caracterizacao);

		if(caracterizacao.isComplexo()) {
			this.complexo = Processo.preencheComplexo(caracterizacao);
		}

	}

}
