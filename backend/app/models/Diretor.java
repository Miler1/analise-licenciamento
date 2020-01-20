package models;

import exceptions.PermissaoNegadaException;
import models.EntradaUnica.CodigoPerfil;
import play.Logger;
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
@Table(schema="analise", name="diretor")
public class Diretor extends GenericModel {

	public static final String SEQ = "analise.diretor_id_seq";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;

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

	public Diretor() {

	}

	public Diretor(Analise analise, UsuarioAnalise usuario) {

		super();
		this.analiseTecnica = analise.analiseTecnica;
		this.analiseGeo = analise.analiseGeo;
		this.usuario = usuario;
		this.dataVinculacao = new Date();

	}


	public static Diretor distribuicaoAutomaticaDiretor(String setorAtividade, Analise analise) {

		UsuarioAnalise.atualizaUsuariosAnalise();

		List<UsuarioAnalise> usuariosAnalise = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.DIRETOR, setorAtividade);

		if (usuariosAnalise.isEmpty()) {

			Logger.info(Mensagem.NENHUM_DIRETOR_ENCONTRADO.getTexto(analise.processo.numero, setorAtividade));

			return null;

		}

		List<Long> idsDiretores= usuariosAnalise.stream()
				.map(ang -> ang.id)
				.collect(Collectors.toList());

		String parameter = "ARRAY[" + getParameterLongAsStringDBArray(idsDiretores) + "]";

		String sql = "WITH t1 AS (SELECT 0 as count, id_usuario, now() as dt_vinculacao FROM unnest(" + parameter + ") as id_usuario ORDER BY id_usuario), " +
				"     t2 AS (SELECT * FROM t1 WHERE t1.id_usuario NOT IN (SELECT id_usuario FROM analise.diretor d) LIMIT 1), " +
				"     t3 AS (SELECT count(id), id_usuario, min(data_vinculacao) as dt_vinculacao FROM analise.diretor " +
				"        WHERE id_usuario in (" + getParameterLongAsStringDBArray(idsDiretores) + ") " +
				"        GROUP BY id_usuario " +
				"        ORDER BY 1, dt_vinculacao OFFSET 0 LIMIT 1) " +
				"SELECT * FROM (SELECT * FROM t2 UNION ALL SELECT * FROM t3) AS t ORDER BY t.count LIMIT 1;";

		Query consulta = JPA.em().createNativeQuery(sql, DistribuicaoProcessoVO.class);

		DistribuicaoProcessoVO distribuicaoProcessoVO = (DistribuicaoProcessoVO) consulta.getSingleResult();

		return new Diretor(analise, UsuarioAnalise.findById(distribuicaoProcessoVO.id));

	}

	private static String getParameterLongAsStringDBArray(List<Long> lista) {

		String retorno = "";

		for (Long id : lista) {
			retorno = retorno + "" + id + ", ";
		}
		retorno = retorno.substring(0, retorno.length() -2) ;

		return retorno;
	}

}
