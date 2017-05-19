package builders;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import play.db.jpa.JPA;

public abstract class CriteriaBuilder<T> {
	
	private Map<String, String> aliases;
	protected Criteria criteria;
	protected ProjectionList projectionList;
	private Class<T> clazz;

	public CriteriaBuilder(){
		
		this.clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
		
		Session session = (Session) JPA.em().getDelegate();
		
		criteria = session.createCriteria(clazz);
		
		aliases = new HashMap<>();
		
	}
	
	public void addAlias(String table, String alias){
		
		aliases.put(table, alias);
	}
	
	public CriteriaBuilder(Criteria criteria) {
		
		this.criteria = criteria;
	}
	
	private void setProjection(){
		
		if (projectionList != null){
			
			for (Entry<String, String> alias : aliases.entrySet()) {
				
				criteria.createAlias(alias.getKey(), alias.getValue());
			}
			
			criteria.setProjection(projectionList)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//				.setResultTransformer(Transformers.aliasToBean(clazz))
			;
		}
	}
	
	protected void addProjection(Projection projection) {
		
		if (projectionList == null)
			projectionList = Projections.projectionList();
		
		projectionList.add(projection);
	}	
	
	protected void addProjection(Property property) {
		
		if (projectionList == null)
			projectionList = Projections.projectionList();
		
		projectionList.add(property);
	}
	
	public Object unique(){
		
		setProjection();
		
		return criteria.uniqueResult();
	}
	
	public List listObject(){
		
		setProjection();
		
		return criteria.list();
	}
	
	public List<T> list(){
		
		setProjection();
		
		return criteria.list();
	}	
	
	public abstract Long count();
}
