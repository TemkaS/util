/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.*;




public class Regexp {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String   EMPTY_STRING       = "";


    public static Replacer<String> RETURN_FIRST = new Replacer<String>() {
        @Override
        public String replace(String[] match) {
            return match.length > 1 ? match[1] : EMPTY_STRING;
        }
    };


    public static interface Replacer<T> {
        T replace(String[] match);
    }




    /**
     * Получить список совпадений
     *
     * @param matcher - обработчик совпадений
     * @return список совпадений
     */
    public static String[] match(Matcher matcher) {
        int count = matcher.groupCount();

        String[] match = new String[count + 1];
        for (int i = 0; i <= count; i++)
            match[i] = matcher.group(i);

        return match;
    }



    /**
     * Получить список совпадений в строке по регулярному выражению
     *
     * @param source  - источник
     * @param pattern - регулярное выражение
     * @return список совпадений
     */
    public static String[] match(CharSequence source, Pattern pattern) {
        Matcher matcher = pattern.matcher(source);

        if (!matcher.find())
            return EMPTY_STRING_ARRAY;

        return match(matcher);
    }


    public static String[] match(CharSequence source, String pattern) {
        return match(source, get(pattern));
    }


    /**
     * Получить список совпадений в строке по регулярному выражению
     *
     * @param source  - источник
     * @param pattern - регулярное выражение
     * @param replacer - функция-заменитель
     * @return список совпадений
     */
    public static <T> T match(CharSequence source, Pattern pattern, Replacer<T> replacer) {
        Matcher matcher = pattern.matcher(source);

        if (!matcher.find())
            return replacer.replace(EMPTY_STRING_ARRAY);

        return replacer.replace(match(matcher));
    }


    public static <T> T match(CharSequence source, String pattern, Replacer<T> replacer) {
        return match(source, get(pattern), replacer);
    }



    /**
     * Получить все списки совпадений в строке по регулярному выражению
     *
     * @param source  - источник
     * @param pattern - регулярное выражение
     * @return список списков совпадений
     */
    public static List<String[]> matchAll(CharSequence source, Pattern pattern) {
        List<String[]> result = new ArrayList<String[]>();
        Matcher matcher = pattern.matcher(source);

        while (matcher.find())
            result.add(match(matcher));

        return result;
    }


    public static List<String[]> matchAll(CharSequence source, String pattern) {
        return matchAll(source, get(pattern));
    }


    /**
     * Получить все списки совпадений в строке по регулярному выражению
     *
     * @param source   - источник
     * @param pattern  - регулярное выражение
     * @param replacer - функция-заменитель
     * @return список списков совпадений
     */
    public static <T> List<T> matchAll(CharSequence source, Pattern pattern, Replacer<T> replacer) {
        List<T> result = new ArrayList<T>();
        Matcher matcher = pattern.matcher(source);

        while (matcher.find())
            result.add(replacer.replace(match(matcher)));

        return result;
    }


    public static <T> List<T> matchAll(CharSequence source, String pattern, Replacer<T> replacer) {
        return matchAll(source, get(pattern), replacer);
    }



    /**
     * Замена строки по регулярному выражению
     *
     * @param source   - источник
     * @param pattern  - регулярное выражение
     * @param replacer - функция-заменитель
     * @return результирующую строку
     */
    public static String replaceAll(CharSequence source, Pattern pattern, Replacer<?> replacer) {
        int length = source.length();
        if (length == 0)
            return EMPTY_STRING;

        int cursor = 0;

        StringBuilder result = new StringBuilder(length + (length >>> 2));
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            int begin = matcher.start();

            if (begin > cursor)
                result.append(source, cursor, begin);

            result.append(replacer.replace(match(matcher)));

            cursor = matcher.end();
        }

        if (length > cursor)
            result.append(source, cursor, length);

        return result.toString();
    }


    public static String replaceAll(CharSequence source, String pattern, Replacer<?> replacer) {
        return replaceAll(source, get(pattern), replacer);
    }



    private static final Map<String, Pattern> cache = new ConcurrentHashMap<String, Pattern>();


    /**
     * @desc Получить компилированный объект рег.выражения из кеша
     *
     * @param source - строка рег. выражения
     * @return скопмилированный объект рег.выражения
     */
    public static Pattern get(String source) {
        final Pattern cached = cache.get(source);

        if (cached != null)
            return cached;

        final Pattern result = Pattern.compile(source);

        cache.put(source, result);
        return result;
    }



    private Regexp() {}
}
