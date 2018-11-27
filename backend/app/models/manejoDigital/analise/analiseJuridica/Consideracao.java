package models.manejoDigital.analise.analiseJuridica;

import models.manejoDigital.PassoAnaliseManejo;
import play.data.validation.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = "analise", name = "analise_juridica_manejo")
public class Consideracao {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.consideracao_id_seq")
    @SequenceGenerator(name="analise.consideracao_id_seq", sequenceName="analise.consideracao_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column
    public String texto;

    @Required
    @ManyToOne
    @JoinColumn(name="id_analise_juridica_manejo")
    public AnaliseJuridicaManejo analiseJuridicaManejo;

    @Required
    @Column(name="num_passo")
    @Enumerated(EnumType.ORDINAL)
    public PassoAnaliseManejo passoAnalise;

    @Required
    @Column
    public Date data;
}
