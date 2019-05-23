package models;

import exceptions.AppException;
import models.licenciamento.DispensaLicenciamento;
import models.licenciamento.LicenciamentoWebService;
import play.Logger;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.Date;

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
	public UsuarioAnalise usuario;
	
	@Column(name="data_cancelamento")
	public Date dataCancelada;
	
	public String justificativa;
	
	public DlaCancelada () {
		
	}
	
	public void cancelarDla(UsuarioAnalise usuarioExecutor) {

		this.usuario = new UsuarioAnalise();
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
