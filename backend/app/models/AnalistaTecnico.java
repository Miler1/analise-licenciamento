package models;

import exceptions.ValidacaoException;
import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Setor;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import security.Auth;
import utils.Mensagem;

import javax.persistence.*;
import javax.xml.ws.WebServiceException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema="analise", name="analista_tecnico")
public class AnalistaTecnico extends GenericModel {
	
	public static final String SEQ = "analise.analista_tecnico_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@OneToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_usuario")
	public UsuarioAnalise usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;
	
	public AnalistaTecnico() {
		
	}
	
	public AnalistaTecnico(AnaliseTecnica analiseTecnica, UsuarioAnalise usuario) {
		
		super();
		this.analiseTecnica = analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();
		
	}

	public static AnalistaTecnico distribuicaoAutomaticaAnalistaTecnico(String setorAtividade, Analise analise) {

		UsuarioAnalise.atualizaUsuariosAnalise();

		List<UsuarioAnalise> usuariosAnalise = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.ANALISTA_TECNICO, setorAtividade);

		if (usuariosAnalise == null || usuariosAnalise.size() == 0)
			throw new WebServiceException("Não existe nenhum analista técnico ativado no sistema");

		List<Long> idsAnalistasTecnico = usuariosAnalise.stream()
				.map(ang->ang.id)
				.collect(Collectors.toList());

		String parameter = "ARRAY["+ getParameterLongAsStringDBArray(idsAnalistasTecnico) +"]";

		String sql = "WITH t1 AS (SELECT 0 as count, id_usuario, now() as dt_vinculacao FROM unnest("+parameter+") as id_usuario ORDER BY id_usuario), " +
				"     t2 AS (SELECT * FROM t1 WHERE t1.id_usuario NOT IN (SELECT id_usuario FROM analise.analista_tecnico at) LIMIT 1), " +
				"     t3 AS (SELECT count(id), id_usuario, min(data_vinculacao) as dt_vinculacao FROM analise.analista_tecnico " +
				"        WHERE id_usuario in ("+ getParameterLongAsStringDBArray(idsAnalistasTecnico) +") " +
				"        GROUP BY id_usuario " +
				"        ORDER BY 1, dt_vinculacao OFFSET 0 LIMIT 1) " +
				"SELECT * FROM (SELECT * FROM t2 UNION ALL SELECT * FROM t3) AS t ORDER BY t.count LIMIT 1;";

		Query consulta = JPA.em().createNativeQuery(sql, DistribuicaoProcessoVO.class);

		DistribuicaoProcessoVO distribuicaoProcessoVO = (DistribuicaoProcessoVO) consulta.getSingleResult();

		return new AnalistaTecnico(analise, UsuarioAnalise.findById(distribuicaoProcessoVO.id));

	}

	public AnalistaTecnico(Analise analise, UsuarioAnalise usuario) {

		super();
		this.analiseTecnica = analise.analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();

	}

	private static String getParameterLongAsStringDBArray(List<Long> lista) {

		String retorno = "";

		for (Long id : lista) {
			retorno = retorno + "" + id + ", ";
		}
		retorno = retorno.substring(0, retorno.length() -2) ;

		return retorno;
	}
	
	public static void vincularAnalise(UsuarioAnalise usuario, AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioExecutor, String justificativaCoordenador) {

		if (!usuario.hasPerfil(CodigoPerfil.ANALISTA_TECNICO))
			throw new ValidacaoException(Mensagem.ANALISTA_DIFERENTE_DE_ANALISTA_TECNICO);
		
		/**
		 * A justificativa é somente obrigatória para o coordenador que vincula uma analista técnico
		 */
		if (usuarioExecutor.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.COORDENADOR_TECNICO)) {
			
			if (justificativaCoordenador == null || justificativaCoordenador.isEmpty()){
				throw new ValidacaoException(Mensagem.ANALISTA_JUSTIFICATIVA_COORDENADOR_OBRIGATORIA);
			}
			
		}
		
		AnalistaTecnico analistaTecnico = new AnalistaTecnico(analiseTecnica, usuario);
		analistaTecnico.save();
		
		/**
		 * Se for o coordenador o executor da vinculação, então atribui o usuário executor para o campo do coordenador,
		 * caso contrário atribui o usuário executor para o campo do coordenador. 
		 */
		if (usuarioExecutor.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.COORDENADOR)) {

			analiseTecnica.usuarioValidacaoCoordenador = usuarioExecutor;

		}
		

	}
	
	public AnalistaTecnico gerarCopia() {
		
		AnalistaTecnico copia = new AnalistaTecnico();
		
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

	public static List<UsuarioAnalise> buscarAnalistasTecnicoParaDesvinculo(String setorAtividade, Long idUltimoAnalistaTecnico) {

		List<UsuarioAnalise> usuarios = UsuarioAnalise.findUsuariosByPerfilAndSetor(CodigoPerfil.ANALISTA_TECNICO, setorAtividade);

		return usuarios.stream().filter(usuario -> !usuario.id.equals(Auth.getUsuarioSessao().id) && !usuario.id.equals(idUltimoAnalistaTecnico)).collect(Collectors.toList());

	}

	public static AnalistaTecnico findByAnaliseTecnica(Long idAnaliseTecnica) {

		return AnalistaTecnico.find("id_analise_tecnica = :analiseTecnica")
				.setParameter("analiseTecnica", idAnaliseTecnica).first();
	}
}
