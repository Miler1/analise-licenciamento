package models.tramitacao;

import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// View que possui os dados das condicao
@Entity
@Table(schema = "tramitacao", name = "VW_CONDICAO")
public class Condicao extends GenericModel {
	
	public static final Long AGUARDANDO_VINCULACAO_JURIDICA = 1l;
	public static final Long AGUARDANDO_ANALISE_JURIDICA = 2l;
	public static final Long EM_ANALISE_JURIDICA = 3l;
	public static final Long NOTIFICADO_PELO_ANALISTA_GEO = 4l;
	public static final Long AGUARDANDO_VALIDACAO_JURIDICA = 5l;
	public static final Long ARQUIVADO = 6l;
	public static final Long AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE = 7l;
	public static final Long AGUARDANDO_ANALISE_TECNICA = 8l;
	public static final Long EM_ANALISE_TECNICA = 9l;
	public static final Long AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR = 10l;
	public static final Long AGUARDANDO_ASSINATURA_PRESIDENE = 11l;
	public static final Long AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR = 12l;
	//public static final Long AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR = 13l;
	public static final Long LICENCA_EMITIDA = 14l;
	public static final Long SUSPENSO = 15l;
	public static final Long CANCELADO = 16l;

	public static final Long AGUARDANDO_VINCULACAO_GEO_PELO_COORDENADOR = 24l;
	public static final Long AGUARDANDO_ANALISE_GEO = 25l;
	public static final Long EM_ANALISE_GEO = 26l;
	public static final Long AGUARDANDO_VALIDACAO_GEO_PELO_COORDENADOR = 27l;
	public static final Long AGUARDANDO_VALIDACAO_COORDENADOR = 28l;
	public static final Long AGUARDANDO_VALIDACAO_DIRETORIA = 29l;
	public static final Long SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO = 30l;
	public static final Long EM_ANALISE_COORDENADOR = 31l;
	public static final Long AGUARDANDO_RESPOSTA_COMUNICADO = 32l;
	public static final Long SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA = 33l;
	public static final Long ANALISE_FINALIZADA = 34l;
	public static final Long NOTIFICADO_PELO_ANALISTA_TECNICO= 35l;
	public static final Long EM_ANALISE_TECNICA_COORDENADOR = 36l;
	public static final Long EM_ANALISE_DIRETOR = 37l;
	public static final Long EM_ANALISE_SECRETARIO = 38l;
	public static final Long SOLICITACAO_LICENCA_APROVADA = 40l;
	public static final Long SOLICITACAO_LICENCA_NEGADA = 41l;

	public static final Long AGUARDANDO_RESPOSTA_JURIDICA = 39L;

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

	public String getNome() {

		String nome = this.nomeCondicao.replace("Manejo digital ", "");
		return nome.substring(0, 1).toUpperCase() + nome.substring(1);
	}
}
