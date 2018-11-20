package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.analiseShape.GeometriaArcgis;

import java.lang.reflect.Type;

public class GeometriaArcgisDeserializer implements JsonDeserializer<GeometriaArcgis> {

	@Override
	public GeometriaArcgis deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		GeometriaArcgis geometria = new GeometriaArcgis();

		JsonObject jsonObject = json.getAsJsonObject();

		geometria.attributes = null;
		geometria.geometry = jsonObject.get("geometry") == null ? null : jsonObject.get("geometry").toString();

		return geometria;
	}
}
