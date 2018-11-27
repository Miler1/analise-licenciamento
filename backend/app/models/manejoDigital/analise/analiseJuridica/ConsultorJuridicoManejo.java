package models.manejoDigital.analise.analiseJuridica;

import models.portalSeguranca.Usuario;
import play.data.validation.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(schema = "analise", name = "consultor_juridico_manejo")
public class ConsultorJuridicoManejo {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.consultor_juridico_manejo_id_seq")
    @SequenceGenerator(name="analise.consultor_juridico_manejo_id_seq", sequenceName="analise.consultor_juridico_manejo_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name = "data_vinculacao")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataVinculacao;

    @Required
    @OneToOne
    @JoinColumn(name="id_analise_juridica_manejo")
    public AnaliseJuridicaManejo analiseJuridicaManejo;

    @Required
    @ManyToOne
    @JoinColumn(name="id_usuario")
    public Usuario usuario;
}