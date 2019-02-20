package utils;

public class PersistUtils {
	
	public static String like(String value) {
		
		return (value != null && !value.isEmpty()) ? "%" + value + "%" : null;
	}
	
}
