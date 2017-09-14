package builders;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.sql.JoinType;

import play.db.jpa.JPA;

public abstract class CriteriaBuilder<T> {
	
	private Map<String, List> aliases;
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
	
	protected void addAlias(String table, String alias){
		
		List value = Arrays.asList(alias);
		
		aliases.put(table, value);
	}
	
	protected void addAlias(String table, String alias, JoinType joinType){
		
		List value = Arrays.asList(alias, joinType);
		
		aliases.put(table, value);
	}	
	
	public CriteriaBuilder(Criteria criteria) {
		
		this.criteria = criteria;
	}
	
	private void setProjection(){
		
		if (projectionList != null){
			
			for (Entry<String, List> alias : aliases.entrySet()) {
				
				List value = alias.getValue();
				
				if (value.size() == 2){
					
					criteria.createAlias(alias.getKey(), (String) value.get(0), (JoinType) value.get(1));
				} else {
					
					criteria.createAlias(alias.getKey(), (String) value.get(0));
				}
			}
			
			criteria.setProjection(projectionList)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		}
	}
	
	private void createProjection() {
		
		if (projectionList == null)
			projectionList = Projections.projectionList();
	}
	
	protected void addProjection(Projection projection) {
		
		createProjection();
		
		projectionList.add(projection);
	}	
	
	protected void addProjection(Property property) {
		
		createProjection();
		
		projectionList.add(property);
	}
	
	protected void addRestriction(Criterion expression) {
		
		criteria.add(expression);
	}
	
	protected void addOrder(Order order) {
		
		criteria.addOrder(order);
	}
	
	public CriteriaBuilder setMaxResult(int maxResult) {
		
		criteria.setMaxResults(maxResult);
		
		return this;
	}
	
	public CriteriaBuilder fetch(Integer page, Integer length) {
		
		if ( page > 0 && length > 0 ) {
			
			criteria.setFirstResult((page - 1) * length);
	        criteria.setMaxResults(length);
		}
		
		return this;
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
}
