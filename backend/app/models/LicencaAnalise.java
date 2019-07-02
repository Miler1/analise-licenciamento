package models;

import exceptions.AppException;
import exceptions.ValidacaoException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.licenciamento.LicenciamentoWebService;
import models.licenciamento.StatusCaracterizacao;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.Identificavel;
import utils.ListUtil;
import utils.Mensagem;
import utils.validacao.Validacao;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
	@ManyToOne
	@JoinColumn(name="id_analise_geo")
	public AnaliseGeo analiseGeo;
	
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
	
	@OneToMany(mappedBy="licencaAnalise")
	public List<Licenca> licencas;
	
	@Transient
	public Licenca licenca;
	
	private static void commit() {
		
		if(JPA.isInsideTransaction()) {
			
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
		}
	}
	
	private static void rollbackTransaction() {
		
		if (JPA.isInsideTransaction()) {
			
			JPA.em().getTransaction().rollback();
			JPA.em().getTransaction().begin();
		}
	}	
	
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
	
	public LicencaAnalise gerarCopia(Boolean copiarId) {
		
		LicencaAnalise copia = new LicencaAnalise();
		copia.validade = this.validade;
		copia.analiseTecnica = this.analiseTecnica;
		copia.caracterizacao = this.caracterizacao;
		copia.observacao = this.observacao;
		copia.emitir = this.emitir;
		
		if(copiarId)
			copia.id = this.id;
				
		copia.condicionantes = new ArrayList<Condicionante>();
		for(Condicionante condicionante : this.condicionantes) {
			
			Condicionante copiaCondicionante = condicionante.gerarCopia();
			copiaCondicionante.licencaAnalise = copia;
			copia.condicionantes.add(copiaCondicionante);						
		}
		
		copia.recomendacoes = new ArrayList<Recomendacao>();
		for(Recomendacao recomendacao : this.recomendacoes) {
			
			Recomendacao copiaRecomendacao = recomendacao.gerarCopia();
			copiaRecomendacao.licencaAnalise = copia;
			copia.recomendacoes.add(copiaRecomendacao);						
		}			
		
		return copia;
	}
	
	public LicencaAnalise gerarCopia() {
		return gerarCopia(false);
	}
	
	public void saveCondicionantes() {
			
		if(this.condicionantes == null) {
			return;
		}
		
		for(Condicionante condicionante : this.condicionantes) {
			
			condicionante.licencaAnalise = this;
			condicionante.save();			
		}					
	}
		
	public void saveRecomendacoes() {
		
		if(this.recomendacoes == null) {
			return;
		}
		
		for(Recomendacao recomendacao : this.recomendacoes) {
			
			recomendacao.licencaAnalise = this;
			recomendacao.save();			
		}					
	}
	
	public static void emitirLicencas(LicencaAnalise[] licencasAnalise, UsuarioAnalise usuarioExecutor) {
		
		List<LicencaAnalise> licencaAnalisesCopia = new ArrayList<>();
		List<Long> idsLicencas = new ArrayList<>();
		List<Long> idsCaracterizacoesDeferidas = new ArrayList<>();
		List<Long> idsCaracterizacoesArquivadas = new ArrayList<>();
		
		for(LicencaAnalise licencaAnaliseUpdate : licencasAnalise) {
			
			LicencaAnalise licencaAAlterar = LicencaAnalise.findById(licencaAnaliseUpdate.id);
			
			if (!licencaAAlterar.emitir) {
				idsCaracterizacoesArquivadas.add(licencaAAlterar.caracterizacao.id);
				continue;
			}
			
			licencaAnalisesCopia.add(licencaAAlterar.gerarCopia(true));
			
			licencaAAlterar.update(licencaAnaliseUpdate);
			Licenca licencaGerada = licencaAAlterar.emitirLicenca();
			idsLicencas.add(licencaGerada.id);
			
			idsCaracterizacoesDeferidas.add(licencaAAlterar.caracterizacao.id);
		}
		
		commit();
		
		try {
			
			LicenciamentoWebService webService = new LicenciamentoWebService();
			webService.gerarPDFLicencas(idsLicencas);
			
			if(!idsCaracterizacoesDeferidas.isEmpty()) {
				Caracterizacao.setStatusCaracterizacao(idsCaracterizacoesDeferidas, StatusCaracterizacao.FINALIZADO);
				Caracterizacao.setCaracterizacaoEmAnalise(idsCaracterizacoesDeferidas, false);
				Caracterizacao.setCaracterizacaoEmRenovacao(idsCaracterizacoesDeferidas, false);
			}

			if(!idsCaracterizacoesArquivadas.isEmpty())
				Caracterizacao.setStatusCaracterizacao(idsCaracterizacoesArquivadas, StatusCaracterizacao.ARQUIVADO);
			
			LicencaAnalise lAnalise = LicencaAnalise.findById(licencasAnalise[0].id);
			Processo processo = lAnalise.analiseTecnica.analise.processo;

			if (processo.processoAnterior != null) {

				if (processo.getCaracterizacao().getLicencaAnterior().prorrogacao) {

					if (processo.processoAnterior.tramitacao.isAcaoDisponivel(AcaoTramitacao.ARQUIVAR_PRORROGACAO_POR_RENOVACAO, processo.processoAnterior)) {

						processo.processoAnterior.tramitacao.tramitar(processo.processoAnterior, AcaoTramitacao.ARQUIVAR_PRORROGACAO_POR_RENOVACAO);
					}

					Licenca.finalizarProrrogacao(processo.getCaracterizacao().id);
				}

				Licenca.setAnteriorInativa(processo.getCaracterizacao().id);
			}
			
			lAnalise.analiseTecnica.dataFimValidacaoAprovador = new Date();
			lAnalise.analiseTecnica._save();

			processo.tramitacao.tramitar(processo, AcaoTramitacao.EMITIR_LICENCA, usuarioExecutor);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(processo.objetoTramitavel.id), usuarioExecutor);

		} catch (Exception e) {
			
			rollbackTransaction();

			for(LicencaAnalise licencaAnalise : licencaAnalisesCopia) {
				
				LicencaAnalise licencaAAlterar = LicencaAnalise.findById(licencaAnalise.id);
				licencaAAlterar.update(licencaAnalise);
				
			}
			
			for(Long idLicenca : idsLicencas) {
				Licenca licenca = Licenca.findById(idLicenca);
				licenca.delete();
			}
			
			commit();
			
			throw new AppException(Mensagem.ERRO_EMITIR_LICENCAS);
		}
	}
	
	private Licenca emitirLicenca() {
		
		Licenca licenca = new Licenca(this.caracterizacao);
		licenca.gerar(this);
		
		return licenca;
	}
	
	public Licenca getLicenca() {
		
		if(this.licenca != null)
			return this.licenca;

		if(this.licenca == null && !this.licencas.isEmpty())
			for(Licenca licenca : this.licencas)
				if(licenca.ativo)
					this.licenca = licenca;

		return this.licenca;
	}
}
