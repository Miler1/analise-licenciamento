package models;

import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "perfil_usuario_analise")
public class PerfilUsuarioAnalise extends GenericModel {

	public static final String SEQ = "analise.perfil_usuario_analise_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	@Column(name="id")
	public Long id;

	@ManyToOne
	@JoinColumn(name = "id_usuario_analise", referencedColumnName = "id")
	public UsuarioAnalise usuarioAnalise;

	@Column(name="codigo_perfil")
	public String codigoPerfil;

	@Column(name="nome_perfil")
	public String nomeCodigo;

	public PerfilUsuarioAnalise (Perfil perfil, UsuarioAnalise usuarioAnalise){
		this.usuarioAnalise = usuarioAnalise;
		this.codigoPerfil = perfil.codigo;
		this.nomeCodigo = perfil.nome;
	}

	public PerfilUsuarioAnalise (){

	}

}
