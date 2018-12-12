package serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import flexjson.transformer.Transformer;
import transformers.DoubleTransformer;

import java.lang.reflect.Type;

public class DoubleSerializer implements JsonSerializer<Double> {

    private static DoubleTransformer doubleTransformer;

    public static Transformer getTransformer() {

        if (doubleTransformer == null)
            doubleTransformer = new DoubleTransformer();

        return doubleTransformer;
    }

    @Override
    public JsonElement serialize(Double valor, Type type, JsonSerializationContext context) {

        return new JsonPrimitive(valor);
    }
}