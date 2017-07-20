package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sun.corba.se.spi.ior.Identifiable;

import exceptions.ValidacaoException;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;
import utils.Configuracoes;
import utils.Identificavel;
import utils.Mensagem;
import utils.validacao.Validacao;

@Entity
@Table(schema="analise", name="condicionante")
public class Condicionante extends GenericModel implements Identificavel {
	
	public static final String SEQ = "analise.condicionante_id_seq";
	
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
	public Integer prazo;
	
	@Required
	public Integer ordem;

	@Override
	public Long getId() {
		
		return this.id;
	}
	
	@Override
	public Condicionante save() {
		
		Validacao.validar(this);
		
		if (this.prazo.compareTo(Configuracoes.PRAZO_MAXIMO_CONDICIONANTE) > 0) {
			
			throw new ValidacaoException(Mensagem.ANALISE_TECNICA_CONDICIONANTE_PRAZO_MAIOR_PERMITIDO);
		}
		
		return super.save();
	}

	public void update(Condicionante novaCondicionante) {
		
		this.texto = novaCondicionante.texto;
		this.prazo = novaCondicionante.prazo;
		this.ordem = novaCondicionante.ordem;
		
		this.save();
	}
	
	public Condicionante gerarCopia() {
		
		Condicionante copia = new Condicionante();
		copia.texto = this.texto;
		copia.prazo = this.prazo;
		copia.ordem = this.ordem;
		
		return copia;
	}
}
