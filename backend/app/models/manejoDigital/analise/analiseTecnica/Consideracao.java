package models.manejoDigital.analise.analiseTecnica;

import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
