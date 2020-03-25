package models.licenciamento;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "parametro_atividade")
public class ParametroAtividade extends Model {

	public String nome;
	
	public String codigo;
}
