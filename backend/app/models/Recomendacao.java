package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Identificavel;
import utils.validacao.Validacao;

import javax.persistence.*;

@Entity
@Table(schema="analise", name="recomendacao")
public class Recomendacao extends GenericModel implements Identificavel {
	
	public static final String SEQ = "analise.recomendacao_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_licenca_analise")
	public LicencaAnalise licencaAnalise;
	
	@Required
	public String texto;
	
	@Required
	public Integer ordem;

	@Override
	public Long getId() {
		
		return this.id;
	}
	
	@Override
	public Recomendacao save() {

		Validacao.validar(this);
		
		return super.save();
	}

	public void update(Recomendacao novaRecomendacao) {

		this.texto = novaRecomendacao.texto;
		this.ordem = novaRecomendacao.ordem;
		
		this.save();
	}
	
	public Recomendacao gerarCopia() {
		
		Recomendacao copia = new Recomendacao();
		copia.texto = this.texto;
		copia.ordem = this.ordem;
		
		return copia;
	}
}