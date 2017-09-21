package models.tramitacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

// View que possui os dasdos das condicao
@Entity
@Table(schema = "tramitacao", name = "VW_CONDICAO")
public class Condicao extends GenericModel {
	
	public static final Long AGUARDANDO_VINCULACAO_JURIDICA = 1l;
	public static final Long AGUARDANDO_ANALISE_JURIDICA = 2l;
	public static final Long EM_ANALISE_JURIDICA = 3l;
	public static final Long NOTIFICADO = 4l;
	public static final Long AGUARDANDO_VALIDACAO_JURIDICA = 5l;
	public static final Long ARQUIVADO = 6l;
	public static final Long AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE = 7l;
	public static final Long AGUARDANDO_ANALISE_TECNICA = 8l;
	public static final Long EM_ANALISE_TECNICA = 9l;
	public static final Long AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE = 10l;
	public static final Long AGUARDANDO_ASSINATURA_APROVADOR = 11l;
	public static final Long AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR = 12l;
	public static final Long AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR = 13l;
	public static final Long LICENCA_EMITIDA = 14l;
	
	@Id
	@Column(name = "ID_CONDICAO")
	public Long idCondicao;

	@Column(name = "ID_FLUXO")
	public Long idFluxo;

	@Column(name = "ID_ETAPA")
	public Long idEtapa;

	@Column(name = "TX_ETAPA")
	public String nomeEtapa;

	@Column(name = "NM_CONDICAO")
	public String nomeCondicao;


}
