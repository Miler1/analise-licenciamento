package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

public class ListUtil {

	public static <T> List<T> createList(T ... objs) {

		if (objs == null || objs.length == 0) {

			return new ArrayList<T>();

		} else {

			List<T> list = new ArrayList<T>();

			for (T obj : objs) {
				list.add(obj);
			}

			return list;
		}
	}

	public static <T extends Object> String toString(Collection<T> objs, String separator) {

		return toString(objs, separator, null, null);
	}

	public static <T extends Object> String toString(Collection<T> objs, 
			String separator,
			String elementPrefix, 
			String elementPosfix) {

		elementPosfix = (elementPosfix != null) ? elementPosfix : "";
		elementPrefix = (elementPrefix != null) ? elementPrefix : "";

		String str = "";

		if (objs != null && objs.size() > 0) {

			for (Object obj : objs) {

				if (!str.isEmpty())
					str += separator;

				str += elementPrefix + obj + elementPosfix;
			}
		}

		return str;
	}

	public static <T extends Identificavel> List<Long> getIds(Collection<T> models) {

		if (models == null || models.isEmpty()) {

			return new ArrayList<Long>();
		}

		List<Long> ids = new ArrayList<Long>();

		for (Identificavel model : models) {

			ids.add(model.getId());
		}

		return ids;
	}
	
	public static <T extends Identificavel> Long[] getIdsAsArray(Collection<T> models) {

		if (models == null || models.isEmpty()) {

			return new Long[]{};
		}

		Long [] ids = new Long[models.size()];

		int i=0;
		for (Identificavel model : models) {

			ids[i] = model.getId();
			i++;
		}

		return ids;
	}

	public static <T extends Identificavel> T getById(Long id, Collection<T> list) {

		if (list != null && !list.isEmpty()) {

			for (T listItem : list) {

				if (listItem.getId() != null && 
						listItem.getId().equals(id)) {

					return listItem;
				}
			}
		}

		return null;
	}

	public static <T extends Identificavel> Map<Long, T> mapById(List<T> list) {

		Map<Long, T> map = new HashMap<Long, T>(); 

		if (list != null) {

			for (T item : list) {
				map.put(item.getId(), item);
			}
		}

		return map;
	}
	
	public static <T extends Object> Long[] getArrayOfLongs(Collection<T> models) {

		if (models == null || models.isEmpty()) {

			return new Long[]{};
		}

		Long [] ids = new Long[models.size()];

		int i=0;
		for (Object model : models) {

			ids[i] = (Long) model;
			i++;
		}

		return ids;
	}
	
	public static <T> Boolean containsAllNulls(Collection<T> list){
		
		if(list != null){
	        
			for(Object a : list)
				if(a != null) 
					return false;
	    }
	    return true;
		
	}
	
	public static void clear(Collection ... lists ) {
		
		if (lists != null) {
			
			for (Collection list : lists) {
				
				if (list != null)
					list.clear();
			}
		}
	}
	
	public static <T extends Identificavel> List<T> orderByIdList(List<T> objs, List<Long> ids) {
		
		List<T> objsOrdenados = new ArrayList<T>(objs);
		
		if (objs != null && !objs.isEmpty()) {
			
			for (T obj : objs) {
				
				int index = ids.indexOf(obj.getId());
				objsOrdenados.set(index, obj);
			}
		}
		
		return objsOrdenados;
	}
	
	public static <T extends Identificavel> boolean containsAll(List<T> objs, Long ... ids) {
		
		if (objs == null || objs.isEmpty() || ids == null || ids.length == 0)
			return false;
		
		if (objs.size() != ids.length)
			return false;
		
		for (long id : ids) {
			
			if (getById(id, objs) == null)
				return false;
		}
		
		return true;
	}
	
	public static <T extends Identificavel> boolean contains(Long id, Long ... list) {

		boolean exists = false;
		
		if (list != null) {

			for (Long listItem : list) {

				if (listItem == id) {
					exists = true;
				}
			}
		}

		return exists;
		
	}
	
	public static <T> List<T> getElementsAfter(T [] array, T element) {
		
		int index = ArrayUtils.indexOf(array, element);
		
		// Se não encontrar ou for o último elemento;
		if (index < 0 || index == array.length -1)
			return null;
		
		List<T> elementsAfter = new ArrayList<>();
		
		for (int i = index + 1; i < array.length; i++) {
			
			elementsAfter.add( array[i] );
		}
		
		return elementsAfter;
	}
	
	public static <T> List<T> getElementsBefore(T [] array, T element) {
		
		int index = ArrayUtils.indexOf(array, element);
		
		// Se não encontrar ou for o último elemento;
		if (index <= 0)
			return null;
		
		List<T> elementsBefore = new ArrayList<>();
		
		for (int i = 0; i < index; i++) {
			
			elementsBefore.add( array[i] );
		}
		
		return elementsBefore;
	}
	
	public static List<Long> toLongList(Collection<Number> ints) {
		
		if (ints == null)
			return null;
		
		List<Long> longs = new ArrayList<>();
		
		if (ints.isEmpty())
			return longs;
		
		for (Number i : ints)
			longs.add( i.longValue() );
		
		return longs;
	}
	
	public static <T> String toTextItens(String prefixo, List<T> list) {
		
		StringBuilder sb = new StringBuilder();
		
		if (list == null || list.isEmpty())
			return "";
		
		for (T obj : list)
			sb.append(prefixo).append(obj.toString()).append("\n");
		
		return sb.toString();
	}
}
