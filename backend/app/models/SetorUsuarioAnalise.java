package models;

import br.ufla.lemaf.beans.pessoa.Setor;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "setor_usuario_analise")
public class SetorUsuarioAnalise extends GenericModel {

	@Id
	@ManyToOne
	@JoinColumn(name = "id_usuario_analise", referencedColumnName = "id")
	public UsuarioAnalise usuarioAnalise;

	@Id
	@Column(name = "sigla_setor")
	public String siglaSetor;

	@Column(name = "nome_setor")
	public String nomeSetor;

	public SetorUsuarioAnalise (Setor setor, UsuarioAnalise usuarioAnalise){

		this.usuarioAnalise = usuarioAnalise;
		this.siglaSetor = setor.sigla;
		this.nomeSetor = setor.nome;

	}

	public SetorUsuarioAnalise (){ }

}
