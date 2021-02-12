package models;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.WebService;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema="analise", name="analise")
public class Analise extends GenericModel {

	private final static String SEQ = "analise.analise_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@OneToOne
	@JoinColumn(name = "id_processo", referencedColumnName = "id")
	public Processo processo;
	
	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;
	
	@Required
	@Column(name="data_vencimento_prazo")
	public Date dataVencimentoPrazo;
	
	@OneToMany(mappedBy="analise")
	public List<AnaliseJuridica> analisesJuridica;
	
	@OneToMany(mappedBy="analise")
	public List<AnaliseTecnica> analisesTecnicas;

	@OneToMany(mappedBy="analise", fetch = FetchType.EAGER)
	public List<AnaliseGeo> analisesGeo;
	
	public Boolean ativo;
	
	@Transient
	public AnaliseJuridica analiseJuridica;
	
	@Transient 
	public AnaliseTecnica analiseTecnica;

	@Transient
	public AnaliseGeo analiseGeo;
	
	@OneToOne(mappedBy="analise")
	public DiasAnalise diasAnalise;
	
	@Column(name="notificacao_aberta")
	public Boolean temNotificacaoAberta;

	@OneToMany(mappedBy = "analise")
	@Fetch(FetchMode.SUBSELECT)
	public List<ParecerDiretorTecnico> pareceresDiretorTecnico;

	@OneToMany(mappedBy = "analise")
	@Fetch(FetchMode.SUBSELECT)
	public List<ParecerSecretario> pareceresSecretario;
	
	public Analise save() {
		
		Calendar c = Calendar.getInstance();
		c.setTime(this.dataCadastro);
		c.add(Calendar.DAY_OF_MONTH, Configuracoes.PRAZO_ANALISE);
		this.dataVencimentoPrazo = c.getTime();
		
		return super.save();
	}
	
	public AnaliseJuridica getAnaliseJuridica() {

		if(this.analiseJuridica != null)
			return this.analiseJuridica;

		if(this.analisesJuridica != null && !this.analisesJuridica.isEmpty())
			for(AnaliseJuridica analiseJuridica : this.analisesJuridica)
				if(analiseJuridica.ativo)
					this.analiseJuridica = analiseJuridica;
		
		if(this.analiseJuridica == null)
			this.analiseJuridica = AnaliseJuridica.findByProcessoAtivo(processo);

		return this.analiseJuridica;
		
	}
	
	public AnaliseTecnica getAnaliseTecnica() {
	
		if(this.analiseTecnica != null)
			return this.analiseTecnica;

		if(this.analiseTecnica != null && !this.analisesTecnicas.isEmpty())
			for(AnaliseTecnica analiseTecnica : this.analisesTecnicas)
				if(analiseTecnica.ativo)
					this.analiseTecnica = analiseTecnica;
		
		if(this.analiseTecnica == null)
			this.analiseTecnica = analiseTecnica.findByProcessoAtivo(processo);

		return this.analiseTecnica;		
	}


	public AnaliseGeo getAnaliseGeo() {

		if(this.analiseGeo != null)
			return this.analiseGeo;

		if(this.analiseGeo != null && !this.analisesGeo.isEmpty())
			for(AnaliseGeo analiseGeo : this.analisesGeo)
				if(analiseGeo.ativo)
					this.analiseGeo = analiseGeo;

		if(this.analiseGeo == null)
			this.analiseGeo = analiseGeo.findByProcessoAtivo(processo);

		return this.analiseGeo;
	}
	
	public static Analise findByProcessoAtivo(Processo processo) {
		return Analise.find("processo.id = :idProcesso AND ativo = true")
				.setParameter("idProcesso", processo.id)
				.first();
	}
	
	public static Analise findByAnaliseJuridica(Long idAnaliseJuridica) {
		
		AnaliseJuridica analiseJuridica = AnaliseJuridica.find("id = :idAnaliseJuridica AND ativo = true")
				.setParameter("idAnaliseJuridica", idAnaliseJuridica)
				.first();
		
		return analiseJuridica.analise;
	}

	public static Analise findByAnaliseGeo(Long idAnaliseGeo) {

		AnaliseGeo analiseGeo = AnaliseGeo.find("id = :idAnaliseGeo AND ativo = true")
				.setParameter("idAnaliseGeo", idAnaliseGeo)
				.first();

		return analiseGeo.analise;
	}
	
	public static List<Analise> findAtivas() {
		return Analise.find("byAtivo", true).fetch();
	}
	
	public static List<Analise> findComNotificacao() {
		return Analise.find("byTemNotificacaoAberta", true).fetch();
	}
	
	public AnaliseJuridica findPrimeiraAnaliseJuridicaComDataFim() {
		
		String jpqlMin = "SELECT MIN(aj.id) FROM " + AnaliseJuridica.class.getSimpleName() + " aj WHERE aj.analise.id = :idAnalise AND aj.dataFim IS NOT NULL";
		String jpql = "SELECT anaJ FROM " + AnaliseJuridica.class.getSimpleName() + " anaJ WHERE anaJ.id = (" + jpqlMin + ")";
		
		AnaliseJuridica analiseJuridica = AnaliseJuridica.find(jpql).setParameter("idAnalise", this.id).first();
		
		return analiseJuridica;
		
	}

	public AnaliseGeo findPrimeiraAnaliseGeoComDataFim() {

		String jpqlMin = "SELECT MIN(aj.id) FROM " + AnaliseGeo.class.getSimpleName() + " aj WHERE aj.analise.id = :idAnalise AND aj.dataFim IS NOT NULL";
		String jpql = "SELECT anaJ FROM " + AnaliseGeo.class.getSimpleName() + " anaJ WHERE anaJ.id = (" + jpqlMin + ")";

		AnaliseGeo analiseGeo = AnaliseGeo.find(jpql).setParameter("idAnalise", this.id).first();

		return analiseGeo;

	}
	
	public AnaliseTecnica findPrimeiraAnaliseTecnicaComDataFim() {
		
		String jpqlMin = "SELECT MIN(aj.id) FROM " + AnaliseTecnica.class.getSimpleName() + " aj WHERE aj.analise.id = :idAnalise AND aj.dataFim IS NOT NULL";
		String jpql = "SELECT anaJ FROM " + AnaliseTecnica.class.getSimpleName() + " anaJ WHERE anaJ.id = (" + jpqlMin + ")";
		
		AnaliseTecnica analiseTecnica = AnaliseTecnica.find(jpql).setParameter("idAnalise", this.id).first();
		
		return analiseTecnica;
		
	}
	
	public boolean hasNotificacaoNaoResolvida() {
		
		Long notificacoesNaoResolvidas;
		
		if(this.getAnaliseTecnica() != null) {

			Query query = em().createQuery("select count (*) from " + Notificacao.class.getName() + " analiseTecnica.id = :idAnaliseTecnica AND ativo = true AND resolvido = false")
					.setParameter("idAnaliseTecnica", this.getAnaliseTecnica().id);
			
			notificacoesNaoResolvidas = Long.parseLong(query.getSingleResult().toString());
			
		} else if (this.getAnaliseJuridica() != null){

			Query query = em().createQuery("select count (*) from " + Notificacao.class.getName() + " analiseJuridica.id = :idAnaliseJuridica AND ativo = true AND resolvido = false")
					.setParameter("idAnaliseJuridica", this.getAnaliseJuridica().id);

			notificacoesNaoResolvidas = Long.parseLong(query.getSingleResult().toString());
			
		}else {

			Query query = em().createQuery("select count (*) from " + Notificacao.class.getName() + " analiseGeo.id = :idAnaliseGeo AND ativo = true AND resolvido = false")
					.setParameter("idAnaliseGeo", this.getAnaliseGeo().id);

			notificacoesNaoResolvidas = Long.parseLong(query.getSingleResult().toString());
		}
		
		if(notificacoesNaoResolvidas > 0) {
			return true;
		}
		
		return false;

	}

	public static void alterarStatusLicenca(String codigoStatus, String numeroLicenca) {

		CaracterizacaoStatusVO caracterizacaoStatusVO = new CaracterizacaoStatusVO(codigoStatus, numeroLicenca);

		new WebService().postJSON(Configuracoes.URL_LICENCIAMENTO_UPDATE_STATUS, caracterizacaoStatusVO);

	}

	public Analise findUltimaInativaByProcesso(Processo processo){
		return find("processo.numero = :numero AND ativo = false ORDER BY id DESC")
				.setParameter("numero", processo.numero)
				.first();
	}

	public Analisavel getAnalisavel(){

		if(this.ativo) {

			AnaliseGeo analiseGeo = AnaliseGeo.findByProcessoAtivo(this.processo);
			if (analiseGeo != null) {
				return analiseGeo;
			}

			return AnaliseTecnica.findByProcessoAtivo(this.processo);
		}

		Analise analise = findUltimaInativaByProcesso(this.processo);

		if(analise != null && analise.analisesTecnicas != null && !analise.analisesTecnicas.isEmpty()) {
			return analise.analisesTecnicas.stream().max(Comparator.comparing(AnaliseTecnica::getId)).orElse(null);
		}

		if(analise != null && analise.analisesGeo != null){
			return analise.analisesGeo.stream().max(Comparator.comparing(AnaliseGeo::getId)).orElse(null);
		}

		return null;
	}

	public Analisavel getAnalisavelNotificacao(){

		if(this.analisesTecnicas != null && !this.analisesTecnicas.isEmpty()) {
			return this.analisesTecnicas.stream().max(Comparator.comparing(AnaliseTecnica::getId)).orElse(null);
		}

		if(this.analisesGeo != null){
			return this.analisesGeo.stream().max(Comparator.comparing(AnaliseGeo::getId)).orElse(null);
		}

		return null;
	}

	public void iniciar(UsuarioAnalise usuarioExecutor) {

		if (this.dataCadastro == null) {

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			this.dataCadastro = c.getTime();

			this._save();

		}

		this.processo.tramitacao.tramitar(this.processo, AcaoTramitacao.INICIAR_ANALISE_DIRETOR, usuarioExecutor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.processo.objetoTramitavel.id), usuarioExecutor);

	}

	public List<Notificacao> getNotificacoes(){
		return getAnalisavelNotificacao().getNotificacoes();
	}

	public void inativar(){
		this.getAnalisavel().inativar();
		this.processo.inativar();
		this.ativo = false;
		this._save();
	}

	public void iniciarAnaliseSecretario(UsuarioAnalise usuarioExecutor) {

		if (this.dataCadastro == null) {

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			this.dataCadastro = c.getTime();

			this._save();

		}

		this.processo.tramitacao.tramitar(this.processo, AcaoTramitacao.INICIAR_ANALISE_SECRETARIO, usuarioExecutor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.processo.idObjetoTramitavel), usuarioExecutor);

	}
	
}
