package models.licenciamento;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "tipo_caracterizacao_atividade")
public class TipoCaracterizacaoAtividade extends GenericModel {

	private static final String SEQ = "licenciamento.tipo_caracterizacao_atividade_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_atividade")
	public Atividade atividade;
	
	@ManyToOne
	@JoinColumn(name="id_atividade_cnae")
	public AtividadeCnae atividadeCnae;

	@Column(name = "dispensa_licenciamento")
	public Boolean dispensaLicenciamento;
	
	@Column(name = "licenciamento_simplificado")
	public Boolean licenciamentoSimplificado;
	
	@Column(name = "licenciamento_declaratorio")
	public Boolean licenciamentoDeclatorio;

	/*
	 * Retorna todas as atividades baseadas no tipo de licencimento dispensa ou simplificado
	 */
	public static List<Atividade> findAtividades(FiltroAtividade filtro) {
		
		Session session = (Session) JPA.em().getDelegate();
		
		Criteria criteria = session.createCriteria(TipoCaracterizacaoAtividade.class);
		
		criteria.createAlias("atividade", "atividade");
		
		ProjectionList projectionList = Projections.projectionList();
		
		criteria.setProjection(projectionList)
			.setResultTransformer(Transformers.aliasToBean(Atividade.class));
		
		projectionList.add(Projections.distinct(Projections.property("atividade.id")), "id");
		
		projectionList.add(Projections.property("atividade.nome"), "nome");
		
		if(filtro.licenciamentoSimplificado != null) {
			
			criteria.add(Restrictions.eq("licenciamentoSimplificado", filtro.licenciamentoSimplificado));
		}
		
		if(filtro.dispensaLicenciamento != null) {
			
			criteria.add(Restrictions.eq("dispensaLicenciamento", filtro.dispensaLicenciamento));
		}
		
		List<Atividade> atividades = criteria.list();
		
		return atividades;
	}	
	
	/*
	 * Retorna todas as tipologias baseadas no tipo de licencimento dispensa ou simplificado
	 */
	public static List<Tipologia> findTipologias(FiltroAtividade filtro) {
		
		Session session = (Session) JPA.em().getDelegate();
		
		Criteria criteria = session.createCriteria(TipoCaracterizacaoAtividade.class);
		
		criteria.createAlias("atividade", "atividade");
		criteria.createAlias("atividade.tipologia", "tipologia");
		
		ProjectionList projectionList = Projections.projectionList();
		
		criteria.setProjection(projectionList)
			.setResultTransformer(Transformers.aliasToBean(Tipologia.class));
		
		projectionList.add(Projections.distinct(Projections.property("tipologia.id")), "id");
		
		projectionList.add(Projections.property("tipologia.nome"), "nome");
		
		if(filtro.licenciamentoSimplificado != null) {
			
			criteria.add(Restrictions.eq("licenciamentoSimplificado", filtro.licenciamentoSimplificado));
		}
		
		if(filtro.dispensaLicenciamento != null) {
			
			criteria.add(Restrictions.eq("dispensaLicenciamento", filtro.dispensaLicenciamento));
		}
		
		List<Tipologia> tipologias = criteria.list();
		
		return tipologias;		
	}
	
	public static TipoCaracterizacaoAtividade findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(List<AtividadeCaracterizacao> atividadesCaracterizacao) {
		
		AtividadeCaracterizacao atividadeCaracterizacao = AtividadeCaracterizacao.getAtividadeCaracterizacaoWithMaiorPotencialPoluidor(atividadesCaracterizacao);
		
		TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
				TipoCaracterizacaoAtividade.find("atividade.id = :idAtividade and atividadeCnae.id = :idAtividadeCnae")
					.setParameter("idAtividade",atividadeCaracterizacao.atividade.id)
					.setParameter("idAtividadeCnae", atividadeCaracterizacao.atividadesCnae.get(0).id)
					.first();
		
		return tipoAtividadeCaracterizacao;
		
	}
	
	public static class FiltroAtividade {
		
		public Boolean licenciamentoSimplificado;
		public Boolean dispensaLicenciamento;
		
		public FiltroAtividade() {
			
		}
	}
}
