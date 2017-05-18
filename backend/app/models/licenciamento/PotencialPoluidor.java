package models.licenciamento;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;


@Entity
@Table(schema = "licenciamento", name = "potencial_poluidor")
public class PotencialPoluidor extends Model {

	public String nome;
	
	public String codigo;
	
}
