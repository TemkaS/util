/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;

import java.io.IOException;
import java.io.StringWriter;
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
import net.darkslave.reflect.Property;
import net.darkslave.reflect.Reflect;
import net.darkslave.util.Misc;





public class CopyOfJsonEncoder {

    /**
     * Сериализация объекта в json формат
     *
     * @param source - исходный объект
     * @return сериализованную строку
     * @throws JsonException
     * @throws IOException
     */
    public static String encode(Object source) throws JsonException, IOException {
        StringWriter writer = new StringWriter();
        encode(source, writer);
        return writer.toString();
    }


    public static void encode(Object source, Writer writer) throws JsonException, IOException {
        new CopyOfJsonEncoder(writer).encode(EMPTY_STRING, source, 0);
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
            if (value < ' ' || value == '\"' || value == '\\') {
                switch (value) {
                    case '\t':
                        result.append("\\t");
                    break;
                    case '\r':
                        result.append("\\r");
                    break;
                    case '\n':
                        result.append("\\n");
                    break;
                    case '\f':
                        result.append("\\f");
                    break;
                    case '\"':
                        result.append("\\\"");
                    break;
                    case '\\':
                        result.append("\\\\");
                    break;
                    default:
                        printHex(result, "\\x", value, 8);
                    break;
                }
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
    private final Stack  stack;
    private final Writer writer;


    private CopyOfJsonEncoder(Writer w) {
        stack  = new Stack(32);
        writer = w;
    }


    private void write(String source) throws JsonException, IOException {
        writer.write(source);
    }


    private void write(Object source) throws JsonException, IOException {
        writer.write(source.toString());
    }


    private void encode(Object field, Object value, int level) throws JsonException, IOException {
        if (value == null) {
            write(MARK_NULL);
            return;
        }

        if (value instanceof Boolean
                || value instanceof Number) {
            write(value);
            return;
        }

        if (value instanceof CharSequence
                || value instanceof Character) {
            write(ENTRY_ESC);
            write(escape(value));
            write(ENTRY_ESC);
            return;
        }

        if (value instanceof Date) {
            write(((Date) value).getTime());
            return;
        }

        if (value instanceof Calendar) {
            write(((Calendar) value).getTimeInMillis());
            return;
        }

        stack.check(field, value, level);

        Class<?> clazz = value.getClass();

        if (clazz.isArray()) {
            writeArray(value, level);
            return;
        }

        if (value instanceof Iterable) {
            writeIterable(value, level);
            return;
        }

        if (value instanceof Map) {
            writeMap(value, level);
            return;
        }


        try {
            getEncoder(clazz).encode(this, field, value, level);
        } catch (ReflectiveOperationException e) {
            throw new JsonException(e);
        }

    }




    private void writeArray(Object value, int level) throws JsonException, IOException {
        int count = Array.getLength(value);
        int index = 0;

        write(LIST_BEG);

        while (index < count) {
            if (index > 0)
                write(ITEMS_SEP);

            encode(index, Array.get(value, index), level + 1);
            index++;
        }

        write(LIST_END);
    }


    private void writeIterable(Object value, int level) throws JsonException, IOException {
        int index = 0;

        write(LIST_BEG);

        for (Object item : (Iterable<?>) value) {
            if (index > 0)
                write(ITEMS_SEP);

            encode(index, item, level + 1);
            index++;
        }

        write(LIST_END);
    }


    private void writeMap(Object value, int level) throws JsonException, IOException {
        int index = 0;

        write(MAPS_BEG);

        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            if (index > 0)
                write(ITEMS_SEP);

            write(encodeKey(entry.getKey()));
            write(ENTRY_SEP);

            encode(entry.getKey(), entry.getValue(), level + 1);
            index++;
        }

        write(MAPS_END);
    }





    /**********************************************************************************************
    */

    private static interface Encoder {
        void encode(CopyOfJsonEncoder encoder, Object field, Object value, int level) throws ReflectiveOperationException, IOException;
    }


    private static class ReplaceEncoder implements Encoder {
        private final Method delegate;

        public ReplaceEncoder(Method delegate) {
            if (delegate == null)
                throw new IllegalArgumentException("Parameter can't be null");
            this.delegate = delegate;
        }

        @Override
        public void encode(CopyOfJsonEncoder encoder, Object field, Object value, int level) throws ReflectiveOperationException, IOException {
            encoder.encode(field, delegate.invoke(value), level);
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
        public void encode(CopyOfJsonEncoder encoder, Object field, Object value, int level) throws ReflectiveOperationException, IOException {
            int index = 0;

            encoder.writer.write(MAPS_BEG);

            for (Map.Entry<String, Property> e : properties.entrySet()) {
                if (index > 0)
                    encoder.writer.write(ITEMS_SEP);

                encoder.writer.write(encodeKey(e.getKey()));
                encoder.writer.write(ENTRY_SEP);

                encoder.encode(e.getKey(), e.getValue().get(value), level + 1);

                index++;
            }

            encoder.writer.write(MAPS_END);
        }

    }


    private static final Map<Class<?>, Encoder> Encoders = new ConcurrentHashMap<Class<?>, Encoder>();


    private static Encoder getEncoder(Class<?> clazz) throws ReflectiveOperationException {
        final Encoder cached = Encoders.get(clazz);

        if (cached != null)
            return cached;

        final Encoder result = getEncoder0(clazz);
        Encoders.put(clazz, result);

        return result;
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
            if (!Misc.isEmpty(targetName = anno.replaceWith())) {
                Method target = clazz.getDeclaredMethod(targetName);
                target.setAccessible(true);
                return new ReplaceEncoder(target);
            }

            // указаны поля и методы сериализации
            JsonProperty[] properties = anno.value();

            if (Misc.isEmpty(properties))
                throw new IllegalArgumentException(clazz + ": properties are not defined");

            Map<String, Property> result = new LinkedHashMap<String, Property>();

            for (JsonProperty prop : properties) {
                String name = prop.value();

                // указан метод для сериализации свойства
                if (!Misc.isEmpty(targetName = prop.method())) {
                    Method target = clazz.getDeclaredMethod(targetName);
                    target.setAccessible(true);

                    if (Misc.isEmpty(name))
                        name = targetName;

                    result.put(name, Property.create(target));
                    continue;
                }

                // указано поле для сериализации свойства
                if (!Misc.isEmpty(targetName = prop.field()) || !Misc.isEmpty(targetName = name)) {
                    Field target = clazz.getDeclaredField(targetName);
                    target.setAccessible(true);

                    if (Misc.isEmpty(name))
                        name = targetName;

                    result.put(name, Property.create(target));
                    continue;
                }

            }

            return new PropertyEncoder(result);
        }

        // если ничего не указано: сериализуем все не статичные поля объекта
        Map<String, Property> result = new HashMap<String, Property>();

        for (Map.Entry<String, Field> e : Reflect.getFields(origin).entrySet()) {
            Field field = e.getValue();

            if ((field.getModifiers() & Modifier.STATIC) != 0)
                continue;

            result.put(e.getKey(), Property.create(field));
        }

        return new PropertyEncoder(result);
    }




    /**
     * Вспомогательный класс стека элементов
     */
    private static class Stack {
        private final List<Object[]> stack;


        public Stack(int size) {
            stack = new ArrayList<Object[]>(size);
        }


        public void check(Object field, Object value, int level) throws JsonException {
            Object[] item = new Object[] { field, value };

            if (level < stack.size()) {
                stack.set(level, item);
            } else {
                stack.add(level, item);
            }

            for (int index = 0; index < level; index++) {
                if (stack.get(index)[1] == value) {
                    throw new JsonException(
                            "Recursion found: " + trace(index + 1) +
                            " refers to " + trace(level + 1)
                        );
                }
            }

        }

        public CharSequence trace(int level) {
            return trace(level, null);
        }

        public CharSequence trace(int level, String item) {
            StringBuilder result = new StringBuilder(128);

            int limit = level - 1,
                index = 0;

            if (limit > 0) {
                while (index <= limit) {
                    result.append(stack.get(index)[0]);

                    if (index != limit)
                        result.append('/');

                    index++;
                }
            } else {
                result.append('/');
            }

            if (item != null) {
                if (limit > 0)
                    result.append('/');
                result.append(item);
            }

            return result;
        }

    }


}
