package models.licenciamento;

import models.*;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import security.cadastrounificado.CadastroUnificadoWS;
import utils.Identificavel;
import utils.ListUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "licenca")
public class Licenca extends GenericModel implements Identificavel {

	private static final String SEQ = "licenciamento.licenca_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
	public Caracterizacao caracterizacao;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@OneToOne
	@JoinColumn(name = "id_documento")
	public DocumentoLicenciamento documento;
	
	public String numero;
	
	@Column(name = "data_validade")
	public Date dataValidade;
	
	@ManyToOne
	@JoinColumn(name="id_licenca_analise")
	public LicencaAnalise licencaAnalise;
	
	@OneToOne
	@JoinColumn(name="id_licenca_anterior")
	public Licenca licencaAnterior;
	
	@Column(name = "data_validade_prorrogada")
	public Date dataValidadeProrrogada;
	
	@OneToOne(mappedBy="licenca")
	public Suspensao suspensao;
	
	public Boolean ativo;

	public Boolean prorrogacao;

	@OneToOne(mappedBy = "licenca")
	public LicencaCancelada licencaCancelada;

	public Licenca(Caracterizacao caracterizacao) {
		
		this.caracterizacao = caracterizacao;
		
	}
	
	public void gerar(LicencaAnalise licencaAnalise) {
		
		this.dataCadastro = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(this.dataCadastro);
		
		c.add(Calendar.YEAR, licencaAnalise.validade);
		this.dataValidade = c.getTime();
		this.licencaAnalise = licencaAnalise;
		this.ativo = true;
		this.save();
		
		this.gerarNumero();
		
		this.save();
		
	}
	
	private void gerarNumero() {
		
		if (this.id == null)
			throw new IllegalStateException("Licença não salva.");
		
		this.numero = Calendar.getInstance().get(Calendar.YEAR) + "/" +
				String.format("%06d", this.id);
	}

	@Override
	public Long getId() {
		
		return this.id;
	}
	
	public Boolean isSuspensa() {
		return this.caracterizacao.status.id.equals(StatusCaracterizacao.SUSPENSO);
	}
	
	public Boolean isCancelado() {
		return this.caracterizacao.status.id.equals(StatusCaracterizacao.CANCELADO);
	}

	public LicencaAnalise getLicencaAnalise() {
		return this.licencaAnalise;
	}

	public static void setAnteriorInativa(Long idCaracterizacao) {

		List<Licenca> licencasAntigas = Licenca.find("caracterizacao.id = :idCaracterizacao AND ativo = TRUE ORDER BY dataCadastro")
				.setParameter("idCaracterizacao", idCaracterizacao)
				.fetch();

		licencasAntigas.remove(licencasAntigas.size() - 1);

		List<Long> ids = ListUtil.getIds(licencasAntigas);

		JPA.em().createQuery("UPDATE Licenca SET ativo = FALSE WHERE id IN :idsCaracterizacoes")
				.setParameter("idsCaracterizacoes", ids)
				.executeUpdate();
	}

	public static void finalizarProrrogacao(Long idCaracterizacao) {

		List<Licenca> licencasAntigas = Licenca.find("caracterizacao.id = :idCaracterizacao AND ativo = TRUE ORDER BY dataCadastro")
				.setParameter("idCaracterizacao", idCaracterizacao)
				.fetch();

		licencasAntigas.remove(licencasAntigas.size() - 1);

		List<Long> ids = ListUtil.getIds(licencasAntigas);

		LicenciamentoWebService licenciamentoWS = new LicenciamentoWebService();
		licenciamentoWS.finalizarProrrogacao(ids);
	}

	public static void prorrogar(Long id) {

		LicenciamentoWebService licenciamentoWS = new LicenciamentoWebService();
		licenciamentoWS.prorrogarLicenca(id);

		Licenca licenca = Licenca.findById(id);

		licenca.enviarNotificacaoProrrogadaPorEmail();
	}

	public void enviarNotificacaoProrrogadaPorEmail() {

		List<String> destinatarios = new ArrayList<String>();

		destinatarios = CadastroUnificadoWS.ws.getEmailProprietarioResponsaveis(this.caracterizacao.empreendimento.empreendimentoEU.proprietarios,
																				this.caracterizacao.empreendimento.empreendimentoEU.responsaveisLegais,
																				this.caracterizacao.empreendimento.empreendimentoEU.responsaveisTecnicos, destinatarios);

		EmailNotificacaoProrrogacaoLicenca emailNotificacao = new EmailNotificacaoProrrogacaoLicenca(this, destinatarios);
		emailNotificacao.enviar();
	}
}
