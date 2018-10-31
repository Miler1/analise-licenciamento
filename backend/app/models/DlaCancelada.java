package models;

import java.util.Date;

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
import models.licenciamento.DispensaLicenciamento;
import models.licenciamento.LicenciamentoWebService;
import models.portalSeguranca.UsuarioLicenciamento;
import play.Logger;
import play.db.jpa.GenericModel;
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
	public UsuarioLicenciamento usuario;
	
	@Column(name="data_cancelamento")
	public Date dataCancelada;
	
	public String justificativa;
	
	public DlaCancelada () {
		
	}
	
	public void cancelarDla(UsuarioLicenciamento usuarioExecutor) {

		this.usuario = new UsuarioLicenciamento();
		this.usuario.id = usuarioExecutor.id;

		LicenciamentoWebService webService = new LicenciamentoWebService();

		try {

			webService.cancelarDla(this);

		} catch (Exception e) {

			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_CANCELAR_DLA, e.getMessage());
		}
	}
}
