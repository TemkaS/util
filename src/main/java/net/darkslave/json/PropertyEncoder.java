/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import net.darkslave.reflect.Property;
import net.darkslave.reflect.Reflect;
import net.darkslave.util.Misc;





/**
 * Сериализатор полей и методов объекта
 */
public class PropertyEncoder implements ObjectEncoder {
    private final Iterable<Property> properties;


    public PropertyEncoder(Iterable<Property> properties) {
        this.properties = properties;
    }


    @Override
    public void encode(JsonEncoder encoder, Object value, int level) throws IOException {
        int index = 0;

        encoder.write(JsonEncoder.MAPS_BEG);

        for (Property prop : properties) {
            if (index > 0)
                encoder.write(JsonEncoder.ITEMS_SEP);

            encoder.writeString(prop.getName());
            encoder.write(JsonEncoder.ENTRY_SEP);

            Object result;

            try {
                result = prop.get(value);
            } catch (ReflectiveOperationException re) {
                throw new JsonException("Encode " + value.getClass() + " error", re);
            }

            encoder.encode(result, level + 1);
            index++;
        }

        encoder.write(JsonEncoder.MAPS_END);
    }


    /**
     * Создать новый сериализатор
     */
    public static ObjectEncoder create(Class<?> targetClass, JsonProperty[] source) throws ReflectiveOperationException {
        Collection<Property> result = new ArrayList<>(source.length);

        for (JsonProperty prop : source) {
            result.add(newProperty(targetClass, PropertyData.from(prop)));
        }

        return new PropertyEncoder(result);
    }


    /**
     * Создать новый сериализатор
     */
    public static ObjectEncoder create(Class<?> targetClass, Collection<PropertyData> source) throws ReflectiveOperationException {
        Collection<Property> result = new ArrayList<>(source.size());

        for (PropertyData prop : source) {
            result.add(newProperty(targetClass, prop));
        }

        return new PropertyEncoder(result);
    }


    private static Property newProperty(Class<?> targetClass, PropertyData source) throws ReflectiveOperationException {
        String outputName = source.value();
        String name;

        // указан метод для сериализации свойства
        if (!Misc.isEmpty(name = source.method())) {
            Method target = Reflect.getMethod(targetClass, name);

            if (target == null)
                throw new NoSuchMethodException(name + " in " + targetClass);

            if (Misc.isEmpty(outputName))
                outputName = name;

            return Property.from(outputName, target);
        }


        // указано поле для сериализации свойства
        if (!Misc.isEmpty(name = source.field()) || !Misc.isEmpty(name = outputName)) {
            Field target = Reflect.getField(targetClass, name);

            if (target == null)
                throw new NoSuchFieldException(name + " in " + targetClass);

            if (Misc.isEmpty(outputName))
                outputName = name;

            return Property.from(outputName, target);
        }


        throw new IllegalArgumentException("Neither Field nor Method name are defined");
    }


}

