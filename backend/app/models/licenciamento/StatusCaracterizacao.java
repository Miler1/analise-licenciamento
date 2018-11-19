package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "status_caracterizacao")
public class StatusCaracterizacao extends GenericModel {

	public static final Long FINALIZADO = 1l;
	public static final Long EM_ANDAMENTO = 2l;
	public static final Long AGUARDANDO_EMISSAO_DAE = 3l;
	public static final Long AGUARDANDO_QUITACAO_DAE = 4l;
	public static final Long EM_ANALISE = 5l;
	public static final Long ARQUIVADO = 6l;
	public static final Long SUSPENSO = 7l;
	public static final Long CANCELADO = 8l;
	public static final Long NOTIFICADO = 9l;
	
	@Id
	public Long id;

	public String nome;

	public String codigo;

}
