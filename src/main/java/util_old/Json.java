package util_old;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.darkslave.util.Reflect;





public class Json {
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
     * @throws JsonException
     */
    public static String encode(Object value) throws JsonException {
        Json encoder = new Json();
        encoder.get(value, 0);
        return encoder.getContent().toString();
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



    /**
     * Установить конвертер содержимого переменной
     *
     * @param clazz - супертип
     * @param conv  - конвертер
     */
    public static void setConverter(Class<?> clazz, Converter conv) {
        ConvertHolder.set(clazz, conv);
    }



    /**********************************************************************************************
    */

    private static CharSequence encodeKey0(Object value) {
        StringBuilder result = new StringBuilder();

        if (value == null)
            return MARK_NULL;

        result.append(ENTRY_ESC);
        result.append(escape0(value.toString()));
        result.append(ENTRY_ESC);

        return result;
    }


    private static CharSequence escape0(CharSequence value) {
        StringBuilder result = new StringBuilder();

        if (value == null)
            return result;

        int length = value.length();

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
                        result.append(toHex(c, 2, "\\x"));
                    break;
                }
            } else if (c > 255) {
                result.append(toHex(c, 4, "\\u"));
            } else {
                result.append(c);
            }
        }

        return result;
    }


    private static CharSequence toHex(int value, int padd, String pref) {
        StringBuilder result = new StringBuilder(pref.length() + padd).append(pref);

        String temp = Integer.toHexString(value);
        padd-= temp.length();

        while (--padd >= 0)
            result.append("0");

        result.append(temp);

        return result;
    }




    /**********************************************************************************************
    */
    private final StringBuilder content;
    private final List<Object> stack;


    private Json() {
        content = new StringBuilder();
        stack   = new ArrayList<Object>();
    }


    private CharSequence getContent() {
        return content;
    }


    private void append(Object value) {
        content.append(value);
    }


    private void get(Object value, int level) throws JsonException {
        if (value == null) {
            append(MARK_NULL);
            return;
        }

        if (value instanceof java.lang.Boolean
                || value instanceof java.lang.Number) {
            get_native(value);
            return;
        }

        if (value instanceof java.lang.CharSequence
                || value instanceof java.lang.Character) {
            get_string(value);
            return;
        }

        if (value instanceof java.util.Date) {
            get_date(value);
            return;
        }

        check_recursion(value, level);

        if (value.getClass().isArray()) {
            get_array(value, level);
            return;
        }

        if (value instanceof java.lang.Iterable) {
            get_list(value, level);
            return;
        }

        if (value instanceof java.util.Map) {
            get_map(value, level);
            return;
        }

        if (throw_json_method(value))
            return;

        if (throw_converter(value))
            return;

        get_object(value, level);
    }


    private void check_recursion(Object value, int level) throws JsonException {
        for (int i = stack.size(); i <= level; i++)
            stack.add(null);

        stack.set(level, value);

        for (int i = 0; i < level; i++) {
            if (stack.get(i) == value)
                throw new JsonException(value.getClass().getName() + " recursion link error");
        }

    }


    private void get_native(Object value) {
        append(value);
    }


    private void get_string(Object value) {
        append(ENTRY_ESC);
        append(escape0(value.toString()));
        append(ENTRY_ESC);
    }


    private void get_date(Object value) {
        append(((Date) value).getTime());
    }


    private void get_array(Object value, int level) throws JsonException {
        int length = Array.getLength(value), index = 0;

        append(LIST_BEG);

        while (index < length) {
            if (index > 0)
                append(ITEMS_SEP);
            get(Array.get(value, index), level + 1);
            index++;
        }

        append(LIST_END);
    }


    private void get_list(Object value, int level) throws JsonException {
        int index = 0;

        append(LIST_BEG);

        for (Object item : (Iterable<?>) value) {
            if (index > 0)
                append(ITEMS_SEP);
            get(item, level + 1);
            index++;
        }

        append(LIST_END);
    }


    private void get_map(Object value, int level) throws JsonException {
        int index = 0;

        append(MAPS_BEG);

        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            if (index > 0)
                append(ITEMS_SEP);

            append(encodeKey0(entry.getKey()));
            append(ENTRY_SEP);

            get(entry.getValue(), level + 1);
            index++;
        }

        append(MAPS_END);
    }


    private void get_object(Object value, int level) throws JsonException {
        Map<String, Field> fields = Reflect.getFields(value.getClass());
        int index = 0;

        append(MAPS_BEG);

        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            Field field = entry.getValue();

            if ((field.getModifiers() & Modifier.STATIC) != 0)
                continue;

            if (index > 0)
                append(ITEMS_SEP);

            append(encodeKey0(entry.getKey()));
            append(ENTRY_SEP);

            try {
                field.setAccessible(true);
                get(field.get(value), level + 1);
            } catch (Exception e) {
                throw new JsonException(value.getClass().getName() + "#" + field.getName() + " access field error", e);
            }

            index++;
        }

        append(MAPS_END);
    }


    private boolean throw_json_method(Object value) throws JsonException {
        JsonMethod holder = JsonMethod.get(value.getClass());

        if (!holder.hasMethod())
            return false;

        try {
            append(holder.getMethod().invoke(value));
        } catch (Exception e) {
            throw new JsonException(value.getClass().getName() + " json method error", e);
        }

        return true;
    }


    private boolean throw_converter(Object value) throws JsonException {
        ConvertHolder holder = ConvertHolder.get(value.getClass());

        if (!holder.hasConverter())
            return false;

        try {
            append(holder.getConverter().convert(value));
        } catch (Exception e) {
            throw new JsonException(value.getClass().getName() + " converter error", e);
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

        private static final Map<Class<?>, JsonMethod> cache = new ConcurrentHashMap<Class<?>, JsonMethod>();
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
     *  Интерфейс конвертера json
     */
    public static interface Converter {
        public CharSequence convert(Object value);
    }


    private static class ConvertHolder {
        private static final Map<Class<?>, ConvertHolder> cache = new ConcurrentHashMap<Class<?>, ConvertHolder>();
        private static final ConvertHolder UNDEFINED = new ConvertHolder(null, true);

        private final Converter converter;
        private final boolean  inherited;


        private ConvertHolder(Converter c, boolean i) {
            this.converter = c;
            this.inherited = i;
        }


        public Converter getConverter() {
            return converter;
        }


        public boolean hasConverter() {
            return converter != null;
        }


        public boolean isInherited() {
            return inherited;
        }


        public static ConvertHolder get(Class<?> clazz) {
            ConvertHolder holder = cache.get(clazz);

            if (holder == null) {
                holder = get0(clazz);
                cache.put(clazz, holder);
            }

            return holder;
        }


        private static ConvertHolder get0(Class<?> clazz) {
            for (Class<?> type : Reflect.getSuperTypes(clazz)) {
                ConvertHolder conv = cache.get(type);
                if (conv != null && !conv.isInherited())
                    return new ConvertHolder(conv.getConverter(), true);
            }

            return UNDEFINED;
        }


        public static void set(Class<?> clazz, Converter conv) {
            cache.put(clazz, new ConvertHolder(conv, false));

            Iterator<Map.Entry<Class<?>, ConvertHolder>> i = cache.entrySet().iterator();

            while (i.hasNext()) {
                Map.Entry<Class<?>, ConvertHolder> e = i.next();
                if (clazz != e.getKey() && clazz.isAssignableFrom(e.getKey()) && e.getValue().isInherited())
                    i.remove();
            }

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

    }



}
