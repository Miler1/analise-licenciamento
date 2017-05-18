package models.licenciamento;


import play.db.jpa.GenericModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "tipologia")
public class Tipologia  extends GenericModel {

	@Id
	public Long id;

	public String nome;

}
