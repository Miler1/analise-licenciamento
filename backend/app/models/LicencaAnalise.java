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
import exceptions.ValidacaoException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;
import utils.Identificavel;
import utils.ListUtil;
import utils.Mensagem;
import utils.validacao.Validacao;

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
	
	public String observacao;
	
	@OneToMany(mappedBy="licencaAnalise", orphanRemoval=true)
	public List<Condicionante> condicionantes;

	@OneToMany(mappedBy="licencaAnalise", orphanRemoval=true)
	public List<Recomendacao> recomendacoes;	
	
	public Boolean emitir;
	
	@Override
	public LicencaAnalise save() {
		
		Validacao.validar(this);
		
		if (this.validade.compareTo(caracterizacao.tipoLicenca.validadeEmAnos) > 0) {
			
			throw new ValidacaoException(Mensagem.ANALISE_TECNICA_VALIDADE_LICENCA_MAIOR_PERMITIDO);
		}
		
		if (!emitirIsTrue()) {
			
			this.validade = caracterizacao.tipoLicenca.validadeEmAnos;
			this.observacao = null;
		}
		 
		return super.save();		
	}
	
	public void update(LicencaAnalise novaLicencaAnalise) {
		
		this.validade = novaLicencaAnalise.validade;
		this.observacao = novaLicencaAnalise.observacao;
		this.emitir = novaLicencaAnalise.emitir;
		
		this.save();
		
		if (!emitirIsTrue()){
			
			novaLicencaAnalise.condicionantes = new ArrayList<>();
			novaLicencaAnalise.recomendacoes = new ArrayList<>();			
		}
		
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
				condicionantesCadastradas.remove();
			}
		}		
				
		for(Condicionante novaCondicionante : novasCondicionantes) {
						
			Condicionante condicionante = ListUtil.getById(novaCondicionante.id, this.condicionantes);
				
			if(condicionante != null) {
				
				condicionante.update(novaCondicionante);
			
			} else {
				
				novaCondicionante.licencaAnalise = this;
				novaCondicionante.save();
				
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
				recomendacoesCadastradas.remove();
			}
		}		
				
		for(Recomendacao novaRecomendacao : novasRecomendacoes) {
						
			Recomendacao recomendacao = ListUtil.getById(novaRecomendacao.id, this.recomendacoes);
				
			if(recomendacao != null) {
				
				recomendacao.update(novaRecomendacao);
			
			} else {
				
				novaRecomendacao.licencaAnalise = this;
				novaRecomendacao.save();
				
				this.recomendacoes.add(novaRecomendacao);
			}			
		}		
	}
	
	private boolean emitirIsTrue() {
		
		return Boolean.TRUE.equals(this.emitir);
	}
	
	public Integer getValidadeMaxima() {
		return caracterizacao.tipoLicenca.validadeEmAnos;
	}

	@Override
	public Long getId() {
		
		return this.id;
	}
}
