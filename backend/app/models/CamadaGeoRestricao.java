package models;

import com.vividsolutions.jts.geom.Geometry;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.GeometriaAtividade;
import models.licenciamento.SobreposicaoCaracterizacao;
import models.licenciamento.SobreposicaoCaracterizacaoAtividade;

public class CamadaGeoRestricao {

	public String item;

	public String tipo;

	public String descricao;

	public Double area;

	public Geometry geometria;

	public SobreposicaoCaracterizacao sobreposicaoCaracterizacao;

	public CamadaGeoRestricao(String item, String tipo, String descricao, Double area, SobreposicaoCaracterizacao sobreposicaoCaracterizacao) {

		this.item = item;
		this.tipo = tipo;
		this.descricao = descricao;
		this.area = area;
		this.sobreposicaoCaracterizacao = sobreposicaoCaracterizacao;

	}

}
