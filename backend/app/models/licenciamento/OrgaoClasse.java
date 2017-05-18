package models.licenciamento;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(schema = "licenciamento", name = "orgao_classe")
public class OrgaoClasse extends Model {

	public String nome;

}