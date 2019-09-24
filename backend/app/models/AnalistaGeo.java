package models;

import exceptions.ValidacaoException;
import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Setor;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.Mensagem;

import javax.persistence.*;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    //TODO PUMA-SQUAD-1 ajustar busca de setor do analista

    //		PerfilUsuario perfil = PerfilUsuario.find("usuario.id = :x AND perfil.nome = :y")
    //				.setParameter("x", this.usuario.id)
    //				.setParameter("y", "Analista TÉCNICO")
    //				.first();
    //
    //		return perfil.setor;

        return null;
    }

    public static AnalistaGeo distribuicaoProcesso(String setorAtividade, AnaliseGeo analiseGeo) {

        List<UsuarioAnalise> analistasGeo = UsuarioAnalise.getUsuariosByPerfilSetor(CodigoPerfil.ANALISTA_GEO, setorAtividade);

        if (analistasGeo == null || analistasGeo.size() == 0)
            throw new WebServiceException("Não existe nenhum analista geo para vincular automáticamente o processo.");

        List<Long> idsAnalistasGeo = analistasGeo.stream()
                .map(ang -> ang.id)
                .collect(Collectors.toList());

        String parameter = "ARRAY[" + getParameterLongAsStringDBArray(idsAnalistasGeo) + "]";

        String sql = "WITH t1 AS (SELECT 0 as count, id_usuario, now() as dt_vinculacao FROM unnest(" + parameter + ") as id_usuario ORDER BY id_usuario), " +
                "     t2 AS (SELECT * FROM t1 WHERE t1.id_usuario NOT IN (SELECT id_usuario FROM analise.analista_geo ag) LIMIT 1), " +
                "     t3 AS (SELECT count(id), id_usuario, min(data_vinculacao) as dt_vinculacao FROM analise.analista_geo " +
                "        WHERE id_usuario in (" + getParameterLongAsStringDBArray(idsAnalistasGeo) + ") " +
                "        GROUP BY id_usuario " +
                "        ORDER BY 1, dt_vinculacao OFFSET 0 LIMIT 1) " +
                "SELECT * FROM (SELECT * FROM t2 UNION ALL SELECT * FROM t3) AS t ORDER BY t.count LIMIT 1;";

        Query consulta = JPA.em().createNativeQuery(sql, DistribuicaoProcessoVO.class);

        DistribuicaoProcessoVO distribuicaoProcessoVO = (DistribuicaoProcessoVO) consulta.getSingleResult();

        return new AnalistaGeo(analiseGeo, UsuarioAnalise.findById(distribuicaoProcessoVO.id));

    }

    private static String getParameterLongAsStringDBArray(List<Long> lista) {

        String retorno = "";

        for (Long id : lista) {
            retorno = retorno + "" + id + ", ";
        }
        retorno = retorno.substring(0, retorno.length() -2) ;

        return retorno;
    }

    public static List<UsuarioAnalise> buscarAnalistasGeoByIdProcesso(String setorAtividade) {

        List<UsuarioAnalise> todosAnalistasGeo = UsuarioAnalise.getUsuariosByPerfilSetor(CodigoPerfil.ANALISTA_GEO, setorAtividade);

        List<UsuarioAnalise> analistasGeo = new ArrayList<>();

        for (UsuarioAnalise analistaGeo : todosAnalistasGeo) {
            if(analistaGeo.pessoa != null) {
                analistasGeo.add(analistaGeo);
            }
        }

        return analistasGeo;
    }
}
