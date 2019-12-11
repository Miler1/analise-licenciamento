package models;

import enums.PerfilEnum;
import enums.SetorEnum;
import exceptions.PortalSegurancaException;
import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Usuario;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import security.cadastrounificado.CadastroUnificadoWS;
import services.IntegracaoEntradaUnicaService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Entity
@Table(schema = "analise", name = "usuario_analise")
public class UsuarioAnalise extends GenericModel  {

	public static final String SEQ = "analise.usuario_analise_id_seq";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@Column(name="login")
	@Required
	@MaxSize(value = 14)
	public String login;

	@OneToOne
	@JoinColumn(name = "id_pessoa")
	public Pessoa pessoa;

	@OneToMany(mappedBy="usuarioAnalise", fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	public List <SetorUsuarioAnalise> setores;

	@OneToMany(mappedBy="usuarioAnalise", fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	public List <PerfilUsuarioAnalise> perfis;

	public static transient ExecutorService executorService = new ScheduledThreadPoolExecutor(Integer.valueOf(Play.configuration.getProperty("usuario.threads", "3")));

	@Transient
	private List<Integer> permittedActionsIds;

	@Transient
	public Usuario usuarioEntradaUnica;

	@Transient
	public String nome;

	public static UsuarioAnalise getUsuarioAnaliseById(Long id) {

		return UsuarioAnalise.findById(id);

	}

	public static UsuarioAnalise getUsuarioEntradaUnicaByLogin(String login) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica= new IntegracaoEntradaUnicaService();

		return integracaoEntradaUnica.findUsuarioByLogin(login);
	}

	public static List<UsuarioAnalise> getUsuariosByPerfil(String codigoPerfil) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica= new IntegracaoEntradaUnicaService();

		return integracaoEntradaUnica.findUsuariosByPerfil(codigoPerfil);
	}
	
	public static List<UsuarioAnalise> getUsuariosEntradaUnica(String codigoPerfil, String siglaSetor) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		return integracaoEntradaUnica.findUsuariosByPerfilAndSetor(codigoPerfil, siglaSetor);
	}

	public static List<UsuarioAnalise> getUsuariosByPerfilSetores(String codigoPerfil, List<String> siglasSetores) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		return integracaoEntradaUnica.findUsuariosByPerfilAndSetores(codigoPerfil, siglasSetores);
	}
	
	public boolean hasPerfil(String codigoPerfil){

		for (Perfil perfil : this.usuarioEntradaUnica.perfis) {

			if (perfil.codigo.equals(codigoPerfil)) {

				return true;
			}
		}

		return false;
	}

	public static UsuarioAnalise findByGerente(Gerente gerente) {

		return UsuarioAnalise.find("id = :id_gerente")
				.setParameter("id_gerente", gerente.usuario.id).first();
	}

	public static UsuarioAnalise findByAnalistaTecnico(AnalistaTecnico analistaTecnico) {

		return UsuarioAnalise.find("id = :id_analista_tecnico")
			.setParameter("id_analista_tecnico", analistaTecnico.usuario.id).first();
	}

	public static UsuarioAnalise findByAnalistaGeo(AnalistaGeo analistaGeo) {

		return UsuarioAnalise.find("id = :id_analista_geo")
				.setParameter("id_analista_geo", analistaGeo.usuario.id).first();
	}

	public static List<UsuarioAnalise> findUsuariosByPerfilAndSetor(String codigoPerfil, String siglaSetor) {

		return UsuarioAnalise.find("SELECT DISTINCT u FROM UsuarioAnalise u " +
				"LEFT JOIN PerfilUsuarioAnalise p ON p.usuarioAnalise.id = u.id " +
				"LEFT JOIN SetorUsuarioAnalise s ON s.usuarioAnalise.id = u.id " +
				"WHERE p.codigoPerfil = :codigoPerfil AND s.siglaSetor = :siglaSetor")
				.setParameter("codigoPerfil", codigoPerfil)
				.setParameter("siglaSetor", siglaSetor)
				.fetch();

	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;

	}

	public static void atualizaUsuariosAnalise() {

		List<UsuarioAnalise> usuariosAnalise = UsuarioAnalise.findAll();
		List<Usuario> usuariosEU = new ArrayList<>();
		PerfilEnum.getList().forEach(perfil -> usuariosEU.addAll(CadastroUnificadoWS.ws.getUsuariosByPerfil(perfil).stream().map(Usuario::new).collect(Collectors.toList())));
		List<Usuario> usuariosFiltrados = usuariosEU.stream().filter(distinctByKey(usuario -> usuario.login)).collect(Collectors.toList());

		for (UsuarioAnalise usuarioAnalise : usuariosAnalise) {

			Usuario usuario = usuariosFiltrados.stream().filter(usuarioEU -> usuarioEU.login.equals(usuarioAnalise.login)).findAny().orElseThrow(PortalSegurancaException::new);

			usuarioAnalise.perfis = usuarioAnalise.salvarPerfis(usuario);
			usuarioAnalise.setores = usuarioAnalise.salvarSetores(usuario);
			usuarioAnalise._save();

		}

	}

	private List<PerfilUsuarioAnalise> salvarPerfis(Usuario usuario) {

		this.perfis.forEach(PerfilUsuarioAnalise::_delete);
		this.perfis.clear();
		this._save();

		usuario.perfis.forEach(perfil -> {

			if(PerfilEnum.getList().contains(perfil.codigo)) {

				PerfilUsuarioAnalise perfilUsuarioAnalise = new PerfilUsuarioAnalise(perfil, this);
				this.perfis.add(perfilUsuarioAnalise.save());

			}

		});

		return this.perfis;

	}

	private List<SetorUsuarioAnalise> salvarSetores(Usuario usuario) {

		this.setores.forEach(SetorUsuarioAnalise::_delete);
		this.setores.clear();
		this._save();

		usuario.setores.forEach(setor -> {

			if(SetorEnum.getList().contains(setor.sigla)) {

				SetorUsuarioAnalise setorUsuarioAnalise = new SetorUsuarioAnalise(setor, this);
				this.setores.add(setorUsuarioAnalise.save());

			}

		});

		return this.setores;

	}
}
