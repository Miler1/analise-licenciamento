package serializers;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vividsolutions.jts.geom.Geometry;

import flexjson.transformer.Transformer;
import utils.GeoJsonUtils;
import utils.flexjson.GeometryTransformer;

import java.lang.reflect.Type;

public class GeometrySerializer implements JsonSerializer<Geometry> {

	@Override
	public JsonElement serialize(Geometry geometria, Type arg1, JsonSerializationContext arg2) {

		if (geometria == null)
			return null;

		return new JsonPrimitive(GeoJsonUtils.toGeoJson(geometria));
	}
	
	private static GeometryTransformer geometryTransform;

	public static Transformer getTransformer() {

		return getTransformer(true);
	}

	public static Transformer getTransformer(Boolean writeQuoted) {
		
		return geometryTransform = new GeometryTransformer(writeQuoted);
	}
}
