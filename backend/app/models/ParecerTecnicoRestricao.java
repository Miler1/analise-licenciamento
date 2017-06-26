package models;

import java.util.List;

import javax.persistence.Column;
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
import play.db.jpa.JPABase;
import utils.Identificavel;
import utils.validacao.Validacao;

@Entity
@Table(schema="analise", name="parecer_tecnico_restricao")
public class ParecerTecnicoRestricao extends GenericModel implements Identificavel {
	
	public static final String SEQ = "analise.parecer_tecnico_restricao_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;
	
	@Required
	@Column(name="codigo_camada")
	public String codigoCamada;
	
	@Required
	public String parecer;

	@Override
	public Long getId() {
		
		return this.id;
	}
	
	@Override
	public ParecerTecnicoRestricao save() {
	
		Validacao.validar(this);		
		
		return super.save();
	}

	public void update(ParecerTecnicoRestricao novoParecerTecnicoRestricao) {
		
		this.codigoCamada = novoParecerTecnicoRestricao.codigoCamada;
		this.parecer = novoParecerTecnicoRestricao.parecer;
		
		this.save();
	}

	public List<ParecerTecnicoRestricao> getByIdAnaliseTecnica(Long idAnaliseTecnica) {
		
		return ParecerTecnicoRestricao.find("byAnaliseTecnica", idAnaliseTecnica).fetch();
		
	}
}
