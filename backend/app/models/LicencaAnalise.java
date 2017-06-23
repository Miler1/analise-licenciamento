package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import exceptions.PermissaoNegadaException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Identificavel;
import utils.ListUtil;
import utils.Mensagem;

@Entity
@Table(schema="analise", name="licenca_analise")
public class LicencaAnalise extends GenericModel implements Identificavel {
	
	public static final String SEQ = "analise.licenca_analise_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;
	
	@Required
	public Integer validade;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_caracterizacao")
	public Caracterizacao caracterizacao;
	
	@Required
	public String observacao;
	
	@OneToMany(mappedBy="licencaAnalise", cascade=CascadeType.ALL)
	public List<Condicionante> condicionantes;

	@OneToMany(mappedBy="licencaAnalise", cascade=CascadeType.ALL)
	public List<Recomendacao> recomendacoes;	
	
	public void update(LicencaAnalise novaLicencaAnalise) {
		
		this.validade = novaLicencaAnalise.validade;
		this.observacao = novaLicencaAnalise.observacao;
		
		updateCondicionantes(novaLicencaAnalise.condicionantes);
		updateRecomendacoes(novaLicencaAnalise.recomendacoes);
	}

	private void updateCondicionantes(List<Condicionante> novasCondicionantes) {
		
		if (this.condicionantes == null) {
			
			this.condicionantes = new ArrayList<>();
		}
		
		Iterator<Condicionante> condicionantesCadastradas = this.condicionantes.iterator();
		
		while(condicionantesCadastradas.hasNext()) {
			
			Condicionante condicionanteCadastrada = condicionantesCadastradas.next();
			
			if (ListUtil.getById(condicionanteCadastrada.id, novasCondicionantes) == null) {
				
				condicionanteCadastrada.delete();
			}
		}		
				
		for(Condicionante novaCondicionante : novasCondicionantes) {
						
			Condicionante condicionante = ListUtil.getById(novaCondicionante.id, this.condicionantes);
				
			if(condicionante != null) {
				
				condicionante.update(novaCondicionante);
			
			} else {
				
				novaCondicionante.licencaAnalise = this;
				this.condicionantes.add(novaCondicionante);
			}			
		}
	}	

	private void updateRecomendacoes(List<Recomendacao> novasRecomendacoes) {
		
		if (this.recomendacoes == null) {
			
			this.recomendacoes = new ArrayList<>();
		}
		
		Iterator<Recomendacao> recomendacoesCadastradas = this.recomendacoes.iterator();
		
		while(recomendacoesCadastradas.hasNext()) {
			
			Recomendacao recomendacaoCadastrada = recomendacoesCadastradas.next();
			
			if (ListUtil.getById(recomendacaoCadastrada.id, novasRecomendacoes) == null) {
				
				recomendacaoCadastrada.delete();
			}
		}		
				
		for(Recomendacao novaRecomendacao : novasRecomendacoes) {
						
			Recomendacao recomendacao = ListUtil.getById(novaRecomendacao.id, this.recomendacoes);
				
			if(recomendacao != null) {
				
				recomendacao.update(novaRecomendacao);
			
			} else {
				
				novaRecomendacao.licencaAnalise = this;
				this.recomendacoes.add(novaRecomendacao);
			}			
		}		
	}
	
	@Override
	public Long getId() {
		
		return this.getId();
	}
}
