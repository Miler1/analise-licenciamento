package models;

import models.licenciamento.Caracterizacao;
import java.util.List;

public class CamadaGeoComplexoVO {


    public Caracterizacao caracterizacao;
    public List<GeometriaAtividadeVO> geometrias;

    public CamadaGeoComplexoVO(Caracterizacao caracterizacao, List<GeometriaAtividadeVO> geometrias) {

        this.caracterizacao = caracterizacao;
        this.geometrias = geometrias;

    }

    public CamadaGeoComplexoVO(List<GeometriaAtividadeVO> geometrias) {

        this.geometrias = geometrias;

    }

    public List<GeometriaAtividadeVO> getGeometrias() {

        return this.geometrias;

    }

}
