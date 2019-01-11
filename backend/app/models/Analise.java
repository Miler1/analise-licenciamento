package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Configuracoes;

@Entity
@Table(schema="analise", name="analise")
public class Analise extends GenericModel {

	private final static String SEQ = "analise.analise_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_processo")
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
	
	public Boolean ativo;
	
	@Transient
	public AnaliseJuridica analiseJuridica;
	
	@Transient 
	public AnaliseTecnica analiseTecnica;	
	
	@OneToOne(mappedBy="analise")
	public DiasAnalise diasAnalise;
	
	@Column(name="notificacao_aberta")
	public Boolean temNotificacaoAberta;
	
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
			this.analiseJuridica = AnaliseJuridica.findByProcesso(processo);

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
			this.analiseTecnica = analiseTecnica.findByProcesso(processo);

		return this.analiseTecnica;		
	}
	
	public static Analise findByProcesso(Processo processo) {
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
			
		} else {

			Query query = em().createQuery("select count (*) from " + Notificacao.class.getName() + " analiseJuridica.id = :idAnaliseJuridica AND ativo = true AND resolvido = false")
					.setParameter("idAnaliseJuridica", this.getAnaliseJuridica().id);

			notificacoesNaoResolvidas = Long.parseLong(query.getSingleResult().toString());
			
		}
		
		if(notificacoesNaoResolvidas > 0) {
			return true;
		}
		
		return false;

	}
}
