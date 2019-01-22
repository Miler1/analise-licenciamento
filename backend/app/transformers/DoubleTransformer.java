package transformers;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

import java.lang.reflect.Type;
import java.util.Locale;

public class DoubleTransformer extends AbstractTransformer implements ObjectFactory {

    public DoubleTransformer() {
    }

    public void transform(Object value) {

        if (value == null) {

            getContext().write("null");

            return;
        }
        getContext().write(formatter((Double) value));
    }

    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {

        return formatter((Double) value);
    }

    private String formatter(Double doubleValue) {

        return String.format("%,.4f", doubleValue).replace(".", "").replace(',', '.');
    }
}
