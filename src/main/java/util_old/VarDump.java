package util_old;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.darkslave.util.Reflect;




public class VarDump {
    private static final String MARK_RECURSIVE = "/* RECURSIVE */";
    private static final String MARK_NO_ACCESS = "/* ACCESS ERROR */";
    private static final String MARK_NULL = "null";
    private static final String LEVEL_PAD = "    ";
    private static final String LEVEL_END = "\n";
    private static final String ITEMS_SEP = ",";
    private static final String ENTRY_SEP = " = ";
    private static final String CLASS_SEP = " ";
    private static final String LIST_BEG = "[";
    private static final String LIST_END = "]";
    private static final String MAPS_BEG = "{";
    private static final String MAPS_END = "}";

    private static final Map<Class<?>, Converter> converters = new ConcurrentHashMap<Class<?>, Converter>();



    /**
     * получить дамп указанной переменной
     *
     * @param value - объект переменной
     * @param gc    - печатать имя класса?
     * @return строку дампа
     */
    public static String dump(Object value) {
        return dump(value, false);
    }


    public static String dump(Object value, boolean gc) {
        return new VarDump(value, gc).toString();
    }



    /**
     * Установить конвертер содержимого переменной
     *
     * @param clazz - супертип
     * @param conv  - конвертер
     */
    public static interface Converter {
        public CharSequence convert(Object value);
    }


    public static void setConverter(Class<?> clazz, Converter conv) {
        converters.put(clazz, conv);
    }



    /**********************************************************************************************
    */
    private Map<Object, List<Object>> table;
    private StringBuilder content;
    private boolean get_class;


    private VarDump(Object value, boolean gc) {
        this.table   = new HashMap<Object, List<Object>>();
        this.content = new StringBuilder();
        this.get_class = gc;
        get(value, 0);
    }


    public String toString(){
        return content.toString();
    }


    private void append(Object value) {
        content.append(value);
    }


    private void padding(int level, boolean separate) {
        if(separate)
            append(ITEMS_SEP);

        append(LEVEL_END);

        while (--level >= 0)
            append(LEVEL_PAD);
    }


    private boolean recursion(Object value) {
        List<Object> items = table.get(value);

        if (items == null) {
            items = new ArrayList<Object>();
            table.put(value, items);
        }

        for (Object item : items)
            if (item == value)
                return true;

        items.add(value);

        return false;
    }


    private void get_class(Object value) {
        if (get_class) {
            append(value.getClass().getCanonicalName());
            append(CLASS_SEP);
        }
    }


    private void get(Object value, int level) {
        if (value == null) {
            append(MARK_NULL);
            return;
        }

        get_class(value);

        if (value instanceof java.lang.Boolean
                || value instanceof java.lang.Number
                || value instanceof java.lang.CharSequence
                || value instanceof java.lang.Character
                || value instanceof java.util.Date) {
            get_native(value);
            return;
        }

        if (!value.getClass().isEnum() && recursion(value)) {
            append(MARK_RECURSIVE);
            return;
        }

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

        for (Class<?> type : Reflect.getSuperTypes(value.getClass())) {
            Converter conv = converters.get(type);
            if (conv != null) {
                append(conv.convert(value));
                return;
            }
        }

        get_object(value, level);

    }


    private void get_native(Object value) {
        append(value);
    }


    private void get_array(Object value, int level) {
        int length = Array.getLength(value),
            index = 0;

        append(LIST_BEG);

        while (index < length) {
            padding(level + 1, index > 0);
            get(Array.get(value, index), level + 1);
            index++;
        }

        padding(level, false);
        append(LIST_END);
    }


    private void get_list(Object value, int level) {
        int index = 0;

        append(LIST_BEG);

        for (Object item : (Iterable<?>) value) {
            padding(level + 1, index > 0);
            get(item, level + 1);
            index++;
        }

        padding(level, false);
        append(LIST_END);
    }


    private void get_map(Object value, int level) {
        int index = 0;

        append(MAPS_BEG);

        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            padding(level + 1, index > 0);
            append(entry.getKey());
            append(ENTRY_SEP);

            get(entry.getValue(), level + 1);
            index++;
        }

        padding(level, false);
        append(MAPS_END);
    }


    private void get_object(Object value, int level) {
        Map<String, Field> fields = Reflect.getFields(value.getClass());
        int index = 0;

        append(MAPS_BEG);

        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            Field field = entry.getValue();

            if((field.getModifiers() & Modifier.STATIC) != 0)
                continue;

            padding(level + 1, index > 0);
            append(entry.getKey());
            append(ENTRY_SEP);

            try {
                field.setAccessible(true);
                get(field.get(value), level + 1);
            } catch (Exception e) {
                append(MARK_NO_ACCESS);
            }

            index++;
        }

        padding(level, false);
        append(MAPS_END);
    }


}
