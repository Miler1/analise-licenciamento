package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.analiseShape.AtributosQueryAMFManejo;

import java.lang.reflect.Type;

public class AtributosQueryAMFManejoDeserializer implements JsonDeserializer<AtributosQueryAMFManejo> {

	@Override
	public AtributosQueryAMFManejo deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		AtributosQueryAMFManejo atributos = new AtributosQueryAMFManejo();

		JsonObject jsonObject = json.getAsJsonObject();

		atributos.area = jsonObject.get("gisdb.gisadmin.AMF_MANEJO.area") == null ? null : jsonObject.get("gisdb.gisadmin.AMF_MANEJO.area").getAsDouble();
		atributos.objeto = jsonObject.get("objeto") == null ? null : jsonObject.get("objeto").getAsString();

		return atributos;
	}
}