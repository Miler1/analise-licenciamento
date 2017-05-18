package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.licenciamento.Caracterizacao;
import models.licenciamento.Empreendimento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema="analise", name="processo")
public class Processo extends GenericModel {
	
	private static final String SEQ = "processo_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	public String numero;
	
	@Required
	@ManyToOne
	@JoinColumn(columnDefinition="id_empreendimento")
	public Empreendimento empreendimento;
	
	//TODO Adicionar objeto tramitável
	
	@ManyToMany
	@JoinTable(schema="analise", name="rel_processo_caracterizacao", 
		joinColumns= @JoinColumn(name="id_processo"),
		inverseJoinColumns = @JoinColumn(name="id_caracterizacao"))
	public List<Caracterizacao> caracterizacoes;

}
