package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.manejoDigital.analise.analiseTecnica.BaseVetorial;

import java.lang.reflect.Type;
import java.util.Date;

public class BaseVetorialDeserializer implements JsonDeserializer<BaseVetorial> {

    @Override
    public BaseVetorial deserialize(JsonElement json, Type type, JsonDeserializationContext context) {

        BaseVetorial baseVetorial = new BaseVetorial();

        JsonObject jsonObject = json.getAsJsonObject();

        baseVetorial.nome = jsonObject.get("str_dados_oficiais") == null ? null : jsonObject.get("str_dados_oficiais").getAsString();
        baseVetorial.fonte = jsonObject.get("str_fonte") == null ? null : jsonObject.get("str_fonte").getAsString();
        baseVetorial.ultimaAtualizacao = jsonObject.get("dt_ult_atualizacao") == null ? null : new Date(jsonObject.get("dt_ult_atualizacao").getAsLong());
        baseVetorial.observacao = jsonObject.get("txt_obs").isJsonNull() || jsonObject.get("txt_obs") == null ? null : jsonObject.get("txt_obs").getAsString();

        return baseVetorial;
    }
}
