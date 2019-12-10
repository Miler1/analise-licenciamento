package models;

import exceptions.PortalSegurancaException;
import exceptions.ValidacaoException;
import jobs.ProcessamentoCaracterizacaoEmAndamento;
import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Setor;
import models.EntradaUnica.Usuario;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.Logger;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.i18n.Messages;
import security.Auth;
import security.cadastrounificado.CadastroUnificadoWS;
import services.IntegracaoEntradaUnicaService;
import utils.Mensagem;

import javax.persistence.*;
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

		List<UsuarioAnalise> analistasGeo = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.ANALISTA_GEO, setorAtividade);

		List<Usuario> usuariosEU = CadastroUnificadoWS.ws.getUsuariosByPerfil(CodigoPerfil.ANALISTA_GEO).stream().map(Usuario::new).collect(Collectors.toList());


		for( UsuarioAnalise analistaGeo : analistasGeo){

			Usuario usuario = usuariosEU.stream().filter(usuarioEU -> usuarioEU.login.equals(analistaGeo.login)).findAny().orElseThrow(PortalSegurancaException::new);

			usuario.salvarPerfis(analistaGeo);
			usuario.salvarSetores(analistaGeo);

		}

		if (analistasGeo.isEmpty()) {

			Logger.info(Mensagem.NENHUM_ANALISTA_ENCONTRADO.getTexto(analiseGeo.analise.processo.numero, setorAtividade));

			return null;

		}

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

	public static AnalistaGeo findByAnaliseGeo(Long idAnaliseGeo) {

		return AnalistaGeo.find("id_analise_geo = :analiseGeo")
				.setParameter("analiseGeo", idAnaliseGeo).first();
	}

	private static String getParameterLongAsStringDBArray(List<Long> lista) {

		String retorno = "";

		for (Long id : lista) {
			retorno = retorno + "" + id + ", ";
		}
		retorno = retorno.substring(0, retorno.length() -2) ;

		return retorno;
	}

	public static List<UsuarioAnalise> buscarAnalistasGeoParaDesvinculo(String setorAtividade, Long idUltimoAnalistaGeo) {

		List<UsuarioAnalise> usuarios = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.ANALISTA_GEO, setorAtividade);

		return usuarios.stream().filter(usuario -> !usuario.id.equals(Auth.getUsuarioSessao().id) && !usuario.id.equals(idUltimoAnalistaGeo)).collect(Collectors.toList());

	}

}
