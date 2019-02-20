package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.vividsolutions.jts.geom.Geometry;
import utils.GeoJsonUtils;

import java.lang.reflect.Type;

public class GeometryDeserializer implements JsonDeserializer<Geometry> {

	@Override
	public Geometry deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		if (json.isJsonPrimitive())
			return parseGeometry(json.getAsJsonPrimitive().getAsString());
		else
			return parseGeometry(json.getAsJsonObject().toString());
	}

	public static Geometry parseGeometry(String geoJson) {
		return GeoJsonUtils.toGeometry(geoJson);
	}
}
