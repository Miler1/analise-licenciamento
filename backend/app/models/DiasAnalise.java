package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(schema="analise", name="dia_analise")
public class DiasAnalise extends GenericModel{
	
	public static final String SEQ = "analise.dia_analise_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;	
	
	@OneToOne
	@JoinColumn(name="id_analise")
	public Analise analise;
	
	@Column(name="qtde_dias_juridica")
	public Integer qtdeDiasJuridica;
	
	@Column(name="qtde_dias_tecnica")
	public Integer qtdeDiasTecnica;
	
	@Column(name="qtde_dias_analise")
	public Integer qtdeDiasAnalise;

}