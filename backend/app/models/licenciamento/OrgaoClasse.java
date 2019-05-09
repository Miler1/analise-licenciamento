package models.licenciamento;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "orgao_classe")
public class OrgaoClasse extends Model {

	public String nome;

}