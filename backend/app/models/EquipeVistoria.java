package models;

import play.db.jpa.GenericModel;
import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "equipe_vistoria")
public class EquipeVistoria extends GenericModel {

	public static final String SEQ = "analise.equipe_vistoria_id_seq";

	@Id
	@ManyToOne
	@JoinColumn(name = "id_vistoria")
	public Vistoria vistoria;

	@OneToOne
	@JoinColumn(name = "id_usuario")
	public UsuarioAnalise usuario;

}
