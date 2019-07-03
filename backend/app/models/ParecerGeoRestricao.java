package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Identificavel;
import utils.validacao.Validacao;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema="analise", name="parecer_geo_restricao")
public class ParecerGeoRestricao extends GenericModel implements Identificavel {

    public static final String SEQ = "analise.parecer_geo_restricao_id_seq";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @Required
    @ManyToOne
    @JoinColumn(name="id_analise_geo")
    public AnaliseGeo analiseGeo;

    @Required
    @Column(name="codigo_camada")
    public String codigoCamada;

    @Required
    public String parecer;

    @Override
    public Long getId() {

        return this.id;
    }

    @Override
    public ParecerGeoRestricao save() {

        Validacao.validar(this);

        return super.save();
    }

    public void update(ParecerGeoRestricao novoParecerGeoRestricao) {

        this.codigoCamada = novoParecerGeoRestricao.codigoCamada;
        this.parecer = novoParecerGeoRestricao.parecer;

        this.save();
    }

    public List<ParecerGeoRestricao> getByIdAnaliseGeo(Long idAnaliseGeo) {

        return ParecerGeoRestricao.find("byAnaliseGeo", idAnaliseGeo).fetch();

    }

    public ParecerGeoRestricao gerarCopia() {

        ParecerGeoRestricao copia = new ParecerGeoRestricao();

        copia.analiseGeo = this.analiseGeo;
        copia.codigoCamada = this.codigoCamada;
        copia.parecer = this.parecer;

        return copia;
    }
}
