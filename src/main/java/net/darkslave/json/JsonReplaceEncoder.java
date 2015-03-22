/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;


import java.io.IOException;
import java.lang.reflect.Method;
import net.darkslave.reflect.Property;
import net.darkslave.reflect.Reflect;





/**
 * Сериализатор замены текущего объекта сериализации
 */
public class JsonReplaceEncoder implements JsonObjectEncoder {
    private final Property delegate;


    public JsonReplaceEncoder(Property delegate) {
        this.delegate = delegate;
    }


    @Override
    public void encode(JsonEncoder encoder, Object value, int level) throws IOException {
        Object result;

        try {
            result = delegate.get(value);
        } catch (ReflectiveOperationException re) {
            throw new JsonException("Encode " + value.getClass() + " error", re);
        }

        encoder.encode(result, level);
    }


    /**
     * Создать новый сериализатор
     */
    public static JsonObjectEncoder create(Class<?> targetClass, String name) throws ReflectiveOperationException {
        Method target = Reflect.getMethod(targetClass, name);

        if (target == null)
            throw new NoSuchMethodException(name + " in " + targetClass);

        return new JsonReplaceEncoder(Property.from(name, target));
    }

}

