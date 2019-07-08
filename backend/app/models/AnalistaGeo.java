package models;

import exceptions.ValidacaoException;
import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Setor;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="analise", name="analista_geo")
public class AnalistaGeo extends GenericModel {

    public static final String SEQ = "analise.analista_geo_id_seq";

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

    public AnalistaGeo() {

    }

    public AnalistaGeo(AnaliseGeo analiseGeo, UsuarioAnalise usuario) {

        super();
        this.analiseGeo = analiseGeo;
        this.usuario = usuario;
        this.dataVinculacao = new Date();

    }

    public static void vincularAnalise(UsuarioAnalise usuario, AnaliseGeo analiseGeo, UsuarioAnalise usuarioExecutor, String justificativaCoordenador) {

        if (!usuario.hasPerfil(CodigoPerfil.ANALISTA_GEO))
            throw new ValidacaoException(Mensagem.ANALISTA_DIFERENTE_DE_ANALISTA_GEO);

        /**
         * A justificativa é somente obrigatória para o coordenador que vincula uma analista geo
         */
        if (usuarioExecutor.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.COORDENADOR_GEO)) {

            if (justificativaCoordenador == null || justificativaCoordenador.isEmpty()){
                throw new ValidacaoException(Mensagem.ANALISTA_JUSTIFICATIVA_COORDENADOR_OBRIGATORIA);
            }

            analiseGeo.justificativaCoordenador = justificativaCoordenador;
        }

        AnalistaGeo analistaGeo = new AnalistaGeo(analiseGeo, usuario);
        analistaGeo.save();

        /**
         * Se for o gerente o executor da vinculação, então atribui o usuário executor para o campo do gerente,
         * caso contrário atribui o usuário executor para o campo do coordenador.
         */
        if (usuarioExecutor.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.GERENTE)){

            analiseGeo.usuarioValidacaoGerente = usuarioExecutor;

        } else {

            analiseGeo.usuarioValidacao = usuarioExecutor;
        }

        analiseGeo._save();
    }

    public AnalistaGeo gerarCopia() {

        AnalistaGeo copia = new AnalistaGeo();

        copia.usuario = this.usuario;
        copia.dataVinculacao = this.dataVinculacao;

        return copia;
    }

    public Setor getSetor() {
//TODO REFACTOR

//		PerfilUsuario perfil = PerfilUsuario.find("usuario.id = :x AND perfil.nome = :y")
//				.setParameter("x", this.usuario.id)
//				.setParameter("y", "Analista TÉCNICO")
//				.first();
//
//		return perfil.setor;

        return null;
    }
}
