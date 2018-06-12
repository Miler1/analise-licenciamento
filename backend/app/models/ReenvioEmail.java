package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "reenvio_email", schema = "analise")
public class ReenvioEmail extends GenericModel {

	public enum TipoEmail {
		NOTIFICACAO_ANALISE_JURIDICA,
		NOTIFICACAO_ANALISE_TECNICA,
		CANCELAMENTO_DLA,
		CANCELAMENTO_LICENCA,
		SUSPENSAO_LICENCA,
		ARQUIVAMENTO_PROCESSO
	}
	
	public static final String SEQ = "analise.reenvio_email_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
    
	@Column(name = "id_itens_email")
	public Long idItensEmail;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "tipo_email")
	public TipoEmail tipoEmail;
	
	public String log;
	
	@Column(name = "emails_destinatario")
	public String emailsDestinatario;
	
	public ReenvioEmail(Long idItensEmail, TipoEmail tipoEmail, String log, List<String> emailsDestinatario) {
		
		super();
		this.idItensEmail = idItensEmail;
		this.tipoEmail = tipoEmail;
		this.log = log;
		
		this.emailsDestinatario = StringUtils.join(emailsDestinatario, ";");
		
	}
	
}
