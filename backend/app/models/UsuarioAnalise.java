package models;

import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import models.EntradaUnica.Usuario;
import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import services.IntegracaoEntradaUnicaService;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
	
	public static List<UsuarioAnalise> getUsuariosByPerfilSetor(String codigoPerfil, String siglaSetor) {

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
}
