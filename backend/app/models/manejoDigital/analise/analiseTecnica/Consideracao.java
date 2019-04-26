package models.manejoDigital.analise.analiseTecnica;

import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "consideracao")
public class Consideracao extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.consideracao_id_seq")
    @SequenceGenerator(name="analise.consideracao_id_seq", sequenceName="analise.consideracao_id_seq", allocationSize=1)
    public Long id;

    @Column
    public String texto;
}
