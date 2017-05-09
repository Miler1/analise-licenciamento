package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(schema = "licenciamento", name = "estado_civil")
public class EstadoCivil extends Model {

	public String nome;
}
