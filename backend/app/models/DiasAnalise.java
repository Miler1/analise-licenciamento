package models;

import play.db.jpa.GenericModel;

import javax.persistence.*;

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
	
	@Column(name="quantidade_dias_juridica")
	public Integer qtdeDiasJuridica;
	
	@Column(name="quantidade_dias_tecnica")
	public Integer qtdeDiasTecnica;

	@Column(name="quantidade_dias_geo")
	public Integer qtdeDiasGeo;

	@Column(name="quantidade_dias_analise")
	public Integer qtdeDiasAnalise;
	
	@Column(name="quantidade_dias_aprovador")
	public Integer qtdeDiasAprovador;
	
	@Column(name="quantidade_dias_notificacao")
	public Integer qtdeDiasNotificacao;
	
	public DiasAnalise(Analise analise) {
		
		if(this.analise == null){
			
			this.analise = analise;
			this.qtdeDiasAnalise = 0;
			this.qtdeDiasJuridica = 0;
			this.qtdeDiasGeo = 0;
		}
		
	}
	
	public DiasAnalise() {
		
	}
}