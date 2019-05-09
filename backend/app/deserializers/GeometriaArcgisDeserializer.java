package deserializers;

import com.google.gson.*;
import models.manejoDigital.analise.analiseShape.GeometriaArcgis;
import models.manejoDigital.analise.analiseShape.GeometryArcgis;

import java.lang.reflect.Type;

public class GeometriaArcgisDeserializer implements JsonDeserializer<GeometriaArcgis> {

	@Override
	public GeometriaArcgis deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy")
				.registerTypeAdapter(GeometryArcgis.class, new GeometryArcgisDeserializer())
				.create();

		GeometriaArcgis geometria = new GeometriaArcgis();

		JsonObject jsonObject = json.getAsJsonObject();

		geometria.attributes = null;
		geometria.geometry = jsonObject.get("geometry") == null ? null : gson.fromJson(jsonObject.get("geometry"), GeometryArcgis.class);

		return geometria;
	}
}
