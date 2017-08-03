package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.portalSeguranca.Setor;

import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "atividade_cnae")
public class AtividadeCnae extends GenericModel {

	@Id
	public Long id;

	public String nome;

	public String codigo;
	
}
