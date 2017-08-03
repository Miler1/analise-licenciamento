package models.portalSeguranca;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import models.licenciamento.PessoaFisica;
import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

@Entity
@Table(schema = "portal_seguranca", name = "usuario")
public class Usuario extends GenericModel  {

	public static final int SENHA_TAMANHO_MINIMO = 6;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_usuario")
	@SequenceGenerator(name = "sq_usuario", sequenceName = "portal_seguranca.sq_usuario", allocationSize = 1)
	public Long id;

	@Required(message = "usuarios.validacao.login.req")
	@MaxSize(value = 20)
	public String login;

	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	public Boolean ativo;

	@MinSize(value = 6, message = "usuarios.validacao.usuario.tamanhoSenha")
	public String senha;

	@Column(name = "trocar_senha")
	public Boolean trocarSenha;

	@Column(name = "data_atualizacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataAtualizacao;

	@Required(message = "usuarios.validacao.pessoa.req")	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
	public PessoaFisica pessoa;
	
	@OneToMany(mappedBy="usuario", orphanRemoval = true)
	public List<PerfilUsuario> perfisUsuario;	
	
	@Transient
	public Perfil perfilSelecionado;
	
	@Transient
	public Setor setorSelecionado;
		
	public static transient ExecutorService executorService = new ScheduledThreadPoolExecutor(Integer.valueOf(Play.configuration.getProperty("usuario.threads", "3")));

	@Transient
	private List<Integer> permittedActionsIds;

	//-- Usuario Sessao para logar no licenciamento

	@Transient
	public Long idPessoa;

	@Transient
	public String nome;

	@Transient
	public String cpfCnpj;

	@Transient
	public List<String> acoesPermitidas;
	
	public static List<Usuario> getUsuariosByPerfil(Integer idPerfil) {
		
		return Usuario.find("SELECT u FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p WHERE p.id = ?", idPerfil).fetch();
	}
	
	public static List<Usuario> getUsuariosByPerfilSetor(Integer idPerfil, Integer idSetor) {
		
		return Usuario.find("SELECT u FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p JOIN pu.setor s where p.id = ? and s.id = ? ",idPerfil, idSetor).fetch();
	}
	
	public static List<Usuario> getUsuariosByPerfilSetores(Integer idPerfil, List<Integer> idsSetores) {
		
		Query query = JPA.em().createQuery("SELECT u FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p JOIN pu.setor s where p.id = :idPerfil and s.id IN (:idsSetores)");
		
		query.setParameter("idPerfil", idPerfil);
		query.setParameter("idsSetores", idsSetores);
		
		return query.getResultList();
	}
	
	public boolean hasPerfil(Integer idPerfil){
		
		for (PerfilUsuario perfilUsuario : perfisUsuario) {
			
			if (perfilUsuario.perfil.id.equals(idPerfil)) {
				return true;
			}
		}
		
		return false;
	}
}
