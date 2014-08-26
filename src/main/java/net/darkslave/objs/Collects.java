/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.objs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;




public class Collects {


    /**
     * Собрать значения коллекции
     */
    public static <T, V, R extends Collection<V>> R values(Collection<T> source, Getter<? super T, ? extends V> getter, R result) {
        for (T item : source)
            result.add(getter.get(item));
        return result;
    }


    public static <T, V> List<V> values(Collection<T> source, Getter<? super T, ? extends V> getter) {
        return values(source, getter, new ArrayList<V>());
    }



    /**
     * Собрать из коллекции мапу, используя ключи из элемента коллекции
     */
    public static <K, V, R extends Map<K, V>> R createMap(Collection<V> source, Getter<? super V, ? extends K> getter, R result) {
        for (V item : source)
            result.put(getter.get(item), item);
        return result;
    }


    public static <K, V> Map<K, V> createMap(Collection<V> source, Getter<? super V, ? extends K> getter) {
        return createMap(source, getter, new LinkedHashMap<K, V>());
    }



    /**
     * Заполнить коллекцию указанным значением, до указанного размера
     */
    public static <T, R extends Collection<T>> R fill(R target, T item, int length) {
        int required = length - target.size();
        while (--required >= 0)
            target.add(item);
        return target;
    }



    private Collects() {}
}
