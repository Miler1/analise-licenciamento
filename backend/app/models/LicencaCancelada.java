package models;

import exceptions.AppException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.licenciamento.StatusCaracterizacao;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.db.jpa.GenericModel;
import utils.ListUtil;
import utils.Mensagem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema="analise", name="licenca_cancelada")
public class LicencaCancelada extends GenericModel{
	
	public static final String SEQ = "analise.licenca_cancelada_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;	
	
	@OneToOne
	@JoinColumn(name="id_licenca")
	public Licenca licenca;
	
	@ManyToOne
	@JoinColumn(name="id_usuario_executor")
	public UsuarioAnalise usuario;
	
	@Column(name="data_cancelamento")
	public Date dataCancelada;
	
	public String justificativa;
		
	
	public LicencaCancelada() {
		
	}
	
	public void cancelarLicenca(UsuarioAnalise usuarioExecutor) {
		
		Calendar c = Calendar.getInstance();
		Date dataAtual = c.getTime();
		
		Licenca licenca = Licenca.findById(this.licenca.id);
		licenca.ativo = false;
		
		this.licenca = licenca;
		this.usuario = usuarioExecutor;
		this.dataCancelada = dataAtual; 
		
		try {
			
			licenca.save();
			this.save();
			
			Caracterizacao.setStatusCaracterizacao(ListUtil.createList(this.licenca.caracterizacao.id), StatusCaracterizacao.CANCELADO);
			
			if(deveCancelarProcesso(this.licenca)) {
				Processo processo = this.licenca.licencaAnalise.analiseTecnica.analise.processo;
				processo.tramitacao.tramitar(processo, AcaoTramitacao.CANCELAR_PROCESSO);
			}
			
			enviarNotificacaoCanceladoPorEmail();
			
		} catch (Exception e) {
			
			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_ENVIAR_EMAIL, e.getMessage());
			
		}
		
	}
	
	private void enviarNotificacaoCanceladoPorEmail() {
		
		List<String> destinatarios = new ArrayList<String>();
		destinatarios.addAll(this.licenca.caracterizacao.empreendimento.emailsProprietarios());
		destinatarios.addAll(this.licenca.caracterizacao.empreendimento.emailsResponsaveis());
		
		EmailNotificacaoCancelamentoLicenca emailNotificacao = new EmailNotificacaoCancelamentoLicenca(this, destinatarios);
		emailNotificacao.enviar();
		
	}
	
	private Boolean deveCancelarProcesso(Licenca licenca) {
		
		if(licenca.licencaAnalise == null)
			return false;
		
		Processo processo = licenca.licencaAnalise.analiseTecnica.analise.processo;
		
		int numLicencasCanceladas = 0;
		
		for(Caracterizacao caracterizacao : processo.caracterizacoes) {
			if(caracterizacao.isCancelado())
				numLicencasCanceladas++;
		}
		
		if(processo.getCaracterizacoesNaoArquivadas().size()-1 == numLicencasCanceladas)
			return true;
		
		return false;
	}

}
