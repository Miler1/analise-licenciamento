package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import play.Play;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {

    public static final String DATE_FORMAT = Play.configuration.getProperty("date.format");

    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
    	return parseDate(json.getAsJsonPrimitive().getAsString());
    }

    public static Date parseDate(String dateText) {
        try {
          return new SimpleDateFormat(DATE_FORMAT).parse(dateText);
        } catch (ParseException e) {
          e.printStackTrace();
          return null;
        }
    }
}
