package models;

import br.ufla.lemaf.beans.pessoa.Setor;
import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Usuario;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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

	public static UsuarioAnalise getUsuarioByLogin(String login) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica= new IntegracaoEntradaUnicaService();

		return integracaoEntradaUnica.findUsuarioByLogin(login);
	}

	public static List<UsuarioAnalise> getUsuariosByPerfil(String codigoPerfil) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica= new IntegracaoEntradaUnicaService();

		return integracaoEntradaUnica.findUsuariosByPerfil(codigoPerfil);
	}
	
	public static List<UsuarioAnalise> getUsuarios(String codigoPerfil, String siglaSetor) {

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

	public static List<UsuarioAnalise> findUsuariosByPerfilAndSetor(String codigoPerfil, String siglaSetor) {

		List<UsuarioAnalise> usuarios = UsuarioAnalise.getUsuarios(codigoPerfil, siglaSetor);

		return usuarios.stream().filter(usuarioAnalise -> usuarioAnalise.setores.stream().anyMatch(setor -> setor.siglaSetor.equals(siglaSetor) && usuarioAnalise.perfis.stream().anyMatch(perfil -> perfil.codigoPerfil.equals(codigoPerfil)))).collect(Collectors.toList());

	}

}
