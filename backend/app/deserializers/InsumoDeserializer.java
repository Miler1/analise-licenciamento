package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.manejoDigital.Insumo;

import java.lang.reflect.Type;
import java.util.Date;

public class InsumoDeserializer implements JsonDeserializer<Insumo> {

	@Override
	public Insumo deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		Insumo insumo = new Insumo();

		JsonObject jsonObject = json.getAsJsonObject();

		insumo.data = jsonObject.get("ano") == null ? null : new Date(jsonObject.get("ano").getAsLong());
		insumo.satelite = jsonObject.get("satelite") == null ? null : jsonObject.get("satelite").getAsString();
		insumo.orbPonto = jsonObject.get("orb_ponto") == null ? null : jsonObject.get("orb_ponto").getAsString();

		return insumo;
	}
}
