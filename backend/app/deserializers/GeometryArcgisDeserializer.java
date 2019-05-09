package deserializers;

import com.google.gson.*;
import models.manejoDigital.analise.analiseShape.GeometryArcgis;
import models.manejoDigital.analise.analiseShape.SpatialReferenceArcgis;

import java.lang.reflect.Type;

public class GeometryArcgisDeserializer implements JsonDeserializer<GeometryArcgis> {

    @Override
    public GeometryArcgis deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy")
                .create();

        GeometryArcgis geometria = new GeometryArcgis();

        JsonObject jsonObject = json.getAsJsonObject();

        geometria.spatialReference = jsonObject.get("spatialReference") == null ? null : gson.fromJson(jsonObject.get("spatialReference"), SpatialReferenceArcgis.class);
        geometria.rings = jsonObject.get("rings") == null ? null : jsonObject.get("rings").toString();

        return geometria;
    }
}