package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "analise", name = "base_vetorial")
public class BaseVetorial extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.base_vetorial_id_seq")
    @SequenceGenerator(name="analise.base_vetorial_id_seq", sequenceName="analise.base_vetorial_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="nome")
    public String nome;

    @Required
    @Column(name="fonte")
    public String fonte;

    @Required
    @Column(name="ultima_atualizacao")
    public Date ultimaAtualizacao;

    @Required
    @Column(name="escala")
    public String escala;

    @Required
    @Column(name="observacao")
    public String observacao;
}
