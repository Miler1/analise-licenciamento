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
@Table(schema = "analise", name = "embasamento_legal")
public class EmbasamentoLegal extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.embasamento_legal_id_seq")
    @SequenceGenerator(name="analise.embasamento_legal_id_seq", sequenceName="analise.embasamento_legal_id_seq", allocationSize=1)
    public Long id;

    @Column
    public String texto;
}
