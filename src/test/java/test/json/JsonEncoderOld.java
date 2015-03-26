/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package test.json;


import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.darkslave.reflect.Reflect;





public class JsonEncoderOld {
    private static final String EMPTY_STRING = "";
    private static final String MARK_NULL = "null";
    private static final String ITEMS_SEP = ", ";
    private static final String ENTRY_SEP = ": ";
    private static final String ENTRY_ESC = "\"";
    private static final String LIST_BEG = "[";
    private static final String LIST_END = "]";
    private static final String MAPS_BEG = "{";
    private static final String MAPS_END = "}";


    /**
     * Преобразование объекта в json-формат,
     * – числа выдаются "как есть",
     * – строки экранируются и выделяются кавычками,
     * – коллекции преобразуются в js-массивы и объекты,
     * – объекты преобразуются в js-объекты
     *
     * преобразование объектов в js-представление происходит
     *   либо через метод класса toJson,
     *   либо через предопределенный конвертер,
     *   либо через отражение по полям объекта
     *
     * @param value - объект
     * @return строку в json-формате
     */
    public static String encode(Object value) throws JsonException, IOException {
        StringWriter writer = new StringWriter();
        JsonEncoderOld encoder = new JsonEncoderOld(writer);
        encoder.encode(value, 0);
        return writer.toString();
    }


    public static void encode(Object value, Writer writer) throws JsonException, IOException {
        JsonEncoderOld encoder = new JsonEncoderOld(writer);
        encoder.encode(value, 0);
    }



    /**
     * Преобразование объекта в json-формат для использования в именах свойств
     *
     * @param value - объект
     * @return строку в json-формате
     */
    public static String encodeKey(Object value) {
        return encodeKey0(value).toString();
    }



    /**
     * Экранирование js спец. символов
     *
     * @param value - исходная строка
     * @return экранированную строку
     */
    public static String escape(CharSequence value) {
        return escape0(value).toString();
    }



    /**********************************************************************************************
    */

    private static CharSequence encodeKey0(Object value) {
        if (value == null)
            return MARK_NULL;

        CharSequence temp = escape0(value.toString());
        int size = (ENTRY_ESC.length() << 1) + temp.length();

        StringBuilder result = new StringBuilder(size);
        result.append(ENTRY_ESC);
        result.append(temp);
        result.append(ENTRY_ESC);

        return result;
    }


    private static CharSequence escape0(CharSequence value) {
        if (value == null)
            return EMPTY_STRING;

        int length = value.length();
        StringBuilder result = new StringBuilder(length + (length >>> 1));

        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c < 32 || c == 34 || c == 92) {
                switch (c) {
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
                        printHex(result, c, 2, "\\x");
                    break;
                }
            } else if (c > 255) {
                printHex(result, c, 4, "\\u");
            } else {
                result.append(c);
            }
        }

        return result;
    }


    private static void printHex(StringBuilder result, int value, int padd, String pref) {
        result.append(pref);

        String temp = Integer.toHexString(value);

        padd-= temp.length();

        while (--padd >= 0)
            result.append("0");

        result.append(temp);
    }




    /**********************************************************************************************
    */
    private final List<Object> stack;
    private final Writer writer;


    private JsonEncoderOld(Writer w) {
        stack  = new ArrayList<>();
        writer = w;
    }


    private void encode(Object value, int level) throws JsonException, IOException {
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
            write_string(value);
            return;
        }

        if (value instanceof Date) {
            write_date(value);
            return;
        }

        check_recursion(value, level);

        if (value.getClass().isArray()) {
            write_array(value, level);
            return;
        }

        if (value instanceof Iterable) {
            write_list(value, level);
            return;
        }

        if (value instanceof Map) {
            write_map(value, level);
            return;
        }

        if (throw_json_method(value))
            return;

        write_object(value, level);
    }


    private void check_recursion(Object value, int level) throws JsonException {
        for (int i = stack.size(); i <= level; i++)
            stack.add(null);

        stack.set(level, value);

        for (int i = 0; i < level; i++)
            if (stack.get(i) == value)
                throw new JsonException("Recursion found for field type " + value.getClass().getName());

    }


    private void write(String value) throws JsonException, IOException {
        writer.write(value);
    }


    private void write(Object value) throws JsonException, IOException {
        writer.write(value.toString());
    }


    private void write_string(Object value) throws JsonException, IOException {
        write(ENTRY_ESC);
        write(escape0(value.toString()));
        write(ENTRY_ESC);
    }


    private void write_date(Object value) throws JsonException, IOException {
        write(((Date) value).getTime());
    }


    private void write_array(Object value, int level) throws JsonException, IOException {
        int length = Array.getLength(value), index = 0;

        write(LIST_BEG);

        while (index < length) {
            if (index > 0)
                write(ITEMS_SEP);
            encode(Array.get(value, index), level + 1);
            index++;
        }

        write(LIST_END);
    }


    private void write_list(Object value, int level) throws JsonException, IOException {
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


    private void write_map(Object value, int level) throws JsonException, IOException {
        int index = 0;

        write(MAPS_BEG);

        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            if (index > 0)
                write(ITEMS_SEP);

            write(encodeKey0(entry.getKey()));
            write(ENTRY_SEP);

            encode(entry.getValue(), level + 1);
            index++;
        }

        write(MAPS_END);
    }


    private void write_object(Object value, int level) throws JsonException, IOException {
        Map<String, Field> fields = Reflect.getFields(value.getClass());
        int index = 0;

        write(MAPS_BEG);

        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            Field field = entry.getValue();

            if ((field.getModifiers() & Modifier.STATIC) != 0)
                continue;

            if (index > 0)
                write(ITEMS_SEP);

            write(encodeKey0(entry.getKey()));
            write(ENTRY_SEP);

            try {
                field.setAccessible(true);
                encode(field.get(value), level + 1);
            } catch (JsonException | IOException e) {
                throw e;
            } catch (Exception e) {
                throw new JsonException(value.getClass().getName() + "#" + field.getName() + " access field error", e);
            }

            index++;
        }

        write(MAPS_END);
    }


    private boolean throw_json_method(Object value) throws JsonException, IOException {
        JsonMethod holder = JsonMethod.get(value.getClass());

        if (!holder.hasMethod())
            return false;

        try {
            write(holder.getMethod().invoke(value));
        } catch (JsonException | IOException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonException(value.getClass().getName() + " json method error", e);
        }

        return true;
    }





    /**********************************************************************************************
    */
    /**
     *  Обертка json метода
     */
    private static class JsonMethod {
        private static final String JSON_METHOD_NAME = "toJson";

        private static final Map<Class<?>, JsonMethod> cache = new ConcurrentHashMap<>();
        private static final JsonMethod UNDEFINED = new JsonMethod(null);

        private final Method method;


        private JsonMethod(Method m) {
            this.method = m;
        }


        public Method getMethod() {
            return method;
        }


        public boolean hasMethod() {
            return method != null;
        }


        public static JsonMethod get(Class<?> clazz) {
            JsonMethod holder = cache.get(clazz);

            if (holder == null) {
                holder = get0(clazz, JSON_METHOD_NAME);
                cache.put(clazz, holder);
            }

            return holder;
        }


        private static JsonMethod get0(Class<?> clazz, String name) {
            Method method = Reflect.getMethod(clazz, name);

            if (method == null)
                return UNDEFINED;

            if ((method.getModifiers() & Modifier.STATIC) != 0)
                return UNDEFINED;

            if (!CharSequence.class.isAssignableFrom(method.getReturnType()))
                return UNDEFINED;

             return new JsonMethod(method);
        }

    }





    /**
     *  Ошибка json сериализации
     */
    public static class JsonException extends Exception {
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

}
