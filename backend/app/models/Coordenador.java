package models;

import exceptions.PermissaoNegadaException;
import models.EntradaUnica.CodigoPerfil;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.Mensagem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.ws.WebServiceException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "analise", name = "coordenador")
public class Coordenador extends GenericModel {

    public static final String SEQ = "analise.coordenador_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "id_analise_tecnica")
    public AnaliseTecnica analiseTecnica;

    @ManyToOne
    @JoinColumn(name = "id_analise_geo")
    public AnaliseGeo analiseGeo;

    @Required
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    public UsuarioAnalise usuario;

    @Required
    @Column(name = "data_vinculacao")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataVinculacao;

    public Coordenador() {

    }

    public Coordenador(AnaliseTecnica analiseTecnica, UsuarioAnalise usuario) {

        super();
        this.analiseTecnica = analiseTecnica;
        this.usuario = usuario;
        this.dataVinculacao = new Date();

    }

//    public Coordenador(AnaliseGeo analiseGeo, UsuarioAnalise usuario) {
//
//        super();
//        this.analiseGeo = analiseGeo;
//        this.usuario = usuario;
//        this.dataVinculacao = new Date();
//
//    }

    public static void vincularAnalise(UsuarioAnalise usuario, UsuarioAnalise usuarioExecutor, AnaliseTecnica analiseTecnica) {

        if (!usuario.hasPerfil(CodigoPerfil.COORDENADOR))
            throw new PermissaoNegadaException(Mensagem.COORDENADOR_DIFERENTE_DE_COORDENADOR_TECNICO);

        Coordenador coordenador = new Coordenador(analiseTecnica, usuario);
        coordenador.save();

        analiseTecnica.usuarioValidacao = usuarioExecutor;
        analiseTecnica._save();
    }

//    public static void vincularAnaliseGeo(UsuarioAnalise usuario, UsuarioAnalise usuarioExecutor, AnaliseGeo analiseGeo) {
//
//        if (!usuario.hasPerfil(CodigoPerfil.COORDENADOR))
//            throw new PermissaoNegadaException(Mensagem.COORDENADOR_DIFERENTE_DE_COORDENADOR_TECNICO);
//
//        Coordenador coordenador = new Coordenador(analiseGeo, usuario);
//        coordenador.save();
//
//        analiseGeo.usuarioValidacao = usuarioExecutor;
//        analiseGeo._save();
//    }

    public Coordenador gerarCopia() {

        Coordenador copia = new Coordenador();

        copia.usuario = this.usuario;
        copia.dataVinculacao = this.dataVinculacao;

        return copia;
    }

//    public static Coordenador distribuicaoAutomaticaCoordenador(String setorAtividade, AnaliseGeo analiseGeo) {
//
//        UsuarioAnalise.atualizaUsuariosAnalise();
//
//        List<UsuarioAnalise> usuariosAnalise = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.COORDENADOR_GEO, setorAtividade);
//
//        if (usuariosAnalise == null || usuariosAnalise.isEmpty())
//            throw new WebServiceException(Mensagem.NENHUM_COORDENADOR_ENCONTRADO.getTexto());
//
//        List<Long> idsCoordenadores = usuariosAnalise.stream()
//                .map(ang -> ang.id)
//                .collect(Collectors.toList());
//
//        String parameter = "ARRAY[" + getParameterLongAsStringDBArray(idsCoordenadores) + "]";
//
//        String sql = "WITH t1 AS (SELECT 0 as count, id_usuario, now() as dt_vinculacao FROM unnest(" + parameter + ") as id_usuario ORDER BY id_usuario), " +
//                "     t2 AS (SELECT * FROM t1 WHERE t1.id_usuario NOT IN (SELECT id_usuario FROM analise.coordenador ge) LIMIT 1), " +
//                "     t3 AS (SELECT count(id), id_usuario, min(data_vinculacao) as dt_vinculacao FROM analise.coordenador " +
//                "        WHERE id_usuario in (" + getParameterLongAsStringDBArray(idsCoordenadores) + ") " +
//                "        GROUP BY id_usuario " +
//                "        ORDER BY 1, dt_vinculacao OFFSET 0 LIMIT 1) " +
//                "SELECT * FROM (SELECT * FROM t2 UNION ALL SELECT * FROM t3) AS t ORDER BY t.count LIMIT 1;";
//
//        Query consulta = JPA.em().createNativeQuery(sql, DistribuicaoProcessoVO.class);
//
//        DistribuicaoProcessoVO distribuicaoProcessoVO = (DistribuicaoProcessoVO) consulta.getSingleResult();
//
//        return new Coordenador(analiseGeo, UsuarioAnalise.findById(distribuicaoProcessoVO.id));
//
//    }

    public static Coordenador distribuicaoAutomaticaCoordenadorAnaliseTecnica(String setorAtividade, AnaliseTecnica analiseTecnica) {

        UsuarioAnalise.atualizaUsuariosAnalise();

        List<UsuarioAnalise> coordenadores = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.COORDENADOR, setorAtividade);

        if (coordenadores == null || coordenadores.isEmpty())
            throw new WebServiceException(Mensagem.NENHUM_COORDENADOR_ENCONTRADO.getTexto());

        List<Long> idsCoordenadores = coordenadores.stream()
                .map(ang -> ang.id)
                .collect(Collectors.toList());

        String parameter = "ARRAY[" + getParameterLongAsStringDBArray(idsCoordenadores) + "]";

        String sql = "WITH t1 AS (SELECT 0 as count, id_usuario, now() as dt_vinculacao FROM unnest(" + parameter + ") as id_usuario ORDER BY id_usuario), " +
                "     t2 AS (SELECT * FROM t1 WHERE t1.id_usuario NOT IN (SELECT id_usuario FROM analise.coordenador ge) LIMIT 1), " +
                "     t3 AS (SELECT count(id), id_usuario, min(data_vinculacao) as dt_vinculacao FROM analise.coordenador " +
                "        WHERE id_usuario in (" + getParameterLongAsStringDBArray(idsCoordenadores) + ") " +
                "        GROUP BY id_usuario " +
                "        ORDER BY 1, dt_vinculacao OFFSET 0 LIMIT 1) " +
                "SELECT * FROM (SELECT * FROM t2 UNION ALL SELECT * FROM t3) AS t ORDER BY t.count LIMIT 1;";

        Query consulta = JPA.em().createNativeQuery(sql, DistribuicaoProcessoVO.class);

        DistribuicaoProcessoVO distribuicaoProcessoVO = (DistribuicaoProcessoVO) consulta.getSingleResult();

        return new Coordenador(analiseTecnica, UsuarioAnalise.findById(distribuicaoProcessoVO.id));

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