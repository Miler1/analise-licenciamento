package models;

import play.db.jpa.GenericModel;
import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "equipe_vistoria")
public class EquipeVistoria extends GenericModel {

	@Id
	@ManyToOne
	@JoinColumn(name = "id_vistoria")
	public Vistoria vistoria;

	@Id
	@OneToOne
	@JoinColumn(name = "id_usuario")
	public UsuarioAnalise usuario;

}
