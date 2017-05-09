package utils;

import java.util.Collection;

import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;

public class ModelUtil {

	public static <T extends GenericModel> void deleteAll(Collection<T> models) {
		
		if (models == null || models.isEmpty())
			return;
		
		for (T model : models) {
			model.delete();
		}
	}
}
