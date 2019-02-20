package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.manejoDigital.analise.analiseTecnica.AnaliseNdfi;
import play.Play;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AnaliseNDFIDeserializer implements JsonDeserializer<AnaliseNdfi> {

	@Override
	public AnaliseNdfi deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		AnaliseNdfi analise = new AnaliseNdfi();

		JsonObject jsonObject = json.getAsJsonObject();

		try {

			analise.dataAnalise = (jsonObject.get("data") == null) ? null :
					new SimpleDateFormat(Play.configuration.getProperty("date.format.invert"))
							.parse(jsonObject.get("data").getAsString());

		} catch (ParseException e) {

			e.printStackTrace();
		}

		analise.orbita = jsonObject.get("orb_ponto") == null ? null : Integer.valueOf(jsonObject.get("orb_ponto").getAsString().substring(0, jsonObject.get("orb_ponto").getAsString().indexOf('_')));
		analise.ponto = jsonObject.get("orb_ponto") == null ? null : Integer.valueOf( jsonObject.get("orb_ponto").getAsString().substring(jsonObject.get("orb_ponto").getAsString().indexOf('_') + 1));
		analise.satelite = jsonObject.get("satelite") == null ? null : jsonObject.get("satelite").getAsString();
		analise.valor = jsonObject.get("ndfi") == null ? null : jsonObject.get("ndfi").getAsDouble();
		analise.area = jsonObject.get("area_ha") == null ? null : jsonObject.get("area_ha").getAsDouble();
		analise.nivelExploracao = jsonObject.get("nv_exploracao") == null ? null : jsonObject.get("nv_exploracao").getAsString();

		return analise;
	}
}