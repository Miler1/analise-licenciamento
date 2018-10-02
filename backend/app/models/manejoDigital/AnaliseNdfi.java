package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "analise", name = "analise_ndfi")
public class AnaliseNdfi extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise_ndfi_id_seq")
    @SequenceGenerator(name="analise_ndfi_id_seq", sequenceName="analise_ndfi_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="data")
    public Date dataAnaliseNdfi;

    @Required
    @Column(name="orbita")
    public Integer orbita;

    @Required
    @Column(name="ponto")
    public Integer ponto;

    @Required
    @Column(name="satelite")
    public String satelite;

    @Required
    @Column(name="nivel_exploracao")
    public String nivelExploracao;

    @Required
    @Column(name="valor_ndfi")
    public Double valorNdfi;

    @Required
    @Column(name="area")
    public Double area;

    @Required
    @OneToOne
    @JoinColumn(name="id_analise_manejo")
    public AnaliseManejo analiseManejo;

}
