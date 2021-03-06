package deserializers;

import com.google.gson.*;
import models.manejoDigital.analise.analiseShape.AtributosAddLayer;
import models.manejoDigital.analise.analiseShape.FeatureAddLayer;

import java.lang.reflect.Type;

public class FeatureAddLayerDeserializer implements JsonDeserializer<FeatureAddLayer> {

    @Override
    public FeatureAddLayer deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy")
                .create();

        FeatureAddLayer featureAddLayer = new FeatureAddLayer();

        JsonObject jsonObject = json.getAsJsonObject();

        featureAddLayer.attributes = jsonObject.get("attributes") == null ? null : gson.fromJson(jsonObject.get("attributes"), AtributosAddLayer.class);
        featureAddLayer.geometry = jsonObject.get("geometry") == null ? null : jsonObject.get("geometry").toString();

        return featureAddLayer;
    }
}
