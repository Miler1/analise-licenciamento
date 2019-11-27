package models;

import models.licenciamento.AtividadeCaracterizacao;
import java.util.List;

public class CamadaGeoAtividadeVO {

	public AtividadeCaracterizacao atividadeCaracterizacao;
	public List<GeometriaAtividadeVO> geometrias;

	public CamadaGeoAtividadeVO(AtividadeCaracterizacao atividadeCaracterizacao, List<GeometriaAtividadeVO> geometrias) {

		this.atividadeCaracterizacao = atividadeCaracterizacao;
		this.geometrias = geometrias;

	}

	public CamadaGeoAtividadeVO(List<GeometriaAtividadeVO> geometrias) {

		this.geometrias = geometrias;

	}

	public List<GeometriaAtividadeVO> getGeometrias() {

		return this.geometrias;

	}

}
