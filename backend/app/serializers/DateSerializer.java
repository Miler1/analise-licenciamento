package serializers;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import play.Play;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import flexjson.transformer.DateTransformer;
import flexjson.transformer.Transformer;

public class DateSerializer implements JsonSerializer<Date> {

	private static final String DATE_FORMAT = Play.configuration.getProperty("date.format");
	private static final String DATE_FORMAT_TIMEABLE = Play.configuration.getProperty("date.format.timeable");
	
	private static DateTransformer dateTransformer;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	public static Transformer getTransformer() {
		
		if (dateTransformer == null)
			dateTransformer = new DateTransformer(DATE_FORMAT);
		
		return dateTransformer;
	}

	@Override
	public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {

		return new JsonPrimitive(dateFormat.format(date));
	}

	public static Transformer getTransformerWithDateTime() {

		dateFormat = new SimpleDateFormat(DATE_FORMAT_TIMEABLE);

		dateTransformer = new DateTransformer(DATE_FORMAT_TIMEABLE);

		return dateTransformer;
	}

}