/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.darkslave.io.CharArrayWriter;
import net.darkslave.reflect.Property;
import net.darkslave.reflect.Reflect;
import net.darkslave.util.Misc;




/**
 * Класс сериализатор объектов в json-формат
 */
public class JsonEncoder {
    public static final String EMPTY_STRING = "";
    public static final String MARK_NULL = "null";
    public static final String ITEMS_SEP = ", ";
    public static final String ENTRY_SEP = ": ";
    public static final String ENTRY_ESC = "\"";
    public static final String LIST_BEG  = "[";
    public static final String LIST_END  = "]";
    public static final String MAPS_BEG  = "{";
    public static final String MAPS_END  = "}";



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



    /**
     * Сериализация объекта в json формат
     *
     * @param source - исходный объект
     * @param writer - поток записи
     * @throws IOException
     */
    public static void encode(Object source, Writer writer) throws IOException {
        JsonEncoder encoder = new JsonEncoder(writer);
        encoder.encode(source, 0);
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

        StringBuilder result = new StringBuilder(length + (length >>> 2) + 16);

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
    private static final String[] CHAR_REPLACES;

    static {
        CHAR_REPLACES = new String[128];

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


    /**
     * Добавление объекта сериализации в стек вызова и проверка на циклические ссылки
     *
     * @param value - объект
     * @param level - уровень вложенности
     */
    public void dejavu(Object value, int level) throws JsonException {
        if (level < dejavu.size()) {
            dejavu.set(level, value);
        } else {
            dejavu.add(level, value);
        }

        for (int index = 0; index < level; index++)
            if (dejavu.get(index) == value)
                throw new JsonException("Circular reference in " + value.getClass());

    }


    /**
     * Сериализаия текущего объекта
     *
     * @param value - объект
     * @param level - уровень вложенности
     */
    public void encode(Object value, int level) throws IOException {
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
            writeString(value);
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
            writeString(Misc.getErrorTrace((Throwable) value));
            return;
        }

        // проверка рекурсии
        dejavu(value, level);

        Class<?> targetClass = value.getClass();

        // массив
        if (targetClass.isArray()) {
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
        ObjectEncoder encoder = getEncoder(targetClass);
        encoder.encode(this, value, level);
    }


    /**
     * Запись массива
     *
     * @param value - объект
     * @param level - уровень вложенности
     */
    public void writeArray(Object value, int level) throws IOException {
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


    /**
     * Запись Iterable объекта
     *
     * @param value - объект
     * @param level - уровень вложенности
     */
    public void writeIterable(Object value, int level) throws IOException {
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


    /**
     * Запись Map объекта
     *
     * @param value - объект
     * @param level - уровень вложенности
     */
    public void writeMap(Object value, int level) throws IOException {
        int index = 0;

        write(MAPS_BEG);

        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            if (index > 0)
                write(ITEMS_SEP);

            writeString(entry.getKey());
            write(ENTRY_SEP);

            encode(entry.getValue(), level + 1);
            index++;
        }

        write(MAPS_END);
    }


    /**
     * Запись строкового значения
     *
     * @param value - объект
     */
    public void writeString(Object value) throws IOException {
        write(ENTRY_ESC);
        write(escape(value));
        write(ENTRY_ESC);
    }


    /**
     * Запись строки
     */
    public void write(String value) throws IOException {
        writer.write(value);
    }


    /**
     * Запись объекта
     */
    public void write(Object value) throws IOException {
        writer.write(value.toString());
    }



    /**********************************************************************************************
    */
    private static final Map<Class<?>, ObjectEncoder> Encoders = new ConcurrentHashMap<Class<?>, ObjectEncoder>();



    /**
     * Установить кастомный сериализатор класса
     *
     * @param targetClass - целевой класс
     * @param encoder - сериализатор
     */
    public static void setEncoder(Class<?> targetClass, ObjectEncoder encoder) {
        Encoders.put(targetClass, encoder);
    }



    /**
     * Получить сериализатор класса
     *
     * @param targetClass - целевой класс
     * @return сериализатор
     * @throws JsonException
     */
    public static ObjectEncoder getEncoder(Class<?> targetClass) throws JsonException {
        final ObjectEncoder cached = Encoders.get(targetClass);
        final ObjectEncoder result;

        if (cached != null)
            return cached;

        try {
            result = createByAnnotation(targetClass);
        } catch (ReflectiveOperationException e) {
            throw new JsonException("Create " + targetClass + " encoder error", e);
        }

        Encoders.put(targetClass, result);
        return result;
    }



    /**
     * Создание сериализатора по аннотации к классу
     */
    private static ObjectEncoder createByAnnotation(Class<?> targetClass) throws ReflectiveOperationException {
        JsonSerialize annotate = targetClass.getAnnotation(JsonSerialize.class);

        // если не указана аннотация сериализации
        if (annotate == null) {
            // для енумов используем метод замены объекта name()
            if (targetClass.isEnum())
                return ReplaceEncoder.create(targetClass, "name");

            // для остальных классов испольузем сериализацию полей
            return createDefaultEncoder(targetClass);
        }


        // если в аннотации указан метод замены объекта
        String methodName = annotate.replaceWith();

        if (!Misc.isEmpty(methodName))
            return ReplaceEncoder.create(targetClass, methodName);


        // если указан список свойств для сериализации
        JsonProperty[] properties = annotate.value();

        if (Misc.isEmpty(properties))
            throw new ReflectiveOperationException("JsonProperty list is empty");

        return PropertyEncoder.create(targetClass, properties);
    }



    /**
     * Создание сериализатора по умолчанию (по полям класса)
     */
    private static ObjectEncoder createDefaultEncoder(Class<?> targetClass) throws ReflectiveOperationException {
        Map<String, Field>   fields = Reflect.getFields(targetClass);
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

            result.add(Property.from(e.getKey(), field));
        }

        return new PropertyEncoder(result);
    }


}
