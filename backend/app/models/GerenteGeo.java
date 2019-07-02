package models;

import exceptions.PermissaoNegadaException;
import models.EntradaUnica.CodigoPerfil;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="analise", name="gerente_geo")
public class GerenteGeo extends GenericModel {

    public static final String SEQ = "analise.gerente_geo_id_seq";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @Required
    @ManyToOne
    @JoinColumn(name="id_analise_geo")
    public AnaliseGeo analiseGeo;

    @Required
    @ManyToOne
    @JoinColumn(name="id_usuario")
    public UsuarioAnalise usuario;

    @Required
    @Column(name="data_vinculacao")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataVinculacao;

    public GerenteGeo() {

    }

    public GerenteGeo(AnaliseGeo analiseGeo, UsuarioAnalise usuario) {

        super();
        this.analiseGeo = analiseGeo;
        this.usuario = usuario;
        this.dataVinculacao = new Date();

    }

    public static void vincularAnalise(UsuarioAnalise usuario, UsuarioAnalise usuarioExecutor, AnaliseGeo analiseGeo) {

        if (!usuario.hasPerfil(CodigoPerfil.GERENTE_GEO))
            throw new PermissaoNegadaException(Mensagem.GERENTE_DIFERENTE_DE_GERENTE_GEO);

        GerenteGeo gerenteGeo = new GerenteGeo(analiseGeo, usuario);
        gerenteGeo.save();

        analiseGeo.usuarioValidacao = usuarioExecutor;
        analiseGeo._save();
    }

    public GerenteGeo gerarCopia() {

        GerenteGeo copia = new GerenteGeo();

        copia.usuario = this.usuario;
        copia.dataVinculacao = this.dataVinculacao;

        return copia;
    }
}
