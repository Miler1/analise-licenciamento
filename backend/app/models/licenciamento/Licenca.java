package models.licenciamento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import exceptions.AppException;
import models.EmailNotificacaoCancelamentoLicenca;
import models.EmailNotificacaoSuspensaoLicenca;
import models.LicencaAnalise;
import models.Processo;
import models.Suspensao;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.db.jpa.GenericModel;
import utils.Identificavel;
import utils.ListUtil;
import utils.Mensagem;

@Entity
@Table(schema = "licenciamento", name = "licenca")
public class Licenca extends GenericModel implements Identificavel {

	private static final String SEQ = "licenciamento.licenca_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@OneToOne
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
	
	@OneToOne
	@JoinColumn(name="id_licenca_analise")
	public LicencaAnalise licencaAnalise;
	
	public Boolean ativo;

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
		return Suspensao.find("byLicenca", this).first() != null ? true : false;
	}
	
	public Boolean isCancelado() {
		return Licenca.find("byAtivo", this).first() != null ? true : false;
	}
	
	public void cancelarLicenca() {
		
		this.ativo = false;
		
		try {
			
			this.save();
			
			if(deveCancelarProcesso(this)) {
				Processo processo = this.licencaAnalise.analiseTecnica.analise.processo;
				processo.tramitacao.tramitar(processo, AcaoTramitacao.CANCELAR_PROCESSO);
			}
			
			Caracterizacao.setStatusCaracterizacao(ListUtil.createList(this.caracterizacao.id), StatusCaracterizacao.SUSPENSO);
			
			enviarNotificacaoCanceladoPorEmail();
			
		} catch (Exception e) {
			
			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_ENVIAR_EMAIL, e.getMessage());
			
		}
		
	}
	
	private void enviarNotificacaoCanceladoPorEmail() {
		
		List<String> destinatarios = new ArrayList<String>();
		destinatarios.addAll(this.caracterizacao.empreendimento.emailsProprietarios());
		destinatarios.addAll(this.caracterizacao.empreendimento.emailsResponsaveis());
		
		EmailNotificacaoCancelamentoLicenca emailNotificacao = new EmailNotificacaoCancelamentoLicenca(this, destinatarios);
		emailNotificacao.enviar();
		
	}
	
	private Boolean deveCancelarProcesso(Licenca licenca) {
		
		if(licenca.licencaAnalise == null)
			return false;
		
		Processo processo = licenca.licencaAnalise.analiseTecnica.analise.processo;
		
		int numLicencasCanceladas = 0;
		
		for(Caracterizacao caracterizacao : processo.caracterizacoes) {
			if(caracterizacao.licenca.isSuspensa())
				numLicencasCanceladas++;
		}
		
		if(processo.caracterizacoes.size() == numLicencasCanceladas)
			return true;
		
		return false;
	}
}
