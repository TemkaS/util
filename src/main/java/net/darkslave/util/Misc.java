/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.darkslave.vars.Entry;
import net.darkslave.vars.Getter;





public class Misc {
    public static final String   EMPTY_STRING = "";
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String  SPACE_CHARS = "\\u0000-\\u0020\\u00A0\\u1680\\u180E\\u2000-\\u200B\\u202F\\u205F\\u3000\\uFEFF";
    private static final Pattern TRIM_SPACES = Pattern.compile("^[" + SPACE_CHARS + "]+|[" + SPACE_CHARS + "]+$");

    private static final String  MARKER_BEGIN = "{{";
    private static final String  MARKER_CLOSE = "}}";
    private static final int     MARKER_BEGIN_LENGTH = MARKER_BEGIN.length();
    private static final int     MARKER_CLOSE_LENGTH = MARKER_CLOSE.length();


    /**
     * Интерфейс разборщика шаблона на основе хэш-таблицы
     */
    public static class MapResolver implements Resolver {
        private final Map<?, ?> delegate;

        public MapResolver(Map<?, ?> delegate) {
            if (delegate == null)
                throw new IllegalArgumentException("Parameter can't be null");
            this.delegate = delegate;
        }

        @Override
        public Object get(String key) {
            return delegate.get(key);
        }

    }


    /**
     * Интерфейс разборщика шаблона
     */
    public static interface Resolver {
        Object get(String key);
    }


    /**
     * Получить строку по шаблону
     *
     * @param source - шаблон
     * @param params - параметры
     * @return строка
     */
    public static String template(String source, Map<String, ?> params) {
        return template(source, new MapResolver(params));
    }


    /**
     * Получить строку по шаблону
     *
     * @param source   - шаблон
     * @param resolver - разборщик шаблона
     * @return строка
     */
    public static String template(String source, Resolver resolver) {
        StringBuilder result = new StringBuilder(source.length());
        int index = 0;

        while (true) {
            int begin = source.indexOf(MARKER_BEGIN, index);
            int close = source.indexOf(MARKER_CLOSE, begin);

            if (begin < 0 || close < 0) {
                result.append(source.substring(index));
                break;
            }

            String key = source.substring(begin + MARKER_BEGIN_LENGTH, close);
            Object val = resolver.get(key);

            if (val != null) {
                result.append(source.substring(index, begin));
                result.append(val);
            } else {
                result.append(source.substring(index, close + MARKER_CLOSE_LENGTH));
            }

            index = close + MARKER_CLOSE_LENGTH;
        }

        return result.toString();
    }




    /**
     * Получить строковое представление или пустую строку, если null
     *
     * @param source - исходный объект
     * @return строка
     */
    public static String toString(Object source) {
        return source != null ? source.toString() : EMPTY_STRING;
    }


    /**
     * Получить строковое представление объекта или значение по умолчанию, если объект null
     *
     * @param source - исходный объект
     * @param value  - значение по умолчанию
     * @return строку
     */
    public static String toString(Object source, String value) {
        return source != null ? source.toString() : value;
    }


    /**
     * Объект-конвертер в Integer
     */
    public static final Getter<Number, Integer> GET_INTEGER = new Getter<Number, Integer>() {
        @Override
        public Integer get(Number source) {
            return toInteger(source, null);
        }
    };


    /**
     * Объект-конвертер в Long
     */
    public static final Getter<Number, Long> GET_LONG = new Getter<Number, Long>() {
        @Override
        public Long get(Number source) {
            return toLong(source, null);
        }
    };


    /**
     * Объект-конвертер в Double
     */
    public static final Getter<Number, Double> GET_DOUBLE = new Getter<Number, Double>() {
        @Override
        public Double get(Number source) {
            return toDouble(source, null);
        }
    };


    /**
     * Преобразовать в Integer
     *
     * @param source - исходный объект Number
     * @param value  - значение по умолчанию
     * @return Integer
     */
    public static Integer toInteger(Number source, Integer value) {
        if (source == null)
            return value;

        if (source instanceof Integer)
            return (Integer) source;

        return Integer.valueOf(source.intValue());
    }


    /**
     * Преобразовать в Long
     *
     * @param source - исходный объект Number
     * @param value  - значение по умолчанию
     * @return Long
     */
    public static Long toLong(Number source, Long value) {
        if (source == null)
            return value;

        if (source instanceof Long)
            return (Long) source;

        return Long.valueOf(source.longValue());
    }


    /**
     * Преобразовать в Double
     *
     * @param source - исходный объект Number
     * @param value  - значение по умолчанию
     * @return Double
     */
    public static Double toDouble(Number source, Double value) {
        if (source == null)
            return value;

        if (source instanceof Double)
            return (Double) source;

        return new Double(source.doubleValue());
    }




    /**
     * Удалить все пробельные символы из начала и конца строки
     *
     * @param source - исходная строка
     * @return строка
     */
    public static String trim(CharSequence source) {
        return TRIM_SPACES.matcher(source).replaceAll(EMPTY_STRING);
    }


    /**
     * Получить часть строки
     * если начальная и конечная позиции меньше нуля, отсчет ведется с конца строки
     *
     * @param source - исходная строка
     * @param start  - начальная позиция
     * @param end    - конечная позиция
     * @return част строки
     */
    public static String substring(CharSequence source, int start, int end) {
        int length = source.length();

        if (start >= length) {
            return EMPTY_STRING;

        } else
        if (start < 0) {
            start+= length;
            if (start < 0)
                start = 0;
        }

        if (end >= length) {
            end = length;

        } else
        if (end <= 0) {
            end+= length;
            if (end <= start)
                return EMPTY_STRING;
        }

        return source.subSequence(start, end).toString();
    }


    /**
     * Продублировать строку указанное кол-во раз
     *
     * @param source - шаблон строки
     * @param count  - кол-во повторений
     * @return результирующую строку
     */
    public static String repeat(CharSequence source, int count) {
        if (count < 1)
            throw new IllegalArgumentException("Repeat count " + count + " is not valid");

        int length = source.length();
        if (length == 0)
            return EMPTY_STRING;

        StringBuilder result = new StringBuilder(length * count);

        while (--count >= 0)
            result.append(source);

        return result.toString();
    }


    /**
     * Собрать строку из частей
     *
     * @param source - набор частей строки
     * @return результирующую строку
     */
    public static String concat(Object ... source) {
        StringBuilder result = new StringBuilder();

        for (Object item : source)
            result.append(item);

        return result.toString();
    }


    /**
     * Собрать строку из набора, используя разделитель
     *
     * @param sepp   - разделитель
     * @param source - набор кусков строки
     * @return результирующую строку
     */
    public static String join(String sepp, Iterable<?> source) {
        StringBuilder result = new StringBuilder();

        int index = 0;
        for (Object item : source) {
            if (index > 0)
                result.append(sepp);

            result.append(item);
            index++;
        }

        return result.toString();
    }


    public static String join(String sepp, Object ... source) {
        return join(sepp, new ArrayIterable<Object>(source));
    }


    /**
     * Собрать строку из набора, используя разделитель,
     * исключая пустые элементы из списка
     *
     * @param sepp   - разделитель
     * @param source - набор кусков строки
     * @return результирующую строку
     */
    public static String joinNotEmpty(String sepp, Iterable<?> source) {
        StringBuilder result = new StringBuilder();

        int index = 0;
        for (Object object : source) {
            String item = toString(object);

            if (isEmpty(item))
                continue;

            if (index > 0)
                result.append(sepp);

            result.append(item);
            index++;
        }

        return result.toString();
    }


    public static String joinNotEmpty(String sepp, Object ... source) {
        return joinNotEmpty(sepp, new ArrayIterable<Object>(source));
    }



    /**
     * Разбивка строки по разделителю
     *
     * @param source - исходная строка
     * @param splitter - разделитель
     * @return набор непустых элементов
     */
    public static List<String> splitNotEmpty(String source, String splitter) {
        if (isEmpty(source))
            return Collections.emptyList();

        String[] parsed = source.split(splitter);

        if (parsed.length == 0)
            return Collections.emptyList();

        List<String> result = new ArrayList<String>(parsed.length);

        for (String item : parsed)
            if (!isEmpty(item = trim(item)))
                result.add(item);

        return result;
    }


    /**
     * Разбить строку на части указанного размера
     *
     * @param source - исходная строка
     * @param size   - размер куска
     * @return набор кусков строки
     */
    public static List<String> chunk(String source, int size) {
        if (size < 1)
            throw new IllegalArgumentException("Chunk size " + size + " is not valid");

        int length = source.length();
        if (length == 0)
            return Collections.emptyList();

        List<String> result = new ArrayList<String>((length / size) + 1);
        int next = size;
        int prev = 0;

        while (next < length) {
            result.add(source.substring(prev, next));
            prev = next;
            next+= size;
        }

        if (prev < length) {
            result.add(source.substring(prev));
        }

        return result;
    }


    /**
     * Поиск в массиве элементов
     *
     * @param value - искомый элемент
     * @param array - источник для поиска
     * @return
     */
    @SafeVarargs
    public static <T> boolean inArray(T value, T ... array) {

        if (value != null) {
            for (T item : array)
                if (value.equals(item))
                    return true;
        } else {
            for (T item : array)
                if (item == null)
                    return true;
        }

        return false;
    }



    /**
     * Получить первый не null элемент списка
     *
     * @param array - набор элементов
     * @return первый не null элемент, либо null, если все элементы null
     */
    @SafeVarargs
    public static <T> T notNull(T ... array) {
        for (T item : array)
            if (item != null)
                return item;
        return null;
    }


    public static <T> T notNull(T last) {
        return last;
    }


    public static <T> T notNull(T arg1, T last) {
        if (arg1 != null) return arg1;
        return last;
    }


    public static <T> T notNull(T arg1, T arg2, T last) {
        if (arg1 != null) return arg1;
        if (arg2 != null) return arg2;
        return last;
    }


    public static <T> T notNull(T arg1, T arg2, T arg3, T last) {
        if (arg1 != null) return arg1;
        if (arg2 != null) return arg2;
        if (arg3 != null) return arg3;
        return last;
    }


    /**
     * Составить маппинг значений
     *
     * @param source - исходное значение
     * @param value  - значение по умолчанию
     * @param array  - набор кейсов
     * @return результат
     */
    @SafeVarargs
    public static <K, V> V decode(K source, V value, Entry<K, V> ... array) {
        for (Entry<K, V> entry : array)
            if (isEqual(source, entry.getKey()))
                return entry.getValue();
        return value;
    }


    @SafeVarargs
    public static <K, V> V decode(K source, Entry<K, V> ... array) {
        return decode(source, null, array);
    }



    /**
     * Проверка на заполненность данных
     *
     * @param source - исходный объект
     * @return true / false
     */
    public static boolean isEmpty(CharSequence source) {
        return source == null || source.length() == 0;
    }


    public static boolean isEmpty(Number source) {
        return source == null || source.doubleValue() == 0;
    }


    public static boolean isEmpty(Collection<?> source) {
        return source == null || source.size() == 0;
    }


    public static boolean isEmpty(Map<?, ?> source) {
        return source == null || source.size() == 0;
    }


    public static boolean isEmpty(Object[] source) {
        return source == null || source.length == 0;
    }



    /**
     * Сравнение объектов с учетом null-значений
     *
     * @param first - объект для сравнения
     * @param other - объект для сравнения
     * @return true когда объекты одинаковы
     */
    public static boolean isEqual(Object first, Object other) {
        return (first == other) || (first != null && first.equals(other));
    }


    /**
     * Получить хэш-код от заданных параметров
     *
     * @param value - список параметров
     * @return хэш-код
     */
    public static int hashCode(Object ... value) {
        return hashCode0(value);
    }


    private static int hashCode0(Object value) {
        if (value == null)
            return 11;

        if (!value.getClass().isArray())
            return value.hashCode();

        int length = Array.getLength(value),
            hash = 17;

        for (int index = 0; index < length; index++)
            hash = 31 * hash + hashCode0(Array.get(value, index));

        return hash;
    }




    /**
     * Получить строку стектрейса ошибки
     *
     * @param e - объект ошибки
     * @return строку стектрейса
     */
    public static String getErrorTrace(Throwable e) {
        StringWriter str = new StringWriter();
        PrintWriter prn = new PrintWriter(str);

        e.printStackTrace(prn);
        prn.flush();

        return str.toString().trim();
    }



    private Misc() {}
}
