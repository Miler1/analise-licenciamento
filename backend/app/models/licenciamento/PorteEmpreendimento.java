package models.licenciamento;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "porte_empreendimento")
public class PorteEmpreendimento extends Model {

	public String nome;
	
	public String codigo;
	
}
