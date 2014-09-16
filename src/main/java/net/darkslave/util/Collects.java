/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;




public class Collects {


    /**
     * Собрать значения в коллекцию
     *
     * @param source - коллекция-источник
     * @param getter - получатель значений
     * @param result - результирующая коллекция
     * @return результирующую коллекцию
     */
    public static <T, V, R extends Collection<V>> R collect(Iterable<T> source, Getter<? super T, ? extends V> getter, R result) {
        for (T item : source)
            result.add(getter.get(item));
        return result;
    }


    /**
     * Собрать значения в коллекцию
     *
     * @param source - коллекция-источник
     * @param getter - получатель значений
     * @return результирующую коллекцию
     */
    public static <T, V> List<V> collect(Iterable<T> source, Getter<? super T, ? extends V> getter) {
        return collect(source, getter, new ArrayList<V>());
    }



    /**
     * Собрать значения в таблицу
     *
     * @param source - коллекция-источник
     * @param getter - получатель ключей
     * @param result - результирующая таблица
     * @return результирующую таблицу
     */
    public static <K, V, R extends Map<K, V>> R createMap(Iterable<V> source, Getter<? super V, ? extends K> getter, R result) {
        for (V item : source)
            result.put(getter.get(item), item);
        return result;
    }


    /**
     * Собрать значения в таблицу
     *
     * @param source - коллекция-источник
     * @param getter - получатель ключей
     * @return результирующую таблицу
     */
    public static <K, V> Map<K, V> createMap(Iterable<V> source, Getter<? super V, ? extends K> getter) {
        return createMap(source, getter, new LinkedHashMap<K, V>());
    }



    /**
     * Заполнить коллекцию до указанного размера
     *
     * @param target - целевая коллекция
     * @param item   - заполнитель
     * @param length - необходимый размер
     * @return целевую коллекцию
     */
    public static <T, R extends Collection<T>> R fill(R target, T item, int length) {
        int required = length - target.size();
        while (--required >= 0)
            target.add(item);
        return target;
    }



    /**
     * Получить часть коллекции
     *
     * @param source - коллекция-источник
     * @param start  - начальный индекс
     * @param end    - конечный индекс
     * @return часть коллекции
     */
    public static <T> List<T> sublist(List<T> source, int start, int end) {
        int length = source.size();

        if (start >= length) {
            return Collections.emptyList();

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
                return Collections.emptyList();
        }

        return Collections.unmodifiableList(source.subList(start, end));
    }




    private Collects() {}
}
