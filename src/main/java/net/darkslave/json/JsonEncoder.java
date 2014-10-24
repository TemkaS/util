/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.darkslave.io.StringWriter;
import net.darkslave.reflect.Property;
import net.darkslave.reflect.Reflect;





public class JsonEncoder {

    /**
     * Сериализация объекта в json формат
     *
     * @param source - исходный объект
     * @return сериализованную строку
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static String encode(Object source) throws IOException {
        StringWriter writer = new StringWriter(512);
        encode(source, writer);
        return writer.toString();
    }


    public static void encode(Object source, Writer writer) throws IOException {
        new JsonEncoder(writer).encode(source, 0);
    }





    /**********************************************************************************************
    */
    private static final String EMPTY_STRING = "";

    private static final String MARK_NULL = "null";
    private static final String ITEMS_SEP = ", ";
    private static final String ENTRY_SEP = ": ";
    private static final String ENTRY_ESC = "\"";
    private static final String LIST_BEG  = "[";
    private static final String LIST_END  = "]";
    private static final String MAPS_BEG  = "{";
    private static final String MAPS_END  = "}";

    private static final int ENTRY_ESC_PADD = ENTRY_ESC.length() << 1;


    /**
     * Сериализация объекта в json формат для использования в именах свойств
     *
     * @param source - исходный объект
     * @return сериализованную строку
     */
    public static String encodeKey(Object source) {
        if (source == null)
            return MARK_NULL;

        String temp = escape(source);

        StringBuilder result = new StringBuilder(temp.length() + ENTRY_ESC_PADD);

        result.append(ENTRY_ESC);
        result.append(temp);
        result.append(ENTRY_ESC);

        return result.toString();
    }


    /**
     * Экранирование строки
     *
     * @param source - исходная строка
     * @return экранированную строку
     */
    public static String escape(Object source) {
        if (source == null)
            return EMPTY_STRING;

        String temp = source.toString();

        int length = temp.length();
        if (length == 0)
            return EMPTY_STRING;

        StringBuilder result = new StringBuilder(length + (length >>> 3) + 16);

        for (int i = 0; i < length; i++) {
            char value = temp.charAt(i);
            if (value <= 0x1f || (value >= 0x7f && value <= 0x9f)) {
                printHex(result, "\\u", value, 16);
            } else
            if (value == '\"' || value == '\\') {
                result.append('\\').append(value);
            } else {
                result.append(value);
            }
        }

        return result.toString();
    }


    private static final char[] HEX_CHARS = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };


    private static void printHex(StringBuilder result, String prefix, int value, int index) {
        result.append(prefix);

        while ((index-= 4) >= 0)
            result.append(HEX_CHARS[(value >>> index) & 15]);

    }



    /**********************************************************************************************
    */
    private final List<Object> stack;
    private final Writer writer;


    private JsonEncoder(Writer w) {
        stack  = new ArrayList<Object>(32);
        writer = w;
    }


    private void check(Object value, int level) throws JsonException {
        if (level < stack.size()) {
            stack.set(level, value);
        } else {
            stack.add(level, value);
        }

        for (int index = 0; index < level; index++)
            if (stack.get(index) == value)
                throw new JsonException("Recursion found for " + value.getClass());

    }


    private void write(String source) throws IOException {
        writer.write(source);
    }


    private void write(Object source) throws IOException {
        writer.write(source.toString());
    }


    private void encode(Object value, int level) throws IOException {
        // null
        if (value == null) {
            write(MARK_NULL);
            return;
        }

        // примитивы и обертки примитивов
        if (value instanceof Boolean
                || value instanceof Number) {
            write(value);
            return;
        }

        // символы и строки
        if (value instanceof CharSequence
                || value instanceof Character) {
            write(ENTRY_ESC);
            write(escape(value));
            write(ENTRY_ESC);
            return;
        }

        // дата
        if (value instanceof Date) {
            write(((Date) value).getTime());
            return;
        }

        // календарь
        if (value instanceof Calendar) {
            write(((Calendar) value).getTimeInMillis());
            return;
        }

        // проверка рекурсии
        check(value, level);

        Class<?> clazz = value.getClass();

        // массив
        if (clazz.isArray()) {
            writeArray(value, level);
            return;
        }

        // коллекция
        if (value instanceof Iterable) {
            writeIterable(value, level);
            return;
        }

        // таблица
        if (value instanceof Map) {
            writeMap(value, level);
            return;
        }

        // прочие объекты
        getEncoder(clazz).encode(this, value, level);
    }


    private void writeArray(Object value, int level) throws IOException {
        int count = Array.getLength(value);
        int index = 0;

        write(LIST_BEG);

        while (index < count) {
            if (index > 0)
                write(ITEMS_SEP);

            encode(Array.get(value, index), level + 1);
            index++;
        }

        write(LIST_END);
    }


    private void writeIterable(Object value, int level) throws IOException {
        int index = 0;

        write(LIST_BEG);

        for (Object item : (Iterable<?>) value) {
            if (index > 0)
                write(ITEMS_SEP);

            encode(item, level + 1);
            index++;
        }

        write(LIST_END);
    }


    private void writeMap(Object value, int level) throws IOException {
        int index = 0;

        write(MAPS_BEG);

        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            if (index > 0)
                write(ITEMS_SEP);

            write(encodeKey(entry.getKey()));
            write(ENTRY_SEP);

            encode(entry.getValue(), level + 1);
            index++;
        }

        write(MAPS_END);
    }


    private void encodeReplace(Method delegate, Object value, int level) throws IOException {
        Object result;

        try {
            result = delegate.invoke(value);
        } catch (ReflectiveOperationException re) {
            throw new JsonException("Encode " + value.getClass() + " error", re);
        }

        encode(result, level);
    }


    private void encodeProperty(Map<String, Property> properties, Object value, int level) throws IOException {
        int index = 0;

        write(MAPS_BEG);

        for (Map.Entry<String, Property> entry : properties.entrySet()) {
            if (index > 0)
                write(ITEMS_SEP);

            write(encodeKey(entry.getKey()));
            write(ENTRY_SEP);

            Object result;

            try {
                result = entry.getValue().get(value);
            } catch (ReflectiveOperationException re) {
                throw new JsonException("Encode " + value.getClass() + " error", re);
            }

            encode(result, level + 1);
            index++;
        }

        write(MAPS_END);
    }



    /**********************************************************************************************
    */

    private static interface Encoder {
        void encode(JsonEncoder encoder, Object value, int level) throws IOException;
    }


    private static class ReplaceEncoder implements Encoder {
        private final Method delegate;

        public ReplaceEncoder(Method delegate) {
            if (delegate == null)
                throw new IllegalArgumentException("Parameter can't be null");
            this.delegate = delegate;
        }

        @Override
        public void encode(JsonEncoder encoder, Object value, int level) throws IOException {
            encoder.encodeReplace(delegate, value, level);
        }

    }


    private static class PropertyEncoder implements Encoder {
        private final Map<String, Property> properties;

        public PropertyEncoder(Map<String, Property> properties) {
            if (properties == null)
                throw new IllegalArgumentException("Parameter can't be null");
            this.properties = properties;
        }

        @Override
        public void encode(JsonEncoder encoder, Object value, int level) throws IOException {
            encoder.encodeProperty(properties, value, level);
        }

    }


    private static final Map<Class<?>, Encoder> Encoders = new ConcurrentHashMap<Class<?>, Encoder>();


    private static Encoder getEncoder(Class<?> clazz) throws JsonException {
        final Encoder cached = Encoders.get(clazz);

        if (cached != null)
            return cached;

        try {
            final Encoder result = getEncoder0(clazz);
            Encoders.put(clazz, result);

            return result;

        } catch (ReflectiveOperationException e) {
            throw new JsonException("Get encoder " + clazz + " error", e);
        }
    }


    private static Encoder getEncoder0(Class<?> clazz) throws ReflectiveOperationException {
        final Class<?> origin = clazz;
        String targetName;

        while (clazz != null) {
            JsonSerialize anno = clazz.getAnnotation(JsonSerialize.class);

            if (anno == null) {
                clazz = clazz.getSuperclass();
                continue;
            }

            // указан метод замены целевого объекта сериализации
            if (!isEmpty(targetName = anno.replaceWith())) {
                Method target = clazz.getDeclaredMethod(targetName);
                target.setAccessible(true);
                return new ReplaceEncoder(target);
            }

            // указаны поля и методы сериализации
            JsonProperty[] properties = anno.value();

            if (isEmpty(properties))
                throw new ReflectiveOperationException("JsonProperty list is not defined");

            Map<String, Property> result = new LinkedHashMap<String, Property>();

            for (JsonProperty prop : properties) {
                String name = prop.value();

                // указан метод для сериализации свойства
                if (!isEmpty(targetName = prop.method())) {
                    Method target = clazz.getDeclaredMethod(targetName);
                    target.setAccessible(true);

                    if (isEmpty(name))
                        name = targetName;

                    result.put(name, Property.create(target));
                    continue;
                }

                // указано поле для сериализации свойства
                if (!isEmpty(targetName = prop.field()) || !isEmpty(targetName = name)) {
                    Field target = clazz.getDeclaredField(targetName);
                    target.setAccessible(true);

                    if (isEmpty(name))
                        name = targetName;

                    result.put(name, Property.create(target));
                    continue;
                }

            }

            return new PropertyEncoder(result);
        }

        // если ничего не указано, сериализуем все не статичные поля объекта
        Map<String, Property> result = new HashMap<String, Property>();

        for (Map.Entry<String, Field> e : Reflect.getFields(origin).entrySet()) {
            Field field = e.getValue();

            if ((field.getModifiers() & Modifier.STATIC) != 0)
                continue;

            result.put(e.getKey(), Property.create(field));
        }

        return new PropertyEncoder(result);
    }


    private static boolean isEmpty(CharSequence source) {
        return source == null || source.length() == 0;
    }


    private static boolean isEmpty(Object[] source) {
        return source == null || source.length == 0;
    }


}
