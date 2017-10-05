package models;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;

import exceptions.AppException;
import exceptions.ValidacaoException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.licenciamento.StatusCaracterizacao;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.DateUtil;
import utils.ListUtil;
import utils.Mensagem;

@Entity
@Table(schema="analise", name="suspensao")
public class Suspensao extends GenericModel {
	
	public static final String SEQ = "analise.suspensao_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;	
	
	@OneToOne
	@JoinColumn(name="id_licenca")
	public Licenca licenca;
	
	@ManyToOne
	@JoinColumn(name="id_usuario_suspensao")
	public Usuario usuario;
	
	@Column(name="quantidade_dias_suspensao")
	public Integer qtdeDiasSuspensao;
	
	@Column(name="data_suspensao")
	public Date dataSuspensao;
	
	public String justificativa;
	
	
	@Column(name="ativo")
	public Boolean ativo;
	
	
	public Suspensao() {
		
	}
	
	public static List<Suspensao> findAtivas() {
		return Suspensao.find("byAtivo", true).fetch();
	}
	
	public void suspenderLicenca(Usuario usuarioExecutor) {
		
		Calendar c = Calendar.getInstance();
		Date dataAtual = c.getTime();
		
		Licenca licencaSuspensa = Licenca.findById(this.licenca.id);
		this.licenca = licencaSuspensa;
		
		Date validadeLicenca = this.licenca.dataValidade;
		
		Integer maxPrazo = DateUtil.getDiferencaEmDias(dataAtual, validadeLicenca);
		
		if(this.qtdeDiasSuspensao > maxPrazo)
			throw new ValidacaoException(Mensagem.PRAZO_MAXIMO_SUSPENSAO_EXCEDIDO, maxPrazo.toString());
		
		this.dataSuspensao = dataAtual;
		this.usuario = usuarioExecutor;
		
		try {
			
			this.save();
			
			if(deveSuspenderProcesso(this.licenca)) {
				Processo processo = this.licenca.licencaAnalise.analiseTecnica.analise.processo;
				processo.tramitacao.tramitar(processo, AcaoTramitacao.SUSPENDER_PROCESSO, usuarioExecutor);
			}
			
			Caracterizacao.setStatusCaracterizacao(ListUtil.createList(this.licenca.caracterizacao.id), StatusCaracterizacao.SUSPENSO);
			
			enviarNotificacaoSuspensaoPorEmail();
			
		} catch (Exception e) {
			
			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_ENVIAR_EMAIL, e.getMessage());
			
		}
		
	}
	
	private void enviarNotificacaoSuspensaoPorEmail() {
		
		List<String> destinatarios = new ArrayList<String>();
		destinatarios.addAll(this.licenca.caracterizacao.empreendimento.emailsProprietarios());
		destinatarios.addAll(this.licenca.caracterizacao.empreendimento.emailsResponsaveis());
		
		EmailNotificacaoSuspensaoLicenca emailNotificacao = new EmailNotificacaoSuspensaoLicenca(this, destinatarios);
		emailNotificacao.enviar();
		
	}
	
	private Boolean deveSuspenderProcesso(Licenca licenca) {
		
		if(licenca.licencaAnalise == null)
			return false;
		
		Processo processo = licenca.licencaAnalise.analiseTecnica.analise.processo;
		
		int numLicencasSuspensas = 0;
		
		for(Caracterizacao caracterizacao : processo.caracterizacoes) {
			if(caracterizacao.licenca.isSuspensa())
				numLicencasSuspensas++;
		}
		
		if(processo.caracterizacoes.size() == numLicencasSuspensas)
			return true;
		
		return false;
	}
}
