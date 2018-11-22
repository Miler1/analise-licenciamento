package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.manejoDigital.AnaliseNdfi;

import java.lang.reflect.Type;
import java.util.Date;

public class AnaliseNDFIDeserializer implements JsonDeserializer<AnaliseNdfi> {

	@Override
	public AnaliseNdfi deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		AnaliseNdfi analise = new AnaliseNdfi();

		JsonObject jsonObject = json.getAsJsonObject();

		analise.dataAnalise = jsonObject.get("data") == null ? null : new Date(jsonObject.get("data").getAsLong());
		analise.orbita = jsonObject.get("orb_ponto") == null ? null : Integer.valueOf(jsonObject.get("orb_ponto").getAsString().substring(jsonObject.get("orb_ponto").getAsString().indexOf('/')));
		analise.ponto = jsonObject.get("orb_ponto") == null ? null : Integer.valueOf(jsonObject.get("orb_ponto").getAsString().substring(0, jsonObject.get("orb_ponto").getAsString().indexOf('/')));
		analise.satelite = jsonObject.get("satelite") == null ? null : jsonObject.get("satelite").getAsString();
		analise.valor = jsonObject.get("ndfi") == null ? null : jsonObject.get("ndfi").getAsDouble();
		analise.area = jsonObject.get("gisdb.gisadmin.AMF_RESUMO_NDFI.area") == null ? null : jsonObject.get("gisdb.gisadmin.AMF_RESUMO_NDFI.area").getAsDouble();
		analise.nivelExploracao = jsonObject.get("nv_exploracao") == null ? null : jsonObject.get("nv_exploracao").getAsString();

		return analise;
	}
}