package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "observacao")
public class Observacao  extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.observacao_id_seq")
    @SequenceGenerator(name="analise.observacao_id_seq", sequenceName="analise.observacao_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="texto")
    public String texto;

    @Required
    @ManyToOne
    @JoinColumn(name="id_analise_manejo")
    public AnaliseManejo analiseManejo;

    @Required
    @Column(name="num_passo")
    @Enumerated(EnumType.ORDINAL)
    public PassoAnaliseManejo passoAnalise;
}