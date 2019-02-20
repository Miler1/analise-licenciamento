package models.licenciamento;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "estado_civil")
public class EstadoCivil extends Model {

	public String nome;
}
