package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

public class DadosAmf extends GenericModel {

    public String tipo;

    public String nome;

    public Double distanciaPropriedade;

    public Double sobreposicaoPropriedade;

    public Double distanciaAmf;

    public Double sobreposicaoAmf;

    public Double sobreposicao;

    public String fonte;

    public Date ultimaAtualizacao;

    public Double escala;

    public String observacaoMetadadosBase;

    public String observacaoMetadadosBaseAmf;

    public String observacaoAnaliseVetorialAmf;
}
