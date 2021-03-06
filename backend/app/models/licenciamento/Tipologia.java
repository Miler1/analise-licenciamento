package models.licenciamento;


import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "tipologia")
public class Tipologia  extends GenericModel {

//	public static Long ID_AGROSSILVIPASTORIL = 1l;
//	public static Long ID_INDUSTRIA_MADEIREIRA = 11l;
//	public static String ID_AQUICULTURA = "AQUICULTURA";

	@Id
	public Long id;

	public String nome;

	@OneToOne
	@JoinColumn(name = "id_diretoria")
	public Diretoria diretoria;

//	@Column(name="codigo")
	public String codigo;

}
