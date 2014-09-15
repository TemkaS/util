/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;

import java.util.Iterator;




/**
 * Коллекция-обертка над массивом
 */
public class ArrayIterable<T> implements Iterable<T> {
    protected final T[] array;


    public ArrayIterable(T[] array) {
        if (array == null)
            throw new IllegalArgumentException("Source array can't be null");
        this.array = array;
    }


    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(array);

    }

}