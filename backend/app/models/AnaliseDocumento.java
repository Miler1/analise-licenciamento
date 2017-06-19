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
import utils.Identificavel;
import models.licenciamento.DocumentoLicenciamento;

@Entity
@Table(schema="analise", name="analise_documento")
public class AnaliseDocumento extends GenericModel implements Identificavel {
	
	public static final String SEQ = "analise.analise_documento_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name="id_analise_juridica", nullable=true)
	public AnaliseJuridica analiseJuridica;
	
	
	public Boolean validado;
	
	public String parecer;
	
	@ManyToOne
	@JoinColumn(name="id_documento")
	public DocumentoLicenciamento documento;
	
	@ManyToOne
	@JoinColumn(name="id_analise_tecnica", nullable=true)
	public AnaliseTecnica analiseTecnica;	
	
	public void update(AnaliseDocumento novaAnalise) {
		
		this.parecer = novaAnalise.parecer;
		this.validado = novaAnalise.validado;
	}


	public AnaliseDocumento gerarCopia() {
		
		AnaliseDocumento copia = new AnaliseDocumento();
		
		copia.validado = this.validado;
		copia.parecer = this.parecer;
		copia.documento = this.documento;
		
		return copia;
	}


	@Override
	public Long getId() {
		
		return this.id;
	}
	
}
