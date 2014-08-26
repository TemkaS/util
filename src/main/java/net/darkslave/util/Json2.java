/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class Json2 {

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
        new Json2(writer).encode(MARK_ROOT, source, 0);
    }



    /**********************************************************************************************
    */

    /**
     *  Ошибка json сериализации
     */
    public static class JsonException extends RuntimeException {
        private static final long serialVersionUID = 2051156110978766383L;


        public JsonException(String message, Throwable cause) {
            super(message, cause);
        }


        public JsonException(String message) {
            super(message);
        }


        public JsonException(Throwable cause) {
            super(cause);
        }

    }


    /**
     *  Интерфейс класса-конвертера для замены объекта сериализации
     */
    public static interface JsonReplacer {
        Object replaceWith(Object source);
    }


    /**
     * Интерфейс класса с заменой объекта сериализации
     */
    public static interface JsonReplaceable {
        Object replaceJson();
    }


    /**
     * Маркер для игнорирования сериализации поля
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface JsonIgnore {
    }


    /**
     * Пометка для указания имени свойства при сериализации поля или метода
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.METHOD })
    public static @interface JsonProperty {
        String value();
    }


    /**
     * Элемент стека сериализации
     */
    private static class StackElement {
        private Object field;
        private Object value;


        public StackElement(Object field, Object value) {
            this.field = field;
            this.value = value;
        }


        public Object getField() {
            return field;
        }


        public Object getValue() {
            return value;
        }


        public void set(Object field, Object value) {
            this.field = field;
            this.value = value;
        }


        public static String fieldsPath(List<StackElement> source, int from, int till) {
            StringBuilder result = new StringBuilder(128);

            for (int index = from; index < till; index++) {
                if (index > from)
                    result.append("/");
                result.append(source.get(index).getField());
            }

            return result.toString();
        }

    }



    /**********************************************************************************************
    */
    private static final String EMPTY_STRING = "";

    private static final String MARK_ROOT = "#root";
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
    private final List<StackElement> stack;
    private final Writer writer;


    private Json2(Writer w) {
        stack  = new ArrayList<StackElement>(32);
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

        if (value instanceof JsonReplaceable) {
            JsonReplaceable target = (JsonReplaceable) value;
            encode(field, target.replaceJson(), level);
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

        checkRecursion(field, value, level);

        if (value.getClass().isArray()) {
            writeArray(value, level);
            return;
        }

        if (value instanceof Iterable) {
            writeList(value, level);
            return;
        }

        if (value instanceof Map) {
            writeMap(value, level);
            return;
        }

        writeObject(value, level);
    }


    private void checkRecursion(Object field, Object value, int level) throws JsonException {
        if (level < stack.size()) {
            stack.get(level).set(field, value);
        } else {
            stack.add(level, new StackElement(field, value));
        }

        for (int index = 0; index < level; index++) {
            if (stack.get(index).getValue() == value) {
                throw new JsonException(
                                "Recursion found: " +
                                StackElement.fieldsPath(stack, 0, index + 1) +
                                " refers to subelement " +
                                StackElement.fieldsPath(stack, index + 1, level + 1)
                            );
            }
        }

    }


    private void writeArray(Object value, int level) throws JsonException, IOException {
        int length = Array.getLength(value);
        int index  = 0;

        write(LIST_BEG);

        while (index < length) {
            if (index > 0)
                write(ITEMS_SEP);

            encode(index, Array.get(value, index), level + 1);
            index++;
        }

        write(LIST_END);
    }


    private void writeList(Object value, int level) throws JsonException, IOException {
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


    private void writeObject(Object value, int level) throws JsonException, IOException {
        Map<String, Field> fields = Reflect.getFields(value.getClass());
        int index = 0;

        write(MAPS_BEG);

        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            Field field = entry.getValue();

            if ((field.getModifiers() & Modifier.STATIC) != 0)
                continue;

            if (index > 0)
                write(ITEMS_SEP);

            write(encodeKey(entry.getKey()));
            write(ENTRY_SEP);

            try {
                field.setAccessible(true);
                encode(field.getName(), field.get(value), level + 1);
            } catch (JsonException | IOException e) {
                throw e;
            } catch (Exception e) {
                throw new JsonException(value.getClass().getName() + "#" + field.getName() + " access field error", e);
            }

            index++;
        }

        write(MAPS_END);
    }





    public static void main(String args[]) throws JsonException, IOException {
        Map<Object, Object> m = new HashMap<>();
        List<Object> l = new ArrayList<>();
        l.add(0, 123);
        l.add(1, m);
        m.put("sub", l);

        System.out.println(encode(m));
    }
}
