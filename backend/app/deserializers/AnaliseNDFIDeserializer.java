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

			analise.satelite =  elementIsValido(jsonObject.get("satelite")) ? jsonObject.get("satelite").getAsString() : null;
			analise.orbita =  elementIsValido(jsonObject.get("orb_ponto")) ? Integer.valueOf(jsonObject.get("orb_ponto").getAsString().substring(0, jsonObject.get("orb_ponto").getAsString().indexOf('_'))) : null;
			analise.ponto =  elementIsValido(jsonObject.get("orb_ponto")) ? Integer.valueOf( jsonObject.get("orb_ponto").getAsString().substring(jsonObject.get("orb_ponto").getAsString().indexOf('_') + 1)) : null;
			analise.valor = elementIsValido(jsonObject.get("ndfi")) ? jsonObject.get("ndfi").getAsDouble() : 0.0;
			analise.area =  elementIsValido(jsonObject.get("area_ha")) ? jsonObject.get("area_ha").getAsDouble() : 0.0;
			analise.nivelExploracao =  elementIsValido(jsonObject.get("nv_exploracao")) ? jsonObject.get("nv_exploracao").getAsString() : "INDEFINIDO";

		} catch ( Exception e) {

			e.printStackTrace();
		}

		return analise;
	}

	private Boolean elementIsValido(JsonElement elemento) {

		return !(elemento == null || elemento.isJsonNull());
	}
}