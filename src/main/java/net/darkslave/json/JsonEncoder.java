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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.darkslave.io.CharArrayWriter;
import net.darkslave.reflect.Property;
import net.darkslave.reflect.Reflect;
import net.darkslave.util.Misc;





public class JsonEncoder {

    /**
     * Сериализация объекта в json формат
     *
     * @param source - исходный объект
     * @return сериализованную строку
     * @throws IOException
     */
    public static String encode(Object source) throws IOException {
        try (CharArrayWriter writer = new CharArrayWriter(1024)) {
            encode(source, writer);
            return writer.toString();
        }
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
            char code = temp.charAt(i);

            if (code < 128) {
                String replace = CHAR_REPLACES[code];
                if (replace != null) {
                    result.append(replace);
                    continue;
                }
            }

            result.append(code);
        }

        return result.toString();
    }


    private static final char[] HEX_CHARS = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };


    /**
     * экранирование символов
     */
    private static String safe(int value) {
        char[] temp = new char[6];
        temp[0] = '\\';
        temp[1] = 'u';
        temp[2] = HEX_CHARS[(value >>> 12) & 15];
        temp[3] = HEX_CHARS[(value >>>  8) & 15];
        temp[4] = HEX_CHARS[(value >>>  4) & 15];
        temp[5] = HEX_CHARS[(value       ) & 15];
        return new String(temp);
    }


    /**
     * кэш экранированных символов
     */
    private static final String[] CHAR_REPLACES = new String[128];

    static {
        for (int value = 0; value < 32; value++)
            CHAR_REPLACES[value] = safe(value);

        CHAR_REPLACES['\"'] = "\\\"";
        CHAR_REPLACES['\\'] = "\\\\";
        CHAR_REPLACES['\t'] = "\\t";
        CHAR_REPLACES['\b'] = "\\b";
        CHAR_REPLACES['\n'] = "\\n";
        CHAR_REPLACES['\r'] = "\\r";
        CHAR_REPLACES['\f'] = "\\f";

    }



    /**********************************************************************************************
    */
    private final List<Object> dejavu;
    private final Writer writer;


    private JsonEncoder(Writer w) {
        dejavu = new ArrayList<Object>(32);
        writer = w;
    }


    private void dejavu(Object value, int level) throws JsonException {
        if (level < dejavu.size()) {
            dejavu.set(level, value);
        } else {
            dejavu.add(level, value);
        }

        for (int index = 0; index < level; index++)
            if (dejavu.get(index) == value)
                throw new JsonException("Circular reference in " + value.getClass());

    }


    private void writeEscaped(Object value) throws IOException {
        write(ENTRY_ESC);
        write(escape(value));
        write(ENTRY_ESC);
    }


    private void write(String value) throws IOException {
        writer.write(value);
    }


    private void write(Object value) throws IOException {
        writer.write(value.toString());
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
            writeEscaped(value);
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

        // исключения
        if (value instanceof Throwable) {
            writeEscaped(Misc.getErrorTrace((Throwable) value));
            return;
        }

        // проверка рекурсии
        dejavu(value, level);

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

            writeEscaped(entry.getKey());
            write(ENTRY_SEP);

            encode(entry.getValue(), level + 1);
            index++;
        }

        write(MAPS_END);
    }


    private void encodeReplace(Property delegate, Object value, int level) throws IOException {
        Object result;

        try {
            result = delegate.get(value);
        } catch (ReflectiveOperationException re) {
            throw new JsonException("Encode " + value.getClass() + " error", re);
        }

        encode(result, level);
    }


    private void encodeProperty(Iterable<Property> properties, Object value, int level) throws IOException {
        int index = 0;

        write(MAPS_BEG);

        for (Property prop : properties) {
            if (index > 0)
                write(ITEMS_SEP);

            writeEscaped(prop.getName());
            write(ENTRY_SEP);

            Object result;

            try {
                result = prop.get(value);
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
        private final Property delegate;

        public ReplaceEncoder(Property delegate) {
            this.delegate = Objects.requireNonNull(delegate, "Parameter can't be null");
        }

        @Override
        public void encode(JsonEncoder encoder, Object value, int level) throws IOException {
            encoder.encodeReplace(delegate, value, level);
        }

    }


    private static class PropertyEncoder implements Encoder {
        private final Iterable<Property> properties;

        public PropertyEncoder(Iterable<Property> properties) {
            this.properties = Objects.requireNonNull(properties, "Parameter can't be null");
        }

        @Override
        public void encode(JsonEncoder encoder, Object value, int level) throws IOException {
            encoder.encodeProperty(properties, value, level);
        }

    }


    private static final Map<Class<?>, Encoder> Encoders = new ConcurrentHashMap<Class<?>, Encoder>();


    private static Encoder getEncoder(Class<?> clazz) throws JsonException {
        final Encoder cached = Encoders.get(clazz);
        final Encoder result;

        if (cached != null)
            return cached;

        try {
            result = createEncoder(clazz);
        } catch (ReflectiveOperationException e) {
            throw new JsonException("Create " + clazz + " encoder error", e);
        }

        Encoders.put(clazz, result);
        return result;
    }


    private static Encoder createEncoder(Class<?> clazz) throws ReflectiveOperationException {
        JsonSerialize annotate = clazz.getAnnotation(JsonSerialize.class);
        String targetName;


        // если не указана аннотация сериализации
        if (annotate == null) {
            // для енумов используем метод замены объекта name()
            if (clazz.isEnum())
                return createReplaceEncoder(clazz, "name");

            // для остальных классов испольузем сериализацию полей
            return createDefaultEncoder(clazz);
        }


        // если в аннотации указан метод замены объекта
        if (!isEmpty(targetName = annotate.replaceWith()))
            return createReplaceEncoder(clazz, targetName);


        JsonProperty[] property = annotate.value();

        if (isEmpty(property))
            throw new ReflectiveOperationException("JsonProperty list is not defined");

        // если указаны поля и методы сериализации
        Collection<Property> result = new ArrayList<Property>(property.length);

        for (JsonProperty prop : property) {
            result.add(createProperty(clazz, prop.value(), prop.field(), prop.method()));
        }


        return new PropertyEncoder(result);
    }


    private static Encoder createDefaultEncoder(Class<?> clazz) throws ReflectiveOperationException {
        Map<String, Field>   fields = Reflect.getFields(clazz);
        Collection<Property> result = new ArrayList<Property>(fields.size());

        for (Map.Entry<String, Field> e : fields.entrySet()) {
            Field field = e.getValue();

            // игнорируем статик поля
            if ((field.getModifiers() & Modifier.STATIC) != 0)
                continue;

            // игнорируем транзиент поля
            if ((field.getModifiers() & Modifier.TRANSIENT) != 0)
                continue;

            // игнорируем аннотированные поля
            JsonIgnore ignore = field.getAnnotation(JsonIgnore.class);
            if (ignore != null)
                continue;

            result.add(Property.create(e.getKey(), field));
        }

        return new PropertyEncoder(result);
    }


    private static Encoder createReplaceEncoder(Class<?> clazz, String name) throws ReflectiveOperationException {
        Method target = Reflect.getMethod(clazz, name);

        if (target == null)
            throw new NoSuchMethodException(name + " in " + clazz);

        return new ReplaceEncoder(Property.create(name, target));
    }


    private static Property createProperty(Class<?> clazz, String outputName, String fieldName, String methodName) throws ReflectiveOperationException {
        String name;

        // указан метод для сериализации свойства
        if (!isEmpty(name = methodName)) {
            Method target = Reflect.getMethod(clazz, name);

            if (target == null)
                throw new NoSuchMethodException(name + " in " + clazz);

            if (isEmpty(outputName))
                outputName = name;

            return Property.create(outputName, target);
        }


        // указано поле для сериализации свойства
        if (!isEmpty(name = fieldName) || !isEmpty(name = outputName)) {
            Field target = Reflect.getField(clazz, name);

            if (target == null)
                throw new NoSuchFieldException(name + " in " + clazz);

            if (isEmpty(outputName))
                outputName = name;

            return Property.create(outputName, target);
        }


        throw new IllegalArgumentException("Neither Field nor Method name are defined");
    }


    private static boolean isEmpty(CharSequence source) {
        return source == null || source.length() == 0;
    }


    private static boolean isEmpty(Object[] source) {
        return source == null || source.length == 0;
    }


}
