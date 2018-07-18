package models.tramitacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.Notificacao;
import models.licenciamento.DocumentoLicenciamento;
import models.portalSeguranca.Setor;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

//View que possui informações sobre o histórico do objeto tramitavel

@Entity
@Table(schema="tramitacao", name = "VW_HISTORICO_OBJETO_TRAMITAVEL")
public class HistoricoTramitacao extends GenericModel {

	@Id
	@Column(name = "ID_HISTORICO_OBJETO_TRAMITAVEL")
	public Long idHistorico;

	@Column(name = "ID_OBJETO_TRAMITAVEL")
	public Long idObjetoTramitavel;

	@Column(name = "ID_ACAO")
	public Long idAcao;

	@Column(name = "NM_ACAO")
	public String nomeAcao;

	@Column(name = "ID_CONDICAO_INICIAL")
	public Long idCondicaoInicial;

	@Column(name = "NM_CONDICAO_INICIAL")
	public String nomeCondicaoInicial;

	@Column(name = "ID_CONDICAO_FINAL")
	public Long idCondicaoFinal;

	@Column(name = "NM_CONDICAO_FINAL")
	public String nomeCondicaoFinal;

	@Column(name = "ID_ETAPA_INICIAL")
	public Long idEtapaInicial;

	@Column(name = "NM_ETAPA_INICIAL")
	public String nomeEtapaInicial;

	@Column(name = "ID_ETAPA_FINAL")
	public Long idEtapaFinal;

	@Column(name = "NM_ETAPA_FINAL")
	public String nomeEtapaFinal;

	@Column(name = "TX_OBSERVACAO")
	public String observacao;

	@Column(name = "ID_USUARIO_EXECUTOR")
	public Long idUsuarioExecutor;

	@Column(name = "NM_USUARIO_EXECUTOR")
	public String nomeUsuarioExecutor;

	@Column(name = "ID_USUARIO_DESTINO")
	public Long idUsuarioDestino;

	@Column(name = "NM_USUARIO_DESTINO")
	public String nomeUsuarioDestino;

	@Column(name = "DT_CADASTRO")
	public Date dataInicial;

	@ManyToOne
	@JoinTable(schema = "portal_seguranca", name = "historico_tramitacao_setor",
			joinColumns = @JoinColumn(name = "id_setor"),
			inverseJoinColumns = @JoinColumn(name = "id_historico_tramitacao"))
	public Setor setor;

	@Transient
	public String tempoPermanencia;
	
	@Transient
	public Date dataFinal;


	/** 
	 * Consulta o histórico da tramitacao para um intervalo de tempo determinado e 
	 * com uma ação e uma condição final específica.
	 * @param processo
	 * @return
	 */
	public static List<HistoricoTramitacao> consultarHistoricoTramitacaoView
	(Date dataInicio, Date dataFim, Long acao, Long condicaoFinal){
		List<HistoricoTramitacao> historicoTramitacao = 
				HistoricoTramitacao.find(" dataInicial >=? AND dataInicial <=? AND idCondicaoFinal=? AND idAcao=? ",
						dataInicio,dataFim,condicaoFinal,acao).fetch();
		return historicoTramitacao;		
	}

	/** 
	 * Consulta o histórico da tramitacao para um intervalo com uma ação e uma condição final específica.
	 * @param processo
	 * @return
	 */
	public static List<HistoricoTramitacao> consultarHistoricoTramitacaoView(Long acao, Long condicaoFinal){
		List<HistoricoTramitacao> historicoTramitacao = 
				HistoricoTramitacao.find(" idCondicaoFinal=? AND idAcao=? ", condicaoFinal, acao).fetch();
		return historicoTramitacao;		
	}

	public static HistoricoTramitacao getUltimaTramitacao (Long idObjetoTramitavel){

		return HistoricoTramitacao.find("idObjetoTramitavel = :idObjetoTramitavel and idCondicaoInicial != idCondicaoFinal order by dataInicial desc").setParameter("idObjetoTramitavel", idObjetoTramitavel).first();

	}

	public static HistoricoTramitacao getPenultimaTramitacao (Long idObjetoTramitavel){

		List<HistoricoTramitacao> historicosTramitacao = HistoricoTramitacao.find("idObjetoTramitavel = :idObjetoTramitavel and idCondicaoInicial != idCondicaoFinal order by dataInicial desc").setParameter("idObjetoTramitavel", idObjetoTramitavel).fetch();
		return historicosTramitacao.get(1);
	}

	public static List<HistoricoTramitacao> getByObjetoTramitavel (Long idObjetoTramitavel){

		return HistoricoTramitacao.find("idObjetoTramitavel = :idObjetoTramitavel order by dataInicial desc, idHistorico desc")
									  .setParameter("idObjetoTramitavel", idObjetoTramitavel).fetch();
	}

	private static String consultarHistoricoUltimaCondicaoAcao(List listCondicao, List listAcao, boolean inout){
		StringBuilder retorno = new StringBuilder("");
		StringBuilder sql = new StringBuilder("");

		sql.append(" select hot.id_objeto_tramitavel ")
		.append(" from tramitacao.vw_historico_objeto_tramitavel hot ")
		.append(" where (1=1) ")
		.append(" AND  hot.id_historico_objeto_tramitavel in( ")
		.append(" select  max(reg.id_historico_objeto_tramitavel) as maximum ")
		.append(" from (	select hot.id_historico_objeto_tramitavel,hot.id_objeto_tramitavel ")
		.append(" from tramitacao.vw_historico_objeto_tramitavel hot ")
		.append(" )reg ")
		.append(" group by reg.id_objeto_tramitavel ) ");
		if(inout){
			sql.append(" AND  hot.id_condicao_final in( ?1 ) ")
			.append(" AND  hot.id_acao in( ?2 ) ");
		}else{
			sql.append(" AND  hot.id_condicao_final not in( ?1 ) ")
			.append(" AND  hot.id_acao not in( ?2 ) ");	
		}

		Query query = JPA.em().createNativeQuery (sql.toString())
				.setParameter (1, listCondicao )
				.setParameter (2, listAcao );

		List<Long> lResult =  query.getResultList();

		for(int i = 0; i< lResult.size(); i++)	
			retorno	.append("'"+lResult.get(i)+"'")
			.append((i+1<lResult.size())?",":"");

		if(lResult.size() == 0){
			retorno	.append("'-1'");
		}

		return retorno.toString();
	}

	public static Date getPrimeiraDataHistoricoTramitacao() {
		
		String jpql = "SELECT ht.dataInicial "
				+ "FROM " + HistoricoTramitacao.class.getSimpleName() + " ht "
						+ "ORDER BY ht.dataInicial ASC";
		
		Date primeiraData = (Date) HistoricoTramitacao.find(jpql).first();
		
		return primeiraData;
		
	}

	public boolean getHasNotificacoes() {

		List<Notificacao> notificacoes = Notificacao.find("historicoTramitacao.id", this.idHistorico).fetch();

		return notificacoes != null && notificacoes.size() > 0;
	}

	public Setor getSetor() {

		return Setor.find("select s from Setor s join s.historicosTramitacao ht where ht.id = :x")
				.setParameter("x", this.idHistorico).first();
	}

	public List<DocumentoLicenciamento> getDocumentosLicenciamento() {

		ArrayList<DocumentoLicenciamento> documentos = new ArrayList<>();

		List<Notificacao> notificacoes = Notificacao.find("historicoTramitacao.id", this.idHistorico).fetch();

		if (notificacoes != null && notificacoes.size() > 0) {

			for(Notificacao notificacao : notificacoes) {

				documentos.add(notificacao.analiseDocumento.documento);
			}
		}

		return documentos;
	}

}
