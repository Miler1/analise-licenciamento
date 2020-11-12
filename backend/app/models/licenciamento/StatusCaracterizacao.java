package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "status_caracterizacao")
public class StatusCaracterizacao extends GenericModel {

	public static final Long DEFERIDO = 1l;
	public static final Long EM_ANDAMENTO = 2l;
	public static final Long AGUARDANDO_EMISSAO_DAE = 3l;
	public static final Long AGUARDANDO_QUITACAO_DAE = 4l;
	public static final Long EM_ANALISE = 5l;
	public static final Long ARQUIVADO = 6l;
	public static final Long SUSPENSO = 7l;
	public static final Long CANCELADO = 8l;
	public static final Long NOTIFICADO = 9l;
	public static final Long EM_RENOVACAO_SEM_ALTERACAO = 10l;
	public static final Long EM_RENOVACAO_COM_ALTERACAO = 11l;
	public static final Long VENCIDA = 12L;
	public static final Long VENCIDO_AGUARDANDO_PAGAMENTO = 13L;
	public static final Long VENCIDO_AGUARDANDO_EMISSAO = 14L;
	public static final Long ANALISE_APROVADA = 15l;
	public static final Long INDEFERIDO = 16l;
	public static final Long SOLICITACAO_DE_DESVINCULO = 17l;
	public static final Long NOTIFICACAO_ATENDIDA = 24l;
	
	@Id
	public Long id;

	public String nome;

	public String codigo;

}
