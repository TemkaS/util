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

import net.darkslave.objs.Entry;





public class Misc {
    public static final String   EMPTY_STRING = "";
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String  SPACE_CHARS = "\\u0000-\\u0020\\u00A0\\u1680\\u180E\\u2000\\u2001\\u2002\\u2003\\u2004\\u2005\\u2006\\u2007\\u2008\\u2009\\u200A\\u200B\\u202F\\u205F\\u3000\\uFEFF";
    private static final Pattern TRIM_SPACES = Pattern.compile("^[" + SPACE_CHARS + "]+|[" + SPACE_CHARS + "]+$");





    /*******************************************************************************************************************
     * функции работы со строками
     */

    /**
     * Получить строковое представление или пустую строку, если null
     *
     * @param value - исходный объект
     * @return строка
     */
    public static String toString(Object value) {
        return value != null ? value.toString() : EMPTY_STRING;
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
        StringBuilder result = new StringBuilder();

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



    /*******************************************************************************************************************
     * функции работы с массивами
     */

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
        List<String> result = new ArrayList<String>();

        if (isEmpty(source))
            return result;

        int length = source.length(),
            next = size,
            prev = 0;

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
    public static <T> boolean inArray(Object value, T ... array) {

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


    public static <T> List<T> sublist(List<T> source, int start, int end) {
        int length = source.size();

        if (start >= length) {
            return source.subList(0, 0);

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
                return source.subList(0, 0);
        }

        return source.subList(start, end);
    }



    /*******************************************************************************************************************
     * обобщенные функции
     */

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
