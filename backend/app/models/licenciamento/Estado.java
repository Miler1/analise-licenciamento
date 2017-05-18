package models.licenciamento;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name="estado")
public class Estado extends GenericModel {
	
	@Id
	@Column(name="cod_estado")
	public String codigo;

	public String nome;
	
}
