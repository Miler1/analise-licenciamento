package models.licenciamento;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(schema = "licenciamento", name = "porte_empreendimento")
public class PorteEmpreendimento extends Model {

	public String nome;
	
	public String codigo;
	
}
