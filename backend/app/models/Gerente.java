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
@Table(schema="analise", name="gerente")
public class Gerente extends GenericModel {
	
	public static final String SEQ = "analise.gerente_id_seq";

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
	
	public Gerente() {
		
	}
	
	public Gerente(AnaliseTecnica analiseTecnica, UsuarioAnalise usuario) {

		super();
		this.analiseTecnica = analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();

	}

	public Gerente(AnaliseGeo analiseGeo, UsuarioAnalise usuario) {

		super();
		this.analiseGeo = analiseGeo;
		this.usuario = usuario;
		this.dataVinculacao = new Date();

	}

	public static void vincularAnalise(UsuarioAnalise usuario, UsuarioAnalise usuarioExecutor, AnaliseTecnica analiseTecnica) {
		
		if (!usuario.hasPerfil(CodigoPerfil.GERENTE))
			throw new PermissaoNegadaException(Mensagem.GERENTE_DIFERENTE_DE_GERENTE_TECNICO);		
		
		Gerente gerente = new Gerente(analiseTecnica, usuario);
		gerente.save();

		analiseTecnica.usuarioValidacao = usuarioExecutor;
		analiseTecnica._save();
	}

	public static void vincularAnaliseGeo(UsuarioAnalise usuario, UsuarioAnalise usuarioExecutor, AnaliseGeo analiseGeo) {

		if (!usuario.hasPerfil(CodigoPerfil.GERENTE))
			throw new PermissaoNegadaException(Mensagem.GERENTE_DIFERENTE_DE_GERENTE_TECNICO);

		Gerente gerente = new Gerente(analiseGeo, usuario);
		gerente.save();

		analiseGeo.usuarioValidacao = usuarioExecutor;
		analiseGeo._save();
	}
	
	public Gerente gerarCopia() {
		
		Gerente copia = new Gerente();
		
		copia.usuario = this.usuario;
		copia.dataVinculacao = this.dataVinculacao;
		
		return copia;
	}

	public static Gerente distribuicaoAutomaticaGerente(String setorAtividade, AnaliseGeo analiseGeo) {

		List<UsuarioAnalise> gerentes = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.GERENTE, setorAtividade);

		if (gerentes == null || gerentes.size() == 0)
			throw new WebServiceException(Mensagem.NENHUM_GERENTE_ENCONTRADO.getTexto());

		List<Long> idsGerentes = gerentes.stream()
				.map(ang->ang.id)
				.collect(Collectors.toList());

		String parameter = "ARRAY["+ getParameterLongAsStringDBArray(idsGerentes) +"]";

		String sql = "WITH t1 AS (SELECT 0 as count, id_usuario, now() as dt_vinculacao FROM unnest("+parameter+") as id_usuario ORDER BY id_usuario), " +
				"     t2 AS (SELECT * FROM t1 WHERE t1.id_usuario NOT IN (SELECT id_usuario FROM analise.gerente ge) LIMIT 1), " +
				"     t3 AS (SELECT count(id), id_usuario, min(data_vinculacao) as dt_vinculacao FROM analise.gerente " +
				"        WHERE id_usuario in ("+ getParameterLongAsStringDBArray(idsGerentes) +") " +
				"        GROUP BY id_usuario " +
				"        ORDER BY 1, dt_vinculacao OFFSET 0 LIMIT 1) " +
				"SELECT * FROM (SELECT * FROM t2 UNION ALL SELECT * FROM t3) AS t ORDER BY t.count LIMIT 1;";

		Query consulta = JPA.em().createNativeQuery(sql, DistribuicaoProcessoVO.class);

		DistribuicaoProcessoVO distribuicaoProcessoVO = (DistribuicaoProcessoVO) consulta.getSingleResult();

		return new Gerente(analiseGeo, UsuarioAnalise.findById(distribuicaoProcessoVO.id));

	}

	public static Gerente distribuicaoAutomaticaGerenteAnaliseTecnica(String setorAtividade, AnaliseTecnica analiseTecnica) {

		List<UsuarioAnalise> gerentes = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.GERENTE, setorAtividade);

		if (gerentes == null || gerentes.size() == 0)
			throw new WebServiceException(Mensagem.NENHUM_GERENTE_ENCONTRADO.getTexto());

		List<Long> idsGerentes = gerentes.stream()
				.map(ang->ang.id)
				.collect(Collectors.toList());

		String parameter = "ARRAY["+ getParameterLongAsStringDBArray(idsGerentes) +"]";

		String sql = "WITH t1 AS (SELECT 0 as count, id_usuario, now() as dt_vinculacao FROM unnest("+parameter+") as id_usuario ORDER BY id_usuario), " +
				"     t2 AS (SELECT * FROM t1 WHERE t1.id_usuario NOT IN (SELECT id_usuario FROM analise.gerente ge) LIMIT 1), " +
				"     t3 AS (SELECT count(id), id_usuario, min(data_vinculacao) as dt_vinculacao FROM analise.gerente " +
				"        WHERE id_usuario in ("+ getParameterLongAsStringDBArray(idsGerentes) +") " +
				"        GROUP BY id_usuario " +
				"        ORDER BY 1, dt_vinculacao OFFSET 0 LIMIT 1) " +
				"SELECT * FROM (SELECT * FROM t2 UNION ALL SELECT * FROM t3) AS t ORDER BY t.count LIMIT 1;";

		Query consulta = JPA.em().createNativeQuery(sql, DistribuicaoProcessoVO.class);

		DistribuicaoProcessoVO distribuicaoProcessoVO = (DistribuicaoProcessoVO) consulta.getSingleResult();

		return new Gerente(analiseTecnica, UsuarioAnalise.findById(distribuicaoProcessoVO.id));

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
