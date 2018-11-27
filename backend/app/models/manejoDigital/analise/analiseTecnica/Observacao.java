package models.manejoDigital.analise.analiseTecnica;

import models.manejoDigital.PassoAnaliseManejo;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "analise", name = "observacao")
public class Observacao  extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.observacao_id_seq")
    @SequenceGenerator(name="analise.observacao_id_seq", sequenceName="analise.observacao_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column
    public String texto;

    @Required
    @ManyToOne
    @JoinColumn(name="id_analise_tecnica_manejo")
    public AnaliseTecnicaManejo analiseTecnicaManejo;

    @Required
    @Column(name="num_passo")
    @Enumerated(EnumType.ORDINAL)
    public PassoAnaliseManejo passoAnalise;

    @Required
    @Column
    public Date data;

    @Override
    public Observacao save() {

        this.analiseTecnicaManejo = AnaliseTecnicaManejo.findById(this.analiseTecnicaManejo.id);

        this.data = new Date();

        this._save();

        return this.refresh();
    }
}