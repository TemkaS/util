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



    /**
     * Получить список частей строки, удовлетворяющих регулярному выражению
     *
     * @param pattern - регулярное выражение
     * @param source  - строка источник
     * @return список частей
     */
    public static String[] match(Pattern pattern, String source) {
        Matcher resp = pattern.matcher(source);

        if (!resp.find())
            return EMPTY_STRING_ARRAY;

        int size = resp.groupCount();
        String[] result = new String[size + 1];

        for (int i = 0; i <= size; i++)
            result[i] = resp.group(i);

        return result;
    }


    public static String[] match(String pattern, String source) {
        return match(get(pattern), source);
    }



    /**
     * Получить все списки частей строки, удовлетвоярющих шаблону
     *
     * @param pattern - регулярное выражение
     * @param source  - строка источник
     * @return список списков частей
     */
    public static List<String[]> matchAll(Pattern pattern, String source) {
        List<String[]> result = new ArrayList<String[]>();
        Matcher resp = pattern.matcher(source);

        while (resp.find()) {
            int size = resp.groupCount();
            String[] item = new String[size + 1];

            for (int i = 0; i <= size; i++)
                item[i] = resp.group(i);

            result.add(item);
        }

        return result;
    }


    public static List<String[]> matchAll(String pattern, String source) {
        return matchAll(get(pattern), source);
    }


    private static final Map<String, Pattern> cache = new ConcurrentHashMap<String, Pattern>();


    /**
     * @desc Получить компилированный объект рег.выражения из кеша
     *
     * @param source - строка рег. выражения
     * @return скопмилированный объект рег.выражения
     */
    public static Pattern get(String source) {
        Pattern cached = cache.get(source);

        if (cached != null)
            return cached;

        final Pattern result = Pattern.compile(source);

        cache.put(source, result);
        return result;
    }



    private Regexp() {}
}
