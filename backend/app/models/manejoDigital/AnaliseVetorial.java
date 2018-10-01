package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

public class AnaliseVetorial extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise_vetorial_id_seq")
    @SequenceGenerator(name="analise_vetorial_id_seq", sequenceName="analise_vetorial_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="tipo")
    public String tipo;

    @Required
    @Column(name="nome")
    public String nome;

    @Required
    @Column(name="distancia_propriedade")
    public Double distanciaPropriedade;

    @Required
    @Column(name="sobreposicao_propriedade")
    public Double sobreposicaoPropriedade;

    @Required
    @Column(name="distancia_amf")
    public Double distanciaAmf;

    @Required
    @Column(name="sobreposicao_amf")
    public Double sobreposicaoAmf;

    @Required
    @Column(name="observacao")
    public String observacao;

    @Required
    @ManyToOne
    @JoinColumn(name="id_analise_manejo")
    public Integer idAnaliseManejo;
}
