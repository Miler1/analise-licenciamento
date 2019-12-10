package models;

import br.ufla.lemaf.beans.pessoa.Setor;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "setor_usuario_analise")
public class SetorUsuarioAnalise extends GenericModel {

	public static final String SEQ = "analise.setor_usuario_analise_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	@Column(name="id")
	public Long id;

	@ManyToOne
	@JoinColumn(name = "id_usuario_analise", referencedColumnName = "id")
	public UsuarioAnalise usuarioAnalise;

	@Column(name = "sigla_setor")
	public String siglaSetor;

	@Column(name = "nome_setor")
	public String nomeSetor;



	public SetorUsuarioAnalise (Setor setor, UsuarioAnalise usuarioAnalise){

		this.usuarioAnalise = usuarioAnalise;
		this.siglaSetor = setor.sigla;
		this.nomeSetor = setor.nome;
		this.save();

	}

	public SetorUsuarioAnalise (){

	}
}
