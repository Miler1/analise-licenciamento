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

import exceptions.AppException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.DispensaLicenciamento;
import models.licenciamento.Licenca;
import models.licenciamento.StatusCaracterizacao;
import models.portalSeguranca.Usuario;
import play.Logger;
import play.db.jpa.GenericModel;
import utils.ListUtil;
import utils.Mensagem;

@Entity
@Table(schema="analise", name="dispensa_licencamento_cancelada")
public class DlaCancelada extends GenericModel {
	
	public static final String SEQ = "analise.dispensa_licencamento_cancelada_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;	
	
	@OneToOne
	@JoinColumn(name="id_dispensa_licencamento")
	public DispensaLicenciamento dispensaLicenciamento;
	
	@ManyToOne
	@JoinColumn(name="id_usuario_executor")
	public Usuario usuario;
	
	@Column(name="data_cancelamento")
	public Date dataCancelada;
	
	public String justificativa;
	
	public DlaCancelada () {
		
	}
	
	public void cancelarDla(Usuario usuarioExecutor) {
		
		Calendar c = Calendar.getInstance();
		Date dataAtual = c.getTime();
		
		DispensaLicenciamento dispensaLicenciamento = DispensaLicenciamento.findById(this.dispensaLicenciamento.id);
		dispensaLicenciamento.ativo = false;
		
		this.dispensaLicenciamento = dispensaLicenciamento;
		this.usuario = usuarioExecutor;
		this.dataCancelada = dataAtual;
		
		try {
			
			dispensaLicenciamento.save();
			this.save();
			
			Caracterizacao.setStatusCaracterizacao(ListUtil.createList(this.dispensaLicenciamento.caracterizacao.id), StatusCaracterizacao.CANCELADO);
			
			enviarNotificacaoCanceladoPorEmail();
			
		} catch(Exception e) {
			
			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_ENVIAR_EMAIL, e.getMessage());
		}
	}
	
	private void enviarNotificacaoCanceladoPorEmail() {
		
		List<String> destinatarios = new ArrayList<String>();
		destinatarios.addAll(this.dispensaLicenciamento.caracterizacao.empreendimento.emailsProprietarios());
		destinatarios.addAll(this.dispensaLicenciamento.caracterizacao.empreendimento.emailsResponsaveis());
		
		EmailNotificacaoCancelamentoDla emailNotificacao = new EmailNotificacaoCancelamentoDla(this, destinatarios);
		emailNotificacao.enviar();
	}
	
}
