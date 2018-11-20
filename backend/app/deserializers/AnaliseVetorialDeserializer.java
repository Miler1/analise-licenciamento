package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.manejoDigital.AnaliseVetorial;

import java.lang.reflect.Type;
import java.util.Date;

public class AnaliseVetorialDeserializer implements JsonDeserializer<AnaliseVetorial> {

	@Override
	public AnaliseVetorial deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

		AnaliseVetorial analise = new AnaliseVetorial();

		JsonObject jsonObject = json.getAsJsonObject();

		analise.tipo = jsonObject.get("vet_tipo") == null ? null : jsonObject.get("vet_tipo").getAsString();
		analise.nome = jsonObject.get("vet_nome") == null ? null : jsonObject.get("vet_nome").getAsString();
		analise.sobreposicaoAmf = jsonObject.get("amf_sobreposicao") == null ? null : jsonObject.get("amf_sobreposicao").getAsDouble();
		analise.distanciaAmf = jsonObject.get("amf_distancia") == null ? null : jsonObject.get("amf_distancia").getAsDouble();
		analise.sobreposicaoPropriedade = jsonObject.get("pro_sobreposicao") == null ? null : jsonObject.get("pro_sobreposicao").getAsDouble();
		analise.distanciaPropriedade = jsonObject.get("pro_distancia") == null ? null : jsonObject.get("pro_distancia").getAsDouble();

		return analise;
	}
}
