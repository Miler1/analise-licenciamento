package utils;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;

import java.text.Normalizer;

public class IlikeNoAccents implements Criterion {
	private static final long serialVersionUID = 1L;

	private final String propertyName;
	private final Object value;

	public IlikeNoAccents(String propertyName, Object value) {
		this.propertyName = propertyName;
		this.value = value;
	}

	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		String[] columns = criteriaQuery.getColumns(propertyName, criteria);

		if(columns.length != 1) {
			throw new HibernateException("ilike may only be used with single-column properties");
		}

		return "TRANSLATE(UPPER("+ columns[0] +"),'ÂÁÀÄÃÊÉÈËÎÍÌÏÔÓÒÖÕÛÚÙÜÇ', 'AAAAAEEEEIIIIOOOOOUUUUC') ilike '%'||?||'%'";
	}

	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		return new TypedValue[] { criteriaQuery.getTypedValue( criteria, propertyName, Normalizer.normalize(value.toString(),Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase()) };
	}

}