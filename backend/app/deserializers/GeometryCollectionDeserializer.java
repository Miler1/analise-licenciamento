package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.vividsolutions.jts.geom.GeometryCollection;
import utils.GeoJsonUtils;

import java.lang.reflect.Type;

public class GeometryCollectionDeserializer implements JsonDeserializer<GeometryCollection> {

	@Override
	public GeometryCollection deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		if (json.isJsonPrimitive())
			return parseGeometryCollection(json.getAsJsonPrimitive().getAsString());
		else
			return parseGeometryCollection(json.getAsJsonObject().toString());
	}

	public static GeometryCollection parseGeometryCollection(String geoJson) {
		return GeoJsonUtils.toGeometryCollection(geoJson);
	}
}
