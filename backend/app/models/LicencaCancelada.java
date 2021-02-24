package models;

import exceptions.AppException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.licenciamento.StatusCaracterizacao;
import models.licenciamento.StatusCaracterizacaoEnum;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.db.jpa.GenericModel;
import security.cadastrounificado.CadastroUnificadoWS;
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

			Processo processo = this.licenca.caracterizacao.processo;
			processo.tramitacao.tramitar(processo, AcaoTramitacao.CANCELAR_PROTOCOLO);

			this.licenca.caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacaoEnum.CANCELADO.id);

			ParecerSecretario parecerSecretario = ParecerSecretario.getUltimoParecerSecretario(this.licenca.caracterizacao.processo.analise.pareceresSecretario);
			parecerSecretario.enviarEmailStatusAnalise(this.licenca.caracterizacao.processo.analise);
			
//			enviarNotificacaoCanceladoPorEmail();
			
		} catch (Exception e) {
			
			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_ENVIAR_EMAIL, e.getMessage());
			
		}
		
	}
	
	private void enviarNotificacaoCanceladoPorEmail() {
		
		List<String> destinatarios = new ArrayList<String>();

		destinatarios = CadastroUnificadoWS.ws.getEmailProprietarioResponsaveis(this.licenca.caracterizacao.empreendimento.empreendimentoEU.proprietarios,
																				this.licenca.caracterizacao.empreendimento.empreendimentoEU.responsaveisLegais,
																				this.licenca.caracterizacao.empreendimento.empreendimentoEU.responsaveisTecnicos, destinatarios);

		EmailNotificacaoCancelamentoLicenca emailNotificacao = new EmailNotificacaoCancelamentoLicenca(this, destinatarios);
		emailNotificacao.enviar();
		
	}
	
	private Boolean deveCancelarProcesso(Licenca licenca) {
		
		if(licenca.licencaAnalise == null)
			return false;
		
		Processo processo = licenca.licencaAnalise.analiseTecnica.analise.processo;
		
		int numLicencasCanceladas = 0;
		
		for(Caracterizacao caracterizacao : processo.empreendimento.caracterizacoes) {
			if(caracterizacao.isCancelado())
				numLicencasCanceladas++;
		}
		
		if(processo.getCaracterizacoesNaoArquivadas().size()-1 == numLicencasCanceladas)
			return true;
		
		return false;
	}

}
