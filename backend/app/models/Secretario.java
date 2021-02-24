package models;

import models.EntradaUnica.CodigoPerfil;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.Mensagem;

import javax.persistence.*;
import javax.xml.ws.WebServiceException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "analise", name = "secretario")
public class Secretario extends GenericModel {

    public static final String SEQ = "analise.diretor_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "id_analise")
    public Analise analise;

    @Required
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    public UsuarioAnalise usuario;

    @Required
    @Column(name = "data_vinculacao")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataVinculacao;

    public Secretario() {

    }

    public Secretario(Analise analise, UsuarioAnalise usuario) {

        super();
        this.analise = analise;
        this.usuario = usuario;
        this.dataVinculacao = new Date();

    }


    public static Secretario distribuicaoAutomaticaSecretario(Analise analise) {

        UsuarioAnalise.atualizaUsuariosAnalise();

        List<UsuarioAnalise> usuariosAnalise = UsuarioAnalise.findUsuariosByPerfil(CodigoPerfil.SECRETARIO);

        if (usuariosAnalise.isEmpty())
            throw new WebServiceException(Mensagem.NENHUM_SECRETARIO_ENCONTRADO.getTexto(analise.processo.numero));


        List<Long> idsSecretario = usuariosAnalise.stream()
                .map(ang -> ang.id)
                .collect(Collectors.toList());

        String parameter = "ARRAY[" + getParameterLongAsStringDBArray(idsSecretario) + "]";

        String sql = "WITH t1 AS (SELECT 0 as count, id_usuario, now() as dt_vinculacao FROM unnest(" + parameter + ") as id_usuario ORDER BY id_usuario), " +
                "     t2 AS (SELECT * FROM t1 WHERE t1.id_usuario NOT IN (SELECT id_usuario FROM analise.secretario d) LIMIT 1), " +
                "     t3 AS (SELECT count(id), id_usuario, min(data_vinculacao) as dt_vinculacao FROM analise.secretario " +
                "        WHERE id_usuario in (" + getParameterLongAsStringDBArray(idsSecretario) + ") " +
                "        GROUP BY id_usuario " +
                "        ORDER BY 1, dt_vinculacao OFFSET 0 LIMIT 1) " +
                "SELECT * FROM (SELECT * FROM t2 UNION ALL SELECT * FROM t3) AS t ORDER BY t.count LIMIT 1;";

        Query consulta = JPA.em().createNativeQuery(sql, DistribuicaoProcessoVO.class);

        DistribuicaoProcessoVO distribuicaoProcessoVO = (DistribuicaoProcessoVO) consulta.getSingleResult();

        return new Secretario(analise, UsuarioAnalise.findById(distribuicaoProcessoVO.id));

    }

    private static String getParameterLongAsStringDBArray(List<Long> lista) {

        StringBuilder retorno = new StringBuilder();

        for (Long id : lista) {

            retorno.append(id).append(", ");

        }
        retorno = new StringBuilder(retorno.substring(0, retorno.length() - 2));

        return retorno.toString();
    }

}