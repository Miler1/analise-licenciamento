package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.manejoDigital.analise.analiseTecnica.Insumo;
import play.Play;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InsumoDeserializer implements JsonDeserializer<Insumo> {

	@Override
	public Insumo deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		Insumo insumo = new Insumo();

		JsonObject jsonObject = json.getAsJsonObject();

		try {

			insumo.data = (jsonObject.get("data") == null) ? null :
					new SimpleDateFormat(Play.configuration.getProperty("date.format.invert"))
							.parse(jsonObject.get("data").getAsString());

		} catch (ParseException e) {

			e.printStackTrace();
		}

		insumo.satelite = jsonObject.get("satelite") == null ? null : jsonObject.get("satelite").getAsString();
		insumo.orbPonto = jsonObject.get("orb_ponto") == null ? null : jsonObject.get("orb_ponto").getAsString();

		return insumo;
	}
}
