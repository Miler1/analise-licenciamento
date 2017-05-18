package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import models.licenciamento.Documento;

@Entity
@Table(schema="anlise", name="analise_documento")
public class AnaliseDocumento extends GenericModel {
	
	public static final String SEQ = "analise_documento_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name="id_analise_juridica")
	public AnaliseJuridica analiseJuridica;
	
	
	public Boolean validado;
	
	public String parecer;
	
	@ManyToOne
	@JoinColumn(name="id_documento")
	public Documento documento;
}
