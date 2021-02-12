package models;

import org.apache.commons.lang.StringUtils;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "reenvio_email", schema = "analise")
public class ReenvioEmail extends GenericModel {

	public enum TipoEmail {
		COMUNICAR_ORGAO,
		COMUNICAR_JURIDICO,
		NOTIFICACAO_ANALISE_JURIDICA,
		NOTIFICACAO_ANALISE_TECNICA,
		NOTIFICACAO_ANALISE_GEO,
		NOTIFICACAO_COMUNICADO,
		NOTIFICACAO_SECRETARIO,
		NOTIFICACAO_SECRETARIO_DISPENSA,
		CANCELAMENTO_DLA,
		CANCELAMENTO_LICENCA,
		SUSPENSAO_LICENCA,
		ARQUIVAMENTO_PROCESSO,
		PRORROGACAO_LICENCA
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
